package com.ums.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ums.entity.AppUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class JWTService {
    @Value("${jwt.algorithm.key}")
    private String algorithmKey;
    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiryDuration}")
    private int expiryTime;

    private Algorithm algorithm;

    @PostConstruct
    public void postConstruct() {
  algorithm = Algorithm.HMAC256(algorithmKey);

    }
private final static String USER_NAME= "name";

    public String generateToken(AppUser user){

        return JWT.create().withClaim(USER_NAME,user.getName())
                .withExpiresAt(new Date(System.currentTimeMillis()+expiryTime))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String getUserName(String token) {

        DecodedJWT decodedjwt = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
       return decodedjwt.getClaim(USER_NAME).asString();
    }

}
