package com.relive.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author ReLive
 * @Date 2021/1/28-15:55
 */
@Slf4j
@RestController
public class CodeController {

    private static final String AUTH = "Basic";

//    @Autowired
//    private OAuth2RestTemplate oAuth2RestTemplate;

    @Autowired
    private RestTemplate restTemplate;

    //TODO 后续将配置移到配置文件中
    @GetMapping("/code")
    public String getCode(String code) {
        String tokenUrl = "http://localhost:6001/oauth/token";
        OkHttpClient httpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder().add("grant_type", "authorization_code")
                .add("redirect_uri", "http://localhost:8001/code")
                .add("code", code).build();

        String clientSecret = Base64.encodeBase64String("code-client:code-client-888".getBytes());

        Request request = new Request.Builder()
                .url(tokenUrl)
                .post(body)
                .addHeader("Authorization", "Basic Y29kZS1jbGllbnQ6Y29kZS1jbGllbnQtODg4OA==")
                .build();

        try {
            //获取token并验证
            Response response = httpClient.newCall(request).execute();
            String result = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            Map tokenMap = objectMapper.readValue(result, Map.class);
            String accessToken = tokenMap.get("access_token").toString();
            //TODO 后续改为java-jwt,并添加验证，错误则抛出异常
            Claims claims = Jwts.parser().setSigningKey("dev".getBytes(StandardCharsets.UTF_8)).parseClaimsJws(accessToken).getBody();
            return accessToken;

        } catch (Exception e) {
            log.error("", e);
        }

        return null;

    }


    /**
     * 请求资源服务器，并获取数据返回前端
     *
     * @return
     */
    @GetMapping("/thirdApp")
    public String thirdApp() {
        //假设将token存储到redis中，如果没有token从执行方法获取
        String accessToken = getToken();
        String result = "";
        try {
            Request requestResource = new Request.Builder()
                    .url("http://localhost:7001/get")
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();
            OkHttpClient httpClient1 = new OkHttpClient();
            Response resourceResponse = httpClient1.newCall(requestResource).execute();
            result = resourceResponse.body().string();
        } catch (Exception e) {
            log.error("", e);
        }
        return result;
    }

    /**
     * 目前此方式获取授权码401
     *
     * @return
     */
    public String getToken() {
        String url = "http://localhost:6001/oauth/authorize?client_id=code-client&response_type=code&redirect_uri=http://localhost:8001/code";

        String forObject = restTemplate.getForObject(url, String.class);
//        Request request=new Request.Builder()
//                .get()
//                .url("http://localhost:6001/oauth/authorize?client_id=code-client&response_type=code&redirect_uri=http://localhost:8001/code")
//                .build();
//        OkHttpClient httpClient=new OkHttpClient();
//        try {
//            Response response = httpClient.newCall(request).execute();
//            return response.body().string();
//        } catch (IOException e) {
//            log.error("",e);
//        }
        return forObject;
    }

    /**
     * 密码模式
     *
     * @return
     */
    @GetMapping("/password")
    public String clientMode() throws IOException {
        String url = "http://localhost:6001/oauth/token";
//
//        OkHttpClient httpClient = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add("grant_type", "password")
//                .add("client_id", "code-client")
//                .add("client_secret", "code-client-8888")
//                .add("username","admin")
//                .add("password","123456")
//                .add("scope", "all").build();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
//        Response response = httpClient.newCall(request).execute();
//        boolean successful = response.isSuccessful();
//        int code = response.code();
//        String result = response.body().string();

        ObjectMapper objectMapper=new ObjectMapper();
        ObjectNode objectNode=objectMapper.createObjectNode();
        objectNode.put("grant_type", "password");
        objectNode.put("client_id", "code-client");
        objectNode.put("client_secret", "code-client-8888");
        objectNode.put("scope", "all");
        objectNode.put("username", "admin");
        objectNode.put("password", "123456");

        HttpEntity<String> httpEntity=new HttpEntity<>(objectNode.toString());

        String body = httpEntity.getBody();
        ResponseEntity<ObjectNode> forObject = restTemplate.exchange(url, HttpMethod.POST,httpEntity,ObjectNode.class);
        int statusCodeValue = forObject.getStatusCodeValue();
//

        return "success";
    }
}
