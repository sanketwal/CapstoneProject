package org.example.userauthservice_june4.repos;

import org.example.userauthservice_june4.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Override
    Token save(Token token);

    //select * from token where value = ?  and expiresAt > now()
    Optional<Token> findByValueAndExpiresAtAfter(String tokenValue, Date currentDate);

}
