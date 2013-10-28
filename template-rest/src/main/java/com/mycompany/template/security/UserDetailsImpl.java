package com.mycompany.template.security;

import com.mycompany.template.beans.Role;
import com.mycompany.template.beans.User;
import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: azee
 */
public class UserDetailsImpl implements UserDetails {
    private User user;
    private Collection<GrantedAuthority> authorities;

    private final static Logger log = Logger.getLogger(UserDetailsImpl.class);

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            return new ArrayList<GrantedAuthority>();
        }
        else return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        if (user != null){
            return user.getName();
        }
        else{
            return null;
        }
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRoles() {
        if (user == null) {
            return;
        }
        this.authorities = new HashSet<GrantedAuthority>();
        for (final Role role : user.getRoles()) {
            GrantedAuthority grandAuthority = new GrantedAuthority() {
                    public String getAuthority() {
                        return role.value();
                    }
                };
            this.authorities.add(grandAuthority);

        }
    }
}