package org.example.schoolmanagementsystemspring.authentication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.schoolmanagementsystemspring.authentication.dto.AuthenticationRequestDto;
import org.example.schoolmanagementsystemspring.authentication.dto.AuthenticationResponse;
import org.example.schoolmanagementsystemspring.authentication.entity.Token;
import org.example.schoolmanagementsystemspring.authentication.entity.TokenType;
import org.example.schoolmanagementsystemspring.authentication.exception.InvalidTokenException;
import org.example.schoolmanagementsystemspring.authentication.exception.TokenNotFoundException;
import org.example.schoolmanagementsystemspring.authentication.mapper.TokenAuthMapper;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Predicate;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * The AuthenticationServiceImpl class is a service that implements the AuthenticationService interface.
 * It provides methods for user authentication, token refresh, account activation, and activation code generation.
 * Each method throws specific exceptions that are handled by the controller.
 * It uses the UserRepository, TokenRepository, AuthenticationManager, JwtService, TokenAuthMapper, and EmailService to perform its operations.
 *
 * @author FFreitas
 * <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">LinkedIn</a>
 * <a href="https://github.com/FFreitas997/">Github</a>
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
    private final AuthenticationManager manager;
    private final JwtService jwtService;
    private final TokenAuthMapper tokenMapper;
    private final EmailService emailService;


    /**
     * The authenticate method authenticates a user with the details provided in the request body.
     * The request body must be a valid AuthenticationRequestDto object.
     * If the user is not found, it throws a UserNotFoundException.
     *
     * @param requestBody a valid AuthenticationRequestDto object containing the details of the user to be authenticated.
     * @return an AuthenticationResponse object containing the authentication details.
     * @throws UserNotFoundException if the user is not found.
     */
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequestDto requestBody) throws UserNotFoundException {
        User user = userRepository
                .findByEmailValid(requestBody.email())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + requestBody.email()));
        String generatedToken = jwtService.generateToken(user, Collections.emptyMap());
        String generatedRefreshToken = jwtService.generateRefreshToken(user);
        expireAllUserTokens(user);
        Token tokenMapped = tokenMapper.apply(generatedToken, user);
        tokenRepository.save(tokenMapped);
        manager.authenticate(new UsernamePasswordAuthenticationToken(requestBody.email(), requestBody.password()));
        return AuthenticationResponse.builder()
                .accessToken(generatedToken)
                .refreshToken(generatedRefreshToken)
                .build();
    }

    /**
     * The refreshToken method refreshes the token of the user making the request.
     * It does not require any request body.
     *
     * @param req the HttpServletRequest object containing the details of the request.
     * @param res the HttpServletResponse object for sending the response.
     * @throws IOException if an input or output exception occurred.
     */
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
                expireAllUserTokens(user);
                Token token = tokenMapper.apply(accessToken, user);
                tokenRepository.save(token);
                AuthenticationResponse authResponse = new AuthenticationResponse(accessToken, refreshToken.getValue());
                new ObjectMapper().writeValue(res.getOutputStream(), authResponse);
            }
        }
    }

    /**
     * The confirmAccount method activates the account of the user with the provided activation code.
     * If the user is not found, it throws a UserNotFoundException.
     * If the token is invalid, it throws an InvalidTokenException.
     * If the token is not found, it throws a TokenNotFoundException.
     *
     * @param token the activation code of the user.
     * @throws UserNotFoundException  if the user is not found.
     * @throws InvalidTokenException  if the token is invalid.
     * @throws TokenNotFoundException if the token is not found.
     */
    @Override
    public void confirmAccount(String token) throws TokenNotFoundException, InvalidTokenException, UserNotFoundException {
        Token activationCode = tokenRepository
                .findByValueAndType(token, TokenType.ACTIVATION_CODE)
                .orElseThrow(() -> new TokenNotFoundException("Activation Code not found"));

        if (activationCode.isExpired()) {
            log.error("Activation Code {} is not valid", token);
            throw new InvalidTokenException("Activation Code is not valid");
        }

        if (!isConfirmCodeNonExpired().test(activationCode)) {
            activationCode.setExpired(true);
            tokenRepository.save(activationCode);
            log.error("Activation Code {} is not valid", token);
            throw new InvalidTokenException("Activation Code is not valid");
        }

        User user = userRepository
                .findById(activationCode.getUser() == null ? -1 : activationCode.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);

        activationCode.setExpired(true);
        tokenRepository.save(activationCode);
    }

    /**
     * The generateActivationCode method generates an activation code for the user with the provided email.
     * If the user is not found, it throws a UserNotFoundException.
     *
     * @param email the email of the user.
     * @throws UserNotFoundException if the user is not found.
     */
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

    /**
     * This method expires all valid tokens of a user.
     * It first checks if the user is null, if so, it returns immediately.
     * Otherwise, it retrieves all valid tokens of the user from the token repository and sets them as expired.
     *
     * @param user the user whose tokens are to be expired.
     */
    private void expireAllUserTokens(User user) {
        if (user == null) return;
        tokenRepository
                .findValidTokensByUserId(user.getId())
                .forEach(token -> token.setExpired(true));
    }

    /**
     * This method builds a new Token object with the provided user and code.
     * The token is set as an activation code and is not expired.
     *
     * @param user the user to whom the token belongs.
     * @param code the value of the token.
     * @return a new Token object.
     */
    private Token buildCode(User user, String code) {
        return Token
                .builder()
                .value(code)
                .type(TokenType.ACTIVATION_CODE)
                .expired(false)
                .user(user)
                .build();
    }

    /**
     * This method handles the result of sending an email asynchronously.
     * If an exception occurred during the sending of the email, it returns a string indicating an error.
     * Otherwise, it returns a string indicating success.
     *
     * @param userSaved the user to whom the email was sent.
     * @param exception the exception that occurred during the sending of the email, if any.
     * @return a string indicating the result of the operation.
     */
    private String handleSendEmailAsync(User userSaved, Throwable exception) {
        return exception != null ?
                "An error occurred while sending the email to " + userSaved.getEmail() :
                "The email was sent successfully to " + userSaved.getEmail();
    }

    /**
     * This method generates a random numeric code of a specified length.
     * It uses a SecureRandom object to generate the random numbers.
     *
     * @param length the length of the code to generate.
     * @return a string representing the generated code.
     */
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

    /**
     * This method builds an Email object for confirming a user's email address.
     * It sets the from address, to address, subject, and template of the email.
     * It also sets the template properties and resources.
     *
     * @param code the activation code to include in the email.
     * @param user the user to whom the email will be sent.
     * @return a new Email object.
     */
    private Email buildConfirmEmail(String code, User user) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("name", user.getRole().name() + " " + user.getFirstName() + " " + user.getLastName());
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

    /**
     * This method checks if a confirmation code is not expired.
     * It compares the creation time of the code plus the code expiration time with the current time.
     *
     * @return a Predicate that tests if a Token is not expired.
     */
    private Predicate<Token> isConfirmCodeNonExpired() {
        return param -> param.getCreatedAt()
                .plusMinutes(codeExpiration)
                .isAfter(LocalDateTime.now());
    }
}
