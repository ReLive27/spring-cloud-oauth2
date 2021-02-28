//package com.relive.oauth.conf;
//
//import com.relive.api.Result;
//import com.relive.utils.JsonUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.nio.charset.StandardCharsets;
//
///**
// * @Author ReLive
// * @Date 2021/2/5-9:57
// */
//@Component
//@Slf4j
//public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        log.info("Token失效，禁止访问 {}", request.getRequestURI());
//        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        String result = JsonUtils.objectToJson(Result.error(401, "token令牌无效"));
//        response.setStatus(HttpStatus.SC_OK);
//        PrintWriter printWriter = response.getWriter();
//        printWriter.append(result);
//    }
//}
