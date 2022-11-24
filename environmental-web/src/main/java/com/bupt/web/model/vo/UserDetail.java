package com.bupt.web.model.vo;

import com.bupt.web.model.pojo.User;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@ApiModel("用户")
@NoArgsConstructor
public class UserDetail extends User implements UserDetails {
    private List<GrantedAuthority> authorities;

    @ApiModelProperty("角色")
    private List<String> roles;


    public void setRoles(List<String> roles) {
        this.roles = roles;
        List<GrantedAuthority> authorities = Lists.newArrayList();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.toUpperCase()));
        }
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
