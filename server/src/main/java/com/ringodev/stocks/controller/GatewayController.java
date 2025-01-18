package com.ringodev.stocks.controller;

import com.ringodev.stocks.data.AlreadyExistsException;
import com.ringodev.stocks.data.EmailAlreadyExistsException;
import com.ringodev.stocks.data.UsernameAlreadyExistsException;
import com.ringodev.stocks.service.auth.security.SecurityConstants;
import com.ringodev.stocks.service.mail.MailService;
import com.ringodev.stocks.service.user.AuthorityImpl;
import com.ringodev.stocks.service.user.UserDetailsManagerImpl;
import com.ringodev.stocks.service.user.UserImpl;
import com.ringodev.stocks.service.userdata.UserDataService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

import static com.ringodev.stocks.service.auth.security.SecurityConstants.JWT_COOKIE_NAME;


@RestController
@RequestMapping("api")
public class GatewayController {

    private final Logger logger = LoggerFactory.getLogger(GatewayController.class);
    private final UserDetailsManagerImpl userService;
    private final UserDataService userDataService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    GatewayController(PasswordEncoder passwordEncoder, MailService mailService, UserDetailsManagerImpl userService, UserDataService userDataService) {
        this.userDataService = userDataService;
        this.userService = userService;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @param data the {@link SignUpData} object containing the Users credentials.
     * @return StatusCode 405 if the username is already in use, Status Code 409 if the email is already in use
     */
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody SignUpData data) throws AlreadyExistsException {
        UserImpl userImpl = new UserImpl(data.getUsername(), passwordEncoder.encode(data.getPassword()), List.of(new AuthorityImpl("USER")), false, true, true, true);
        logger.info("Signing up new User: " + userImpl.toString());
        try {
            createUserWithUserData(data.getEmail(), userImpl);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UsernameAlreadyExistsException e) {
            logger.warn(userImpl.getUsername() + " already exists and cant be inserted");
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        } catch (EmailAlreadyExistsException e) {
            logger.warn("UserData with email " + data.getEmail() + " already exists and cant be inserted");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    private void createUserWithUserData(String email, UserImpl userImpl) throws AlreadyExistsException {
        if (userService.userExists(userImpl.getUsername())) {
            throw new UsernameAlreadyExistsException("User " + userImpl.getUsername() + " already exists and can't be inserted");
        }

        userService.createUser(userImpl);
        if (userDataService.UserDataExistsWithUsername(userImpl.getUsername())) {
            userDataService.deleteUserData(userImpl.getUsername());
        }
        try {
            createUserDataSendVerification(email, userImpl);
        } catch (EmailAlreadyExistsException e) {
            userService.deleteUser(userImpl.getUsername());
            throw new EmailAlreadyExistsException(e.getMessage());
        }
    }


    private void createUserDataSendVerification(String email, UserImpl userImpl) throws AlreadyExistsException {

        if (userDataService.UserDataExistsWithEmail(email)) {
            throw new EmailAlreadyExistsException("User with Email exists already");
        }
        userDataService.createUserData(userImpl);
        logger.info("ADDED USER" + userImpl.toString());
        mailService.sendVerificationMessage(email, userImpl.getUsername());
        logger.info("Sent Verification Mail to " + email);
    }

    static class TokenContainer {
        private String token;

        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
    }

    // tries to verify the Users Email
    @PostMapping("/verify")
    public ResponseEntity<Object> verifyMail(@RequestBody TokenContainer container) {
        String token = container.getToken();
        try {
            parseToken(token);
        } catch (ExpiredJwtException exception) {
            logger.warn("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } catch (UnsupportedJwtException exception) {
            logger.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } catch (MalformedJwtException exception) {
            logger.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } catch (SignatureException exception) {
            logger.warn("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } catch (IllegalArgumentException exception) {
            logger.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } catch (EntityNotFoundException exception) {
            logger.warn(Arrays.toString(exception.getStackTrace()));
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void parseToken(String token) throws EntityNotFoundException{
        Jws<Claims> parsedToken = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET.getBytes())
                .parseClaimsJws(token);

        String username = parsedToken.getBody().get("username").toString();
        String email = parsedToken.getBody().get("email").toString();

        if (!StringUtils.isEmpty(email) && !StringUtils.isEmpty(username)) {
            userService.verifyUser(username);
            userDataService.setVerifiedEmail(username,email);
            logger.info("verified Email " + email + " for User " + username);
        }
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        String token = "";
        try {
            token = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(JWT_COOKIE_NAME)).findFirst().orElseThrow(() -> new Exception("No cookie was found")).getValue();
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return;
        }
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, token);
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
        response.setStatus(200);
    }

    // todo
    @PostMapping("/createGuest")
    public ResponseEntity<Object> createGuestAccount(HttpServletRequest request) {
        UserImpl guest = userService.createGuest();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    static class SignUpData {
        String username;
        String password;
        String email;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }


}
