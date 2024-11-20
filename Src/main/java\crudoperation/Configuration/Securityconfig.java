package com.ums.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {

    private JWTResponseFilter jwtResponseFilter;

    public SecurityConfig(JWTResponseFilter jwtResponseFilter) {
        this.jwtResponseFilter = jwtResponseFilter;
    }

    @Bean
   public SecurityFilterChain securityFilterchain(HttpSecurity http) throws Exception{
      http.csrf().disable().cors().disable();
      http.addFilterBefore(jwtResponseFilter, AuthorizationFilter.class);
       http.authorizeHttpRequests().anyRequest().permitAll();
//               requestMatchers("/api/v1/countries/addcountry").hasRole("ADMIN").
//               requestMatchers("/api/v1/auth/profile").hasAnyRole("ADMIN","USER")

       return http.build();
   }
}
