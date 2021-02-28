package com.relive.oauth.conf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * @Author ReLive
 * @Date 2021/2/1-19:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUser {
    private Long id;
    private String username;
    private Set<String> roles;
    private List<String> authorizations;
}
