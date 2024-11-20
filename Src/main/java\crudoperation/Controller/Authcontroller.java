package com.ums.controller;

import com.ums.entity.AppUser;
import com.ums.payload.JwtResponse;
import com.ums.payload.LoginDto;
import com.ums.payload.UserDto;
import com.ums.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("http://localhost:4200")
public class  AuthController {
    private UserService userservice;

    public AuthController(UserService userservice) {
        this.userservice = userservice;
    }

    @PostMapping("/{addUser}")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDto userDto,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        UserDto dto = userservice.addUser(userDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?>login(@RequestBody LoginDto loginDto) {
       String token= userservice.verifyLogin(loginDto);
       if(token!= null){
           JwtResponse response = new JwtResponse();
              response.setToken(token);
           return new ResponseEntity<>(response,HttpStatus.OK);
       }
        return new ResponseEntity<>("Invalid credentials",HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/profile")
    public AppUser getcurrentprofile(@AuthenticationPrincipal AppUser user){

        return user;
    }
}
