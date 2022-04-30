package com.codewithroy.resttester.expense.security;

import com.codewithroy.resttester.expense.web.exception.ExpenseException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

import static io.jsonwebtoken.Jwts.parser;


@Service
public class JwtProvider {
    
    private KeyStore keyStore;
    
    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream security = getClass().getResourceAsStream("/keystore.jks");
            keyStore.load(security, "password".toCharArray());
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            throw new ExpenseException("Exception occurred while loading keystore" + e);
        }
    }
    
    public String generateToken(Authentication authentication) {
        User principle = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principle.getUsername())
                .signWith(getCustomPrivateKey())
                .compact();
                
    }

    private PrivateKey getCustomPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("roys", "password".toCharArray());
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
            throw new ExpenseException("Exception occurred while getting key" + e);
        }
    }

    public boolean validateToken(String jwt) {
        parseToken(jwt);
        return true;
    }

    private PublicKey getPublicKey() {
       try {
           return keyStore.getCertificate("roys").getPublicKey();
       } catch (KeyStoreException e) {
           throw new ExpenseException("Exception while fetching public key from keystore " + e);
       }
    }

    public String getUserNameFromToken(String token) {
        return parseToken(token).getBody().getSubject();
    }

    private Jws<Claims> parseToken(String token) {
        return parser().setSigningKey(getPublicKey()).parseClaimsJws(token);
    }
}
