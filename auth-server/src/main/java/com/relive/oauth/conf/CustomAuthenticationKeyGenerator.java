package com.relive.oauth.conf;

import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * 自定义存储在redis中token的key值，解决相同用户名密码生成token一致问题
 *
 * @Author ReLive
 * @Date 2021/3/15-16:20
 */
public class CustomAuthenticationKeyGenerator implements AuthenticationKeyGenerator {

    @Override
    public String extractKey(OAuth2Authentication authentication) {
        Map<String, String> values = new LinkedHashMap();
        OAuth2Request authorizationRequest = authentication.getOAuth2Request();
        if (!authentication.isClientOnly()) {
            values.put("username", authentication.getName());
        }

        values.put("client_id", authorizationRequest.getClientId());
        if (authorizationRequest.getScope() != null) {
            values.put("scope", OAuth2Utils.formatParameterList(new TreeSet(authorizationRequest.getScope())));
        }
        values.put("random", String.valueOf(new Date().getTime()));

        return generateKey(values);
    }

    protected String generateKey(Map<String, String> values) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(values.toString().getBytes("UTF-8"));
            return String.format("%032x", new BigInteger(1, bytes));
        } catch (NoSuchAlgorithmException var4) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).", var4);
        } catch (UnsupportedEncodingException var5) {
            throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).", var5);
        }
    }
}
