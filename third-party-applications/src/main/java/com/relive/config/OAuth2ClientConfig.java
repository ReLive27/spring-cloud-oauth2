package com.relive.config;

import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
//import org.springframework.security.oauth2.client.OAuth2RestOperations;
//import org.springframework.security.oauth2.client.OAuth2RestTemplate;
//import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
//import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
//import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
//import org.springframework.security.oauth2.common.AuthenticationScheme;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * @Author ReLive
 * @Date 2021/1/29-9:49
 */
//@EnableOAuth2Client
//@Configuration
//public class OAuth2ClientConfig {

//    @Bean
//    protected OAuth2ProtectedResourceDetails resource() {
//
//        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
//
//        resource.setAccessTokenUri(tokenUrl);
//        resource.setClientId(clientId);
//        resource.setClientSecret(clientSecret);
//        resource.setClientAuthenticationScheme(AuthenticationScheme.form);
//        resource.setUsername(username);
//        resource.setPassword(password + passwordToken);
//
//        return resource;
//    }
//
//    @Bean
//    public OAuth2RestOperations restTemplate() {
//        return new OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext(new DefaultAccessTokenRequest()));
//    }
//}
