package org.example.userauthservice_june4.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.userauthservice_june4.models.Role;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private List<Role> roles = new ArrayList<>();
}
