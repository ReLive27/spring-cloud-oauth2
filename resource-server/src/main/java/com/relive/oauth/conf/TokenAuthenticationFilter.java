package com.relive.oauth.conf;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.relive.service.RoleService;
import com.relive.utils.JsonUtils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 解析网关过滤token，进行授权
 *
 * @Author ReLive
 * @Date 2021/2/1-19:39
 */
public class TokenAuthenticationFilter extends BasicAuthenticationFilter {
    private RoleService roleService;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager, RoleService roleService) {
        super(authenticationManager);
        this.roleService = roleService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            token = new String(Base64.decodeBase64(token), Charset.forName("UTF-8"));
            ObjectNode objectNode = JsonUtils.jsonToObject(token);
            long id = objectNode.path("userId").asLong();
            String username = objectNode.path("username").asText();
            int size = objectNode.path("roles").size();
            String[] roles = new String[size];
            for (int i = 0; i < size; i++) {
                roles[i] = objectNode.path("roles").get(i).asText();
            }
            List<String> authorities = roleService.getPermissionCodeByRoleCode(Arrays.asList(roles));
            CurrentUser user = new CurrentUser();
            user.setId(id);
            user.setUsername(username);
            user.setRoles(new HashSet<>(Arrays.asList(roles)));
            user.setAuthorizations(authorities);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, AuthorityUtils.createAuthorityList(authorities.toArray(new String[authorities.size()])));
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
