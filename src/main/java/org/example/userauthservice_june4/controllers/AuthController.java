package org.example.userauthservice_june4.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.userauthservice_june4.dtos.LoginRequestDto;
import org.example.userauthservice_june4.dtos.LogoutRequestDto;
import org.example.userauthservice_june4.dtos.SignupRequestDto;
import org.example.userauthservice_june4.dtos.UserDto;
import org.example.userauthservice_june4.models.Token;
import org.example.userauthservice_june4.models.User;
import org.example.userauthservice_june4.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private IAuthService authService;

    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignupRequestDto signupRequestDto) throws JsonProcessingException {
       User user = authService.signup(
               signupRequestDto.getName(),
               signupRequestDto.getEmail(),
               signupRequestDto.getPassword(),
               signupRequestDto.getPhoneNumber()
       );
       return from(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
      Token token = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
      return new ResponseEntity<>(token.getValue(), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto request) {
        // Call the logout method in the service layer to invalidate the token
        authService.logout(request.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/validate")
    public UserDto validateToken(@RequestHeader("Authorization") String token) {
        // Remove "Bearer " prefix if present
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        User user = authService.validateToken(token);
        return from(user);
    }

    private UserDto from(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}