package org.example.userauthservice_june4.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendEmailEventDto {
    private String email;
    private String body;
    private String subject;
}
