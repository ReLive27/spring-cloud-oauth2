package com.relive.oauth.conf;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.Date;

/**
 * token 续订
 *
 * @Author ReLive
 * @Date 2021/2/18-23:20
 */

public class CustomRedisTokenStore extends RedisTokenStore {
    private JdbcClientDetailsService jdbcClientDetailsService;

    public CustomRedisTokenStore(RedisConnectionFactory connectionFactory, JdbcClientDetailsService clientDetailsService) {
        super(connectionFactory);
        this.jdbcClientDetailsService = clientDetailsService;
    }

    public CustomRedisTokenStore(RedisConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        OAuth2Authentication result = readAuthentication(token.getValue());
        if (result != null) {
            // 如果token没有失效  更新AccessToken过期时间
            DefaultOAuth2AccessToken oAuth2AccessToken = (DefaultOAuth2AccessToken) token;

            //重新设置过期时间
            int validitySeconds = getAccessTokenValiditySeconds(result.getOAuth2Request());
            if (validitySeconds > 0) {
                oAuth2AccessToken.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
            }

            //将重新设置过的过期时间重新存入redis, 此时会覆盖redis中原本的过期时间
            storeAccessToken(token, result);
        }
        return result;
    }

    protected int getAccessTokenValiditySeconds(OAuth2Request clientAuth) {
        //如果client-id,client-secret等信息配置在数据库中通过此方法获取过期时间毫秒数
        if (jdbcClientDetailsService != null) {
            ClientDetails client = jdbcClientDetailsService.loadClientByClientId(clientAuth.getClientId());
            Integer validity = client.getAccessTokenValiditySeconds();
            if (validity != null) {
                return validity;
            }
        }
        // default 60s.
        int accessTokenValiditySeconds = 60;
        return accessTokenValiditySeconds;
    }
}

