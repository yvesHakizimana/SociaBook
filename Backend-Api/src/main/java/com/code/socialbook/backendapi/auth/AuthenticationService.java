package com.code.socialbook.backendapi.auth;

import com.code.socialbook.backendapi.email.EmailService;
import com.code.socialbook.backendapi.email.EmailTemplate;
import com.code.socialbook.backendapi.role.RoleRepository;
import com.code.socialbook.backendapi.security.JwtService;
import com.code.socialbook.backendapi.user.Token;
import com.code.socialbook.backendapi.user.TokenRepository;
import com.code.socialbook.backendapi.user.User;
import com.code.socialbook.backendapi.user.UserRepository;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class AuthenticationService {

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

    public AuthenticationResponse authenticate(AuthenticationRequest authRequest){
        var auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        var claims = new HashMap<String, Object>();
        var user = (User) auth.getPrincipal();
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Transactional
    public void activateAccount(String token){
        //Check if we have the token in the database
        Token savedToken = tokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Invalid token"));
        //What if the token is valid but expired
        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())){
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation Token has expired, A new token has been sent to the same email");
        }
        //Then we have also to check if the token is correct
        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("The user was not found."));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    private void sendValidationEmail(User registeredUser)  {
        var newToken = generateAndSaveActivationToken(registeredUser);
        //Mechanism of sending the email...
        emailService.sendEmail(
                registeredUser.getEmail(),
                registeredUser.fullName(),
                EmailTemplate.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account Activation");
    }

    private String  generateAndSaveActivationToken(User registeredUser) {
        //Generate a token
        String generatedToken = generateActivationCode();
        var token = Token.builder()
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .token(generatedToken)
                .user(registeredUser)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode() {
        String characters = "0123456789";
        StringBuilder activationCode = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for(int i = 0; i < 6; i++){
            int randomIndex = random.nextInt(characters.length());
            activationCode.append(characters.charAt(randomIndex));
        }
        return activationCode.toString();
    }
}
