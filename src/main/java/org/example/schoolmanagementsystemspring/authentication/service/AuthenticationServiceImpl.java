package org.example.schoolmanagementsystemspring.authentication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.authentication.dto.AuthenticationRequestDto;
import org.example.schoolmanagementsystemspring.authentication.dto.AuthenticationResponse;
import org.example.schoolmanagementsystemspring.authentication.dto.RequestRegisterDTO;
import org.example.schoolmanagementsystemspring.authentication.entity.Token;
import org.example.schoolmanagementsystemspring.authentication.entity.TokenType;
import org.example.schoolmanagementsystemspring.authentication.exception.InvalidTokenException;
import org.example.schoolmanagementsystemspring.authentication.exception.TokenNotFoundException;
import org.example.schoolmanagementsystemspring.authentication.exception.UserAlreadyExistsException;
import org.example.schoolmanagementsystemspring.authentication.mapper.TokenAuthMapper;
import org.example.schoolmanagementsystemspring.authentication.mapper.UserAuthMapper;
import org.example.schoolmanagementsystemspring.authentication.repository.TokenRepository;
import org.example.schoolmanagementsystemspring.mail.Email;
import org.example.schoolmanagementsystemspring.mail.EmailService;
import org.example.schoolmanagementsystemspring.mail.EmailTemplate;
import org.example.schoolmanagementsystemspring.user.entity.User;
import org.example.schoolmanagementsystemspring.user.exception.UserNotFoundException;
import org.example.schoolmanagementsystemspring.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${email.confirmation.frontend-url}")
    private String confirmationURL;
    @Value("${email.from}")
    private String fromEmail;
    @Value("${email.subject}")
    private String subject;
    @Value("${email.code.length}")
    private Integer length;
    @Value("${email.code.expiration}")
    private Integer codeExpiration;

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager manager;
    private final JwtService jwtService;
    private final UserAuthMapper userMapper;
    private final TokenAuthMapper tokenMapper;
    private final EmailService emailService;


    @Override
    public void register(RequestRegisterDTO requestBody) throws UserAlreadyExistsException {
        User userMapped = userMapper.apply(requestBody, encoder);
        var checkUser = userRepository
                .findByEmail(requestBody.email());
        if(checkUser.isPresent())
            throw new UserAlreadyExistsException("User already exists");

        User userSaved = userRepository.save(userMapped);

        String code = generateCode(length);

        Token token = buildCode(userSaved, code);
        tokenRepository.save(token);

        emailService
                .sendEmail(buildConfirmEmail(code, userSaved))
                .handleAsync((res, ex) -> handleSendEmailAsync(userSaved, ex))
                .thenAccept(log::info);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequestDto requestBody) throws UserNotFoundException {
        User user = userRepository
                .findByEmailValid(requestBody.email())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + requestBody.email()));
        String generatedToken = jwtService.generateToken(user, Collections.emptyMap());
        String generatedRefreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        Token tokenMapped = tokenMapper.apply(generatedToken, user);
        tokenRepository.save(tokenMapped);
        manager.authenticate(new UsernamePasswordAuthenticationToken(requestBody.email(), requestBody.password()));
        return new AuthenticationResponse(generatedToken, generatedRefreshToken);
    }

    @Override
    public void refreshToken(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String authHeader = req.getHeader(AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        Token refreshToken = new Token();
        refreshToken.setValue(authHeader.substring(7));
        String username = jwtService.getSubject(refreshToken.getValue());
        if (username != null) {
            User user = userRepository
                    .findByEmailValid(username)
                    .orElse(null);
            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user, Collections.emptyMap());
                revokeAllUserTokens(user);
                Token token = tokenMapper.apply(accessToken, user);
                tokenRepository.save(token);
                AuthenticationResponse authResponse = new AuthenticationResponse(accessToken, refreshToken.getValue());
                new ObjectMapper().writeValue(res.getOutputStream(), authResponse);
            }
        }
    }

    @Override
    public void confirmAccount(String token) throws TokenNotFoundException, InvalidTokenException, UserNotFoundException {
        Token activationCode = tokenRepository
                .findByValueAndType(token, TokenType.ACTIVATION_CODE)
                .orElseThrow(() -> new TokenNotFoundException("Activation Code not found"));

        if (activationCode.isRevoked() || activationCode.isExpired()) {
            log.error("Activation Code {} is not valid", token);
            throw new InvalidTokenException("Activation Code is not valid");
        }

        Predicate<Token> isCodeValid = param ->
                param.getCreatedAt()
                        .plusMinutes(codeExpiration)
                        .isAfter(LocalDateTime.now());

        if (!isCodeValid.test(activationCode)) {
            activationCode.setExpired(true);
            activationCode.setRevoked(true);
            tokenRepository.save(activationCode);
            log.error("Activation Code {} is not valid", token);
            throw new InvalidTokenException("Activation Code is not valid");
        }
        User user = userRepository
                .findById(activationCode.getUser() == null ? -1 : activationCode.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setEnabled(true);
        user.setNotification("Your account has been activated successfully");
        userRepository.save(user);
        activationCode.setRevoked(true);
        activationCode.setExpired(true);
        tokenRepository.save(activationCode);
    }

    @Override
    public void generateActivationCode(String email) throws UserNotFoundException {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        String code = generateCode(length);
        Token token = buildCode(user, code);
        tokenRepository.save(token);
        emailService
                .sendEmail(buildConfirmEmail(code, user))
                .handleAsync((res, ex) -> handleSendEmailAsync(user, ex))
                .thenAccept(log::info);
    }

    private void revokeAllUserTokens(User user) {
        if (user == null) return;
        Consumer<Token> revokeToken = token -> {
            token.setRevoked(true);
            token.setExpired(true);
        };
        tokenRepository
                .findAllValidTokensByUserId(user.getId())
                .forEach(revokeToken);
    }

    private Token buildCode(User user, String code) {
        return Token
                .builder()
                .value(code)
                .type(TokenType.ACTIVATION_CODE)
                .expired(false)
                .revoked(false)
                .user(user)
                .build();
    }

    private String handleSendEmailAsync(User userSaved, Throwable exception) {
        return exception != null ?
                "An error occurred while sending the email to " + userSaved.getEmail() :
                "The email was sent successfully to " + userSaved.getEmail();
    }

    private String generateCode(int length) {
        String characters = "0123456789";
        StringBuilder code = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            code.append(characters.charAt(randomIndex));
        }
        return code.toString();
    }

    private Email buildConfirmEmail(String code, User user) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("name", user.fullName());
        properties.put("confirmationURL", confirmationURL);
        properties.put("activationCode", code);

        HashMap<String, Resource> resources = new HashMap<>();
        resources.put("image", new ClassPathResource("./static/images/logo.png"));

        return Email
                .builder()
                .from(fromEmail)
                .to(user.getEmail())
                .subject(subject)
                .template(EmailTemplate.CONFIRM_EMAIL)
                .templateProperties(properties)
                .resources(resources)
                .build();
    }
}
