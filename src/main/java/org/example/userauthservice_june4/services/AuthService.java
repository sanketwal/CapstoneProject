package org.example.userauthservice_june4.services;

import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice_june4.exceptions.PasswordMismatchException;
import org.example.userauthservice_june4.exceptions.UserAlreadyExistException;
import org.example.userauthservice_june4.exceptions.UserNotSignedException;
import org.example.userauthservice_june4.models.User;
import org.example.userauthservice_june4.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepo userRepo;

    public User signup(String name, String email,
                       String password,
                       String phoneNumber) {

       Optional<User> userOptional = userRepo.findByEmailEquals(email);
       if(userOptional.isPresent()) {
         throw new UserAlreadyExistException("Please try login directly !!!");
       }

       User user = new User();
       user.setEmail(email);
       user.setPassword(password); // ToDo : Use Bcrypt here
       user.setName(name);
       user.setPhoneNumber(phoneNumber);
       return userRepo.save(user);
    }

    public Pair<User,String> login(String email,String password) {
        Optional<User> userOptional = userRepo.findByEmailEquals(email);
        if(userOptional.isEmpty()) {
           throw new UserNotSignedException("Please try signup first");
        }

        String storedPassword = userOptional.get().getPassword();

        if(!password.equals(storedPassword)) {
            throw new PasswordMismatchException("Please type correct password");
        }

        //ToDo : Generate JWT

        return new Pair<>(userOptional.get(),"token");
    }
}
