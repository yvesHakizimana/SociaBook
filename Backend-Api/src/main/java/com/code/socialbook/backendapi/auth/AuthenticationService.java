package com.code.socialbook.backendapi.auth;

import com.code.socialbook.backendapi.email.EmailService;
import com.code.socialbook.backendapi.email.EmailTemplate;
import com.code.socialbook.backendapi.exceptions.InvalidTokenException;
import com.code.socialbook.backendapi.exceptions.TokenInvalidException;
import com.code.socialbook.backendapi.role.RoleRepository;
import com.code.socialbook.backendapi.security.JwtService;
import com.code.socialbook.backendapi.user.Token;
import com.code.socialbook.backendapi.user.TokenRepository;
import com.code.socialbook.backendapi.user.User;
import com.code.socialbook.backendapi.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

//@Service
@RequiredArgsConstructor
public class AuthenticationService {

    /*private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @Value("${application.mailing.fronted.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) {
        logger.debug("Registering user: {}", request.getEmail());
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Role USER was not initialized."));
        var registeredUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .roles(List.of(userRole))
                .enabled(false)
                .build();
        userRepository.save(registeredUser);
        sendValidationEmail(registeredUser);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authRequest) {
        logger.debug("Authenticating user: {}", authRequest.getEmail());
        var auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        var claims = new HashMap<String, Object>();
        var user = (User) auth.getPrincipal();
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Transactional
    public void activateAccount(String token) {
        logger.debug("Activating account with token: {}", token);
        Token savedToken = tokenRepository.findByToken(token).orElseThrow(() -> new InvalidTokenException("Token not found"));
        logger.debug("Found token: {}", savedToken);

        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            logger.debug("Token expired, generating new token");
            sendValidationEmail(savedToken.getUser());
            throw new TokenInvalidException("Token expired");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("The user was not found."));
        logger.debug("User found: {}", user);

        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
        logger.debug("Account activated and token validated");
    }


    protected void sendValidationEmail(User registeredUser) {
        var newToken = generateAndSaveActivationToken(registeredUser);
        logger.debug("Sending validation email to user: {}, token: {}", registeredUser.getEmail(), newToken);
        emailService.sendEmail(
                registeredUser.getEmail(),
                registeredUser.fullName(),
                EmailTemplate.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account Activation");
    }


    protected String generateAndSaveActivationToken(User registeredUser) {
        String generatedToken = generateActivationCode();
        logger.debug("Generated new activation token: {} for user: {}", generatedToken, registeredUser.getEmail());
        var token = Token.builder()
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .token(generatedToken)
                .user(registeredUser)
                .build();
        logger.debug("I am going to save the token: {}", token);
        tokenRepository.save(token);
        logger.debug("Token saved successfully: {}", token);
        return generatedToken;
    }

    private String generateActivationCode() {
        String characters = "0123456789";
        StringBuilder activationCode = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(characters.length());
            activationCode.append(characters.charAt(randomIndex));
        }
        return activationCode.toString();
    }*/
}
