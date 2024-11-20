package com.ums.config;

import com.ums.entity.AppUser;
import com.ums.repository.AppUserRepository;
import com.ums.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Component
public class JWTResponseFilter extends OncePerRequestFilter {
    private JWTService jwtService;

    private AppUserRepository userRepository;

    public JWTResponseFilter(JWTService jwtService, AppUserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      String tokenHeader = request.getHeader("Authorization");
       if(tokenHeader!=null && tokenHeader.startsWith("Bearer ")){
         String token =  tokenHeader.substring(8,tokenHeader.length()-1);
        String username =   jwtService.getUserName(token);
           Optional<AppUser> opuser = userRepository.findByUsername(username);
           if(opuser.isPresent()){
               AppUser appuser = opuser.get();
               UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(appuser,null, Collections.singleton(new SimpleGrantedAuthority(appuser.getUserRole())));
               authentication.setDetails(new WebAuthenticationDetails(request));
               SecurityContextHolder.getContext().setAuthentication(authentication);
           }
       }

        filterChain.doFilter(request,response);
    }
}
