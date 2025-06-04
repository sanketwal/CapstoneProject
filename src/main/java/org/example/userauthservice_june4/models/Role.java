package org.example.userauthservice_june4.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Role extends BaseModel {

    private String name;

    //If needed, we can add list of permissions
}
