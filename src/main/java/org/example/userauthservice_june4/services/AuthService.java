package org.example.userauthservice_june4.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.antlr.v4.runtime.misc.Pair;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.userauthservice_june4.dtos.SendEmailEventDto;
import org.example.userauthservice_june4.exceptions.InvalidTokenException;
import org.example.userauthservice_june4.exceptions.PasswordMismatchException;
import org.example.userauthservice_june4.exceptions.UserAlreadyExistException;
import org.example.userauthservice_june4.exceptions.UserNotSignedException;
import org.example.userauthservice_june4.models.Token;
import org.example.userauthservice_june4.models.User;
import org.example.userauthservice_june4.repos.TokenRepository;
import org.example.userauthservice_june4.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {
    private UserRepo userRepo;
    private PasswordEncoder bCryptPasswordEncoder;
    private TokenRepository tokenRepository;
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public AuthService(UserRepo userRepository,
                        TokenRepository tokenRepository,
                        BCryptPasswordEncoder bCryptPasswordEncoder,
                        KafkaTemplate kafkaTemplate,
                        ObjectMapper objectMapper)
        {
            this.userRepo = userRepository;
            this.tokenRepository = tokenRepository;
            this.bCryptPasswordEncoder = bCryptPasswordEncoder;
            this.kafkaTemplate = kafkaTemplate;
            this.objectMapper = objectMapper;
        }

    public User signup(String name, String email,
                       String password,
                       String phoneNumber) throws JsonProcessingException {

       Optional<User> userOptional = userRepo.findByEmailEquals(email);
       if(userOptional.isPresent()) {
         throw new UserAlreadyExistException("Please try login directly !!!");
       }

       User user = new User();
       user.setEmail(email);
       user.setPassword(bCryptPasswordEncoder.encode(password));
       user.setName(name);
       user.setPhoneNumber(phoneNumber);

        SendEmailEventDto sendEmailEventDto = new SendEmailEventDto();
        sendEmailEventDto.setEmail(email);
        sendEmailEventDto.setSubject("Welcome to User Auth Service");
        sendEmailEventDto.setBody("Welcome to User Auth Service");

//       kafkaTemplate.send("sendEmailEvent", objectMapper.writeValueAsString(sendEmailEventDto));
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
        Token token = getToken(userOptional.get());

        return tokenRepository.save(token);
    }

    @Override
    public void logout(String token) {
        // Find the token in the database where it has not been deleted
        Optional<Token> token1 = tokenRepository.findByValueAndExpiresAtAfter(token, new Date());

        // If the token does not exist or is already deleted, do nothing or throw an exception
        if (token1.isEmpty()) {
            return;
        }

        // Mark the token as deleted
        Token tkn = token1.get();
        tkn.setExpiresAt(new Date());

        // Save the updated token back to the repository
        tokenRepository.save(tkn);
    }

    @Override
    public User validateToken(String tokenValue) {

        Optional<Token> optionalToken = tokenRepository.
                findByValueAndExpiresAtAfter(tokenValue, new Date());

        //Token is not valid or expired
        return optionalToken.map(Token::getUser).orElse(null);

    }

    private static Token getToken(User user) {
        // Set the token expiration date to 30 days from the current date
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plus(30, ChronoUnit.DAYS);

        // Convert LocalDate to Date
        Date expiryDate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Create a new Token object
        Token token = new Token();
        token.setUser(user);
        token.setExpiresAt(expiryDate);

        // Generate a random alphanumeric token value
        token.setValue(RandomStringUtils.randomAlphanumeric(128));

        return token;
    }
}
