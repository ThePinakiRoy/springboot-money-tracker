package com.codewithroy.resttester.expense.security;

import com.codewithroy.resttester.expense.web.exception.ExpenseException;
import com.codewithroy.resttester.expense.model.Person;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;


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
        Person principle = (Person) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principle.getUserName())
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
}
