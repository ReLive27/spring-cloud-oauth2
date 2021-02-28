package com.relive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//import org.springframework.security.oauth2.client.OAuth2RestTemplate;
//import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.client.RestTemplate;

/**
 * @Author ReLive
 * @Date 2021/1/28-15:58
 */
@SpringBootApplication
public class ThirdPartyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThirdPartyApplication.class,args);
    }

//    @Bean
////    public OAuth2RestTemplate oAuth2RestTemplate(ClientCredentialsResourceDetails details){
////        return new OAuth2RestTemplate(details);
////    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
