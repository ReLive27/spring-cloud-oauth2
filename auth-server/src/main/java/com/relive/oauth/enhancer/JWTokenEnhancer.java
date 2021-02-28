package com.relive.oauth.enhancer;

import com.relive.oauth.entity.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * Jwt增强器 不仅JWT使用,RedisToken 的方式同样可以
 *
 * @Author ReLive
 * @Date 2021/1/20-20:07
 */
public class JWTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Object credentials = oAuth2Authentication.getCredentials();
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        //添加用户Id和角色code
        CustomUserDetails customUserDetails = (CustomUserDetails) oAuth2Authentication.getPrincipal();
        Map<String, Object> info = new HashMap<>();
        info.put("userId", customUserDetails.getId());
        info.put("roles", customUserDetails.getRoles());
        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(info);
        return oAuth2AccessToken;
    }
}
