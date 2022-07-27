package io.github.julianobrl.archtecture.dtos;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.github.julianobrl.archtecture.model.Users;
import io.github.julianobrl.archtecture.security.UserRole;

import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@ToString
@RequiredArgsConstructor
public class ApplicationUsers implements UserDetails {

	private static final long serialVersionUID = 1L;
	private final Users userObject;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(userObject.isAdmin()) {
            if(userObject.isReadOnlyUser()) {
                return UserRole.MODERATOR.getAuthorities();
            }

            return UserRole.ADMIN.getAuthorities();
        }
        if(userObject.isReadOnlyUser()) {
            return UserRole.GUEST.getAuthorities();
        }

        return UserRole.USER.getAuthorities();
    }

    @Override
    public String getPassword() {
        log.info(String.format("User: %s - Password: %s", userObject.getUsername(), userObject.getPassword()));
        return userObject.getPassword();
    }

    @Override
    public String getUsername() {
        return userObject.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !userObject.isAccountExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !userObject.isAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userObject.getCredentialsExpiryDate()==null || userObject.getCredentialsExpiryDate().isAfter(LocalDateTime.now());
    }

    @Override
    public boolean isEnabled() {
        return !userObject.isDeleted();
    }
    
}