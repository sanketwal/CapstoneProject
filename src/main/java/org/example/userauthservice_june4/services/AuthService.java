package org.example.userauthservice_june4.services;

import org.antlr.v4.runtime.misc.Pair;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.userauthservice_june4.exceptions.InvalidTokenException;
import org.example.userauthservice_june4.exceptions.PasswordMismatchException;
import org.example.userauthservice_june4.exceptions.UserAlreadyExistException;
import org.example.userauthservice_june4.exceptions.UserNotSignedException;
import org.example.userauthservice_june4.models.Token;
import org.example.userauthservice_june4.models.User;
import org.example.userauthservice_june4.repos.TokenRepository;
import org.example.userauthservice_june4.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private TokenRepository tokenRepository;

    public User signup(String name, String email,
                       String password,
                       String phoneNumber) {

       Optional<User> userOptional = userRepo.findByEmailEquals(email);
       if(userOptional.isPresent()) {
         throw new UserAlreadyExistException("Please try login directly !!!");
       }

       User user = new User();
       user.setEmail(email);
       user.setPassword(bCryptPasswordEncoder.encode(password));
       user.setName(name);
       user.setPhoneNumber(phoneNumber);
       return userRepo.save(user);
    }

    public Token login(String email,String password) {
        Optional<User> userOptional = userRepo.findByEmailEquals(email);
        if(userOptional.isEmpty()) {
           throw new UserNotSignedException("Please try signup first");
        }

        if(!bCryptPasswordEncoder.matches(password, userOptional.get().getPassword())) {
            throw new PasswordMismatchException("Please type correct password");
        }

        //Create a token and store it in Tokens table.
        Token token = new Token();
        token.setUser(userOptional.get());
        token.setValue(RandomStringUtils.randomAlphanumeric(128));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date dateAfter30Days = calendar.getTime();

        token.setExpiresAt(dateAfter30Days);

        return tokenRepository.save(token);
    }

    @Override
    public User validateToken(String tokenValue) {

        Optional<Token> optionalToken = tokenRepository.
                findByValueAndExpiresAtAfter(tokenValue, new Date());

        if (optionalToken.isEmpty()) {
            //Token is not valid or expired
            return null;
        }

        return optionalToken.get().getUser();
    }
}
