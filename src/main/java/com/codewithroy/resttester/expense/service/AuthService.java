package com.codewithroy.resttester.expense.service;
import com.codewithroy.resttester.expense.constants.ExpenseConstants;
import com.codewithroy.resttester.expense.util.ConfigUtility;
import com.codewithroy.resttester.expense.web.dto.AuthRequest;
import com.codewithroy.resttester.expense.web.dto.AuthResponse;
import com.codewithroy.resttester.expense.web.exception.ExpenseException;
import com.codewithroy.resttester.expense.model.EmailNotification;
import com.codewithroy.resttester.expense.model.VerificationToken;
import com.codewithroy.resttester.expense.repo.PersonRepository;
import com.codewithroy.resttester.expense.repo.VerificationTokenRepository;
import com.codewithroy.resttester.expense.model.Person;
import com.codewithroy.resttester.expense.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final ConfigUtility configUtility;


    @Transactional
    public void register(AuthRequest authRequest) {
        Person person = new Person();
        person.setName(authRequest.getName());
        person.setUserName(authRequest.getUsername());
        person.setEmail(authRequest.getEmail());
        person.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        person.setEnabled(false);
        personRepository.save(person);

        String token = generateAuthToken(person);
        log.info("token is " + token);
        mailService.sendMail(new EmailNotification(person.getEmail(), ExpenseConstants.ACTIVATION_MAIL_SUBJECT_LINE,
                "Hello "+ person.getName() +",\n"
                        +"Thank you for signing up to expense tracker,\n"+
                "Please click on the below link to activate your account.\n"+
                        configUtility.getProperty(ExpenseConstants.BACK_DOMAIN_URL) +
                        ExpenseConstants.ACTIVATION_MAIL_URL_PATTERN + token));
    }

    private String generateAuthToken(Person person) {
        String authToken = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(authToken);
        verificationToken.setPerson(person);

        verificationTokenRepository.save(verificationToken);
        return authToken;
    }

    public boolean verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new ExpenseException("Invalid Token provided!!"));
        fetchPersonAndEnable(verificationToken.get());
        return true;
    }
    @Transactional
    private void fetchPersonAndEnable(VerificationToken verificationToken) {
        String userName = verificationToken.getPerson().getUserName();
        Person person = personRepository.findByUserName(userName).orElseThrow(() -> new ExpenseException("Person not found!! " + userName));
        person.setEnabled(true);
        personRepository.save(person);
    }

    public AuthResponse login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        return new AuthResponse(token, authRequest.getUsername());
    }
}
