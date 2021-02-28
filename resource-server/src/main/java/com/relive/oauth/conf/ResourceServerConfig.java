//package com.relive.oauth.conf;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.core.GrantedAuthorityDefaults;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//
///**
// * @Author ReLive
// * @Date 2021/1/17-16:41
// */
//@Configuration
//@EnableResourceServer
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
//public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
//
////    @Value("${security.oauth2.client.client-id}")
////    private String clientId;
////    @Value("${security.oauth2.client.client-secret}")
////    private String secret;
////    @Value("${security.oauth2.authorization.check-token-access}")
////    private String checkTokenEndpointUrl;
//
//    private static String TOKEN_SIGNINGKEY = "relive-code";
//
//    @Autowired
//    private AuthExceptionEntryPoint authExceptionEntryPoint;
//
//
//    @Bean
//    public TokenStore jwtTokenStore() {
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }
//
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
//        accessTokenConverter.setSigningKey(TOKEN_SIGNINGKEY);
//        accessTokenConverter.setVerifierKey(TOKEN_SIGNINGKEY);
//        return accessTokenConverter;
//    }
//
//    /**
//     * 通过check-token-uri 验证token，如果使用JWT方式，此方式可以不使用，
//     * JWT可以自行验证token正确性
//     *
//     * @return
//     */
////    @Bean
////    public RemoteTokenServices tokenServices() {
////        RemoteTokenServices tokenServices = new RemoteTokenServices();
////        tokenServices.setClientId(clientId);
////        tokenServices.setClientSecret(secret);
////        tokenServices.setCheckTokenEndpointUrl(checkTokenEndpointUrl);
////        return tokenServices;
////    }
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources.resourceId("relive-client");
//        resources.stateless(true);
//        resources.tokenStore(jwtTokenStore());
//        resources.authenticationEntryPoint(authExceptionEntryPoint); //添加异常处理类
//    }
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()//.antMatchers("/**").permitAll()
//                .anyRequest().access("#oauth2.hasScope('all')")//token中作用域包含all的才可以访问
//                .and().csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    }
//
//    @Bean
//    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
//        return new GrantedAuthorityDefaults("");
//    }
//}
