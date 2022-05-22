package com.arifrajib.springsecurityjwt.secutiry;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtilityService {
    @Value("${jwt.secret.key}")
    private String jwtSecret;

    public String generateJwtToken (String email) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withIssuer("SpringSecuritySampleApplication")
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public String validateTokenAndRetrieveSubject (String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret))
                .withSubject("User Details")
                .withIssuer("SpringSecuritySampleApplication")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
    }
}
