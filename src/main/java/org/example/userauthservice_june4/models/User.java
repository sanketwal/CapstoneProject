package org.example.userauthservice_june4.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
public class User extends BaseModel {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    private List<Role> roles;
}

//1         m
//user     role
//m          1
//
//m   :  m