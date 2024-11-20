package com.ums.service;

import com.ums.entity.AppUser;
import com.ums.payload.LoginDto;
import com.ums.payload.UserDto;
import com.ums.repository.AppUserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private JWTService jwtservice;

    private AppUserRepository userRepository;

    public UserServiceImpl(JWTService jwtservice, AppUserRepository userRepository) {
        this.jwtservice = jwtservice;

        this.userRepository = userRepository;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        AppUser user = mapToEntity(userDto);


        AppUser savedUser = userRepository.save(user);
        UserDto dto = mapToDto(savedUser);
        return dto;
    }

    @Override
    public String verifyLogin(LoginDto loginDto) {
        Optional<AppUser> opUser = userRepository.findByUsername(loginDto.getUsername());
        if(opUser.isPresent()){
            AppUser user = opUser.get();
            if(BCrypt.checkpw(loginDto.getPassword(),user.getPassword())){
               return jwtservice.generateToken(user);
            }
        }
        return null;
    }

    UserDto mapToDto(AppUser user){
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        dto.setUserRole(user.getUserRole());
        dto.setEmailId(user.getEmailId());
        return dto;
    }

    AppUser mapToEntity(UserDto userDto){
        AppUser user = new AppUser();
        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        user.setEmailId(userDto.getEmailId());
        user.setUserRole("ROLE_USER");
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        return user;
    }
}
