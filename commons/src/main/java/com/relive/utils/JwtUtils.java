package com.relive.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author ReLive
 * @Date 2021/2/26-22:18
 */
public class JwtUtils {
    private JwtUtils() {
    }

    private static Map<String, JWTVerifier> verifierMap = new HashMap<>();

    private static final String DEFAULT_SIGN_KEY = "hello-word";

    public static DecodedJWT verifyToken(String tokenString, String signingKey) {
        if (StringUtils.isEmpty(signingKey)) {
            signingKey = DEFAULT_SIGN_KEY;
        }
        JWTVerifier verifier = verifierMap.get(signingKey);
        if (verifier == null) {
            synchronized (verifierMap) {
                verifier = verifierMap.get(signingKey);
                if (verifier == null) {
                    Algorithm algorithm = Algorithm.HMAC256(signingKey);
                    verifier = JWT.require(algorithm).build();
                    verifierMap.put(signingKey, verifier);
                }
            }
        }
        DecodedJWT verify = verifier.verify(tokenString);
        return verify;
    }

    public static DecodedJWT decodedJWT(String token) {
        DecodedJWT decode = JWT.decode(token);
        return decode;
    }
}
