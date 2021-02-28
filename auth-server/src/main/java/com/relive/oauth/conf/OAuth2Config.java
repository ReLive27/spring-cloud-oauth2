package com.relive.oauth.conf;

import com.relive.oauth.constant.AuthConstant;
import com.relive.oauth.enhancer.JWTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author ReLive
 * @Date 2021/1/17-15:37
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private WebResponseExceptionTranslator webResponseExceptionTranslator;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

//    此方式生成jwt token 不能存储在redis中
//    @Bean
//    public TokenStore jwtTokenStore() {
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }

    /**
     * 注册redistokenstore
     *
     * @return
     */
    @Bean
    public RedisTokenStore redisTokenStore() {
        //RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        //redisTokenStore.setPrefix(TOKEN_PREFIX);
        RedisTokenStore redisTokenStore = new CustomRedisTokenStore(redisConnectionFactory, jdbcClientDetailsService());
        redisTokenStore.setPrefix(AuthConstant.TOKEN_PREFIX);
        return redisTokenStore;
    }

    /**
     * 注册 token JWT转换
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey(AuthConstant.TOKEN_SIGNINGKEY);
        accessTokenConverter.setVerifierKey(AuthConstant.TOKEN_SIGNINGKEY);
        return accessTokenConverter;
    }


    /**
     * 注册jwt增强器
     */
    @Bean
    public TokenEnhancer jwtTokenEnhancer() {
        return new JWTokenEnhancer();
    }


    /**
     * authenticationManage() 调用此方法才能支持 password 模式。
     * userDetailsService() 设置用户验证服务。
     * tokenStore() 指定 token 的存储方式。
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        /*无添加Jwt增强器配置
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(myUserDetailsService)
                //jwt token方式
                .tokenStore(jwtTokenStore)
                .accessTokenConverter(jwtAccessTokenConverter);*/
        /**jwt 增强模式*/
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> enhancerList = new ArrayList<>();
        enhancerList.add(jwtTokenEnhancer());
        enhancerList.add(jwtAccessTokenConverter()); //添加token JWT转换器
        enhancerChain.setTokenEnhancers(enhancerList);

        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(customUserDetailsService)
                .tokenStore(redisTokenStore()) //添加redis token
                .tokenEnhancer(enhancerChain)
                .exceptionTranslator(webResponseExceptionTranslator); //添加自定义异常类
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //此代码中是使用 inMemory 方式存储的，将配置保存到内存中，相当于硬编码了
//        clients.inMemory()
//                .withClient("code-client")
//                .secret(passwordEncoder.encode("code-client-8888"))
//                .authorizedGrantTypes("refresh_token", "authorization_code", "password","implicit","client_credentials")
//                .scopes("all")
//                .authorities("admin")
//                .autoApprove(false)
//                .redirectUris("http://localhost:8001/code")//授权码模式回调地址
//                .accessTokenValiditySeconds(60) //token过期时间
//                .refreshTokenValiditySeconds(4000); //刷新token过期时间，一般比token时间长
        //正式环境下的做法是持久化到数据库中，比如 mysql 中
        clients.withClientDetails(jdbcClientDetailsService());

//          此方法不生效
//        JdbcClientDetailsServiceBuilder jcsb = clients.jdbc(dataSource);
//        jcsb.passwordEncoder(passwordEncoder);
    }

    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService() {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
        return jdbcClientDetailsService;
    }


    /*
    限制客户端访问认证接口的权限
    第一行代码是允许客户端访问 OAuth2 授权接口，否则请求 token 会返回 401
    第二行和第三行分别是允许已授权用户访问 checkToken 接口和获取 token 接口。
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.allowFormAuthenticationForClients();
        security.checkTokenAccess("permitAll()");
        security.tokenKeyAccess("permitAll()");
    }
}
