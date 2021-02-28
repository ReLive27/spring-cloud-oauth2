认证服务器支持授权码模式，密码模式，刷新token。采用jwt结合redis存储。


## 问题
1.`Handling error: InvalidTokenException, Token was not recognised`


2.授权码模式获取code时，访问请求403
解决：
访问授权链接
http://localhost:8888/oauth/authorize?client_id=cms&client_secret=secret&response_type=code
因为没登录，所以会返回SpringSecurity的默认登录页面，具体代码是http .formLogin().permitAll();
如果要弹窗登录的，可以配置http.httpBasic();这种配置是没有登录页面的，
自定义登录页面可以这样配置http.formLogin().loginPage("/login").permitAll()


```
protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic() //加上此配置
                .and()
                .csrf().disable();
    }
```

3.授权码访问请求后`错误为:error="invalid_grant", error_description="Invalid redirect: http://localhost:8001/code does not match one of the registered values.`
解决：查看AuthorizationEndpoint类，debug进行调试，最终是数据库里存的redirect_uri多了个空格

4.访问/oauth/token时候报401
  - 如果配置支持allowFormAuthenticationForClients的，且url中有client_id和client_secret的会走ClientCredentialsTokenEndpointFilter来保护
  - 如果没有支持allowFormAuthenticationForClients或者有支持但是url中没有client_id和client_secret的。
  走basic认证保护,并使用 base64 编码(client_id:client_secret)之后的值,类似Authorization: Basic dXNlci1jbGllbnQ6dXNlci1zZWNyZXQtODg4OA==


## 遗留问题
授权码模式自定义code长度

## 注意事项
1.'oauth_client_details'表名与字段名固定，查看JdbcClientDetailsService中代码可知
2.数据库中client_secret 字段不能直接是 secret 的原始值，需要经过加密。因为是用的 BCryptPasswordEncoder，所以最终插入的值应该是经过 BCryptPasswordEncoder.encode()之后的值。