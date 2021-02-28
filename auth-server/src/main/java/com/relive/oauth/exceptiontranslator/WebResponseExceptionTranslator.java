package com.relive.oauth.exceptiontranslator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.UnsupportedResponseTypeException;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * OAuth2异常转换类
 *
 * @Author ReLive
 * @Date 2021/1/21-10:35
 */
@Slf4j
@Component
public class WebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

    public static final String BAD_MSG = "Bad credentials";
    public static final String INVALID_TOKEN = "Token was not recognised";

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        log.error("错误为:" + e);
        OAuth2Exception oAuth2Exception;
        if (e.getMessage() != null && e.getMessage().equals(BAD_MSG)) {
            oAuth2Exception = new InvalidGrantException("用户名密码错误", e);
        } else if (e.getMessage() != null && e.getMessage().equals(INVALID_TOKEN)) {
            oAuth2Exception = new InvalidGrantException("token令牌无效", e);
        } else if (e instanceof InternalAuthenticationServiceException) {
            String errorMessage = e.getMessage();
            if (errorMessage.startsWith("UserDetailsService")) {
                errorMessage = "用户名密码错误";
            }
            oAuth2Exception = new InvalidGrantException(errorMessage, e);
        } else {
            oAuth2Exception = new UnsupportedResponseTypeException("系统服务内部错误", e);
        }
        return super.translate(oAuth2Exception);
    }
}
