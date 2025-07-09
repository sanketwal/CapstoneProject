package org.example.userauthservice_june4.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.userauthservice_june4.models.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

import java.io.Serial;

@JsonDeserialize
@Getter
@Setter
@NoArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority {
    @Serial
    private static final long serialVersionUID = 591816836330679269L;

    private String authority;

    public CustomGrantedAuthority(Role role) {
        this.authority = role.getName();
    }
}
