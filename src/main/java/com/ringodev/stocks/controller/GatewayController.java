package com.ringodev.stocks.controller;

import com.ringodev.stocks.data.AlreadyExistsException;
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

    // tries to signup a new user
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody SignUpData data) {
        UserImpl userImpl = new UserImpl(data.getUsername(), passwordEncoder.encode(data.getPassword()), List.of(new AuthorityImpl("USER")), false, true, true, true);
        logger.info("Signing up new User: " + userImpl.toString());

        if (!userService.userExists(userImpl.getUsername())) {
            return createUser(data, userImpl);
        } else {
            logger.warn(userImpl.getUsername() + " already exists and cant be inserted");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<Object> createUser(SignUpData data, UserImpl userImpl) {
        userService.createUser(userImpl);
        try {
            return createUserData(data, userImpl);
        } catch (AlreadyExistsException e) {
            userService.deleteUser(userImpl.getUsername());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<Object> createUserData(SignUpData data, UserImpl userImpl) throws AlreadyExistsException {
        userDataService.createUserData(userImpl, data.email);
        logger.info("ADDED USER" + userImpl.toString());
        mailService.sendVerificationMessage(data.getEmail(), data.getUsername());
        logger.info("Sent Verification Mail to " + data.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
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
            logger.warn(exception.toString());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void parseToken(String token) {
        Jws<Claims> parsedToken = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET.getBytes())
                .parseClaimsJws(token);

        String username = parsedToken
                .getBody()
                .getSubject();

        String mail = parsedToken.getBody().get(username).toString();

        if (!StringUtils.isEmpty(mail)) {
            userService.verifyMail(username);
            logger.info("verified Email " + mail + " for User " + username);
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
