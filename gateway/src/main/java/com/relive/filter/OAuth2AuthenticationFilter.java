package com.relive.filter;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.relive.api.Result;
import com.relive.exception.BusinessException;
import com.relive.utils.JsonUtils;
import com.relive.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ReLive
 * @Date 2021/1/23-17:31
 */
@Slf4j
@Component
public class OAuth2AuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${ignore.path}")
    private List<String> ignorePaths;

    @Value("${security.oauth2.client.authorization.check-token-uri}")
    private String checkTokenUri;

    private static final String AUTHORIZATION = "Authorization";

    private static String TOKEN_SIGNINGKEY = "relive-code";

    private PathPatternParser pathPatternParser = new PathPatternParser();


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        if (isMatchSkipIgnorePath(exchange)) {
            return chain.filter(exchange);
        }
        String token = headers.getFirst(AUTHORIZATION);
        if (StringUtils.isEmpty(token)) {
            token = request.getQueryParams().getFirst(AUTHORIZATION);
        }
        if (StringUtils.isEmpty(token)) {
            return unauthorized(exchange, HttpStatus.UNAUTHORIZED, "无效的用户令牌");
        }
        token = token.replace("Bearer ", "");
        try {
            if (!validationToken(token)) {
                return unauthorized(exchange, HttpStatus.UNAUTHORIZED, "无效的用户令牌");
            }
            //DecodedJWT decodedJWT = JwtUtils.verifyToken(token, TOKEN_SIGNINGKEY);
            //换成解析JWT不做校验
            DecodedJWT decodedJWT = JwtUtils.decodedJWT(token);
            Map<String, Claim> claims = decodedJWT.getClaims();
            log.warn("claim:{}", claims);

            Long userId = claims.get("userId") == null ? 0 : claims.get("userId").asLong();
            String username = claims.get("user_name") == null ? "" : claims.get("user_name").asString();
            String[] roles = claims.get("roles") == null ? null : claims.get("roles").asArray(String.class);
            Map<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            map.put("username", username);
            map.put("roles", roles);
            String jsonToken = JsonUtils.objectToJson(map);
            ServerHttpRequest build = exchange.getRequest().mutate().header("token", Base64.encodeBase64String(jsonToken.getBytes())).build();
            exchange.mutate().request(build).build();
            return chain.filter(exchange);
        } catch (TokenExpiredException e) {
            return unauthorized(exchange, HttpStatus.UNAUTHORIZED, "无效的用户令牌:已过期");
        } catch (SignatureVerificationException e) {
            return unauthorized(exchange, HttpStatus.UNAUTHORIZED, "无效的用户令牌:无效签名");
        } catch (Exception e) {
            return unauthorized(exchange, HttpStatus.UNAUTHORIZED, "无效的用户令牌");
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }


    public boolean isMatchSkipIgnorePath(ServerWebExchange exchange) {
        List<PathPattern> pathPatterns = new ArrayList<>();
        ignorePaths.forEach(ignorePath -> {
            PathPattern pathPattern = pathPatternParser.parse(ignorePath);
            pathPatterns.add(pathPattern);
        });
        PathContainer pathContainer = PathContainer.parsePath(exchange.getRequest().getURI().getRawPath());
        for (PathPattern pathPattern : pathPatterns) {
            if (pathPattern.matches(pathContainer)) {
                return true;
            }
        }
        return false;
    }

    public Mono<Void> unauthorized(ServerWebExchange exchange, HttpStatus status, String statement) {
        exchange.getResponse().getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        exchange.getResponse().setStatusCode(status);
        String result = JsonUtils.objectToJson(Result.error(status.value(), statement));
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(result.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }

    private boolean validationToken(String token) {
        OkHttpClient httpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("token", token).build();
        Request request = new Request.Builder()
                .url(checkTokenUri)
                .post(body)
                .build();
        Response response = null;
        try {
             response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                return false;
            }
        } catch (IOException e) {
            throw new BusinessException("系统服务内部错误");
        }finally {
            if(response!=null){
                response.close();
            }
        }

        return true;
    }
}
