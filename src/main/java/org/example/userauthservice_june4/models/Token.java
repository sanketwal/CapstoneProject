package org.example.userauthservice_june4.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Token extends BaseModel {
    private String value;
    private Date expiresAt;

    @ManyToOne
    private User user;
}

// Token --- User => M:1
