package org.example.userauthservice_june4.services;

import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice_june4.models.Token;
import org.example.userauthservice_june4.models.User;

public interface IAuthService {
    User signup(String name, String email,
                String password,
                String phoneNumber);

    Token login(String email, String password);

    void logout(String token);

    User validateToken(String token);
}
