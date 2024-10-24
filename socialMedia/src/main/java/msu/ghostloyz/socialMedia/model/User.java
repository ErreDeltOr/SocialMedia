package msu.ghostloyz.socialMedia.model;

import lombok.*;
import msu.ghostloyz.socialMedia.model.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Builder
@ToString
public class User implements UserDetails {
    private Integer id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Integer age;
    private Set<Role> roles;
    private Boolean active;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
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
        return active;
    }
}
