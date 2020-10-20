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
import javax.servlet.http.HttpServletRequest;
import java.util.List;


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
        UserImpl userImpl = new UserImpl(data.getUsername(), passwordEncoder.encode(data.getPassword()), List.of(new AuthorityImpl("USER")),false,true,true,true);
        logger.info("Signing up new User: " + userImpl.toString());
        if (userService.userExists(userImpl.getUsername())) {
            logger.warn(userImpl.getUsername() + " already exists and cant be inserted");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            userService.createUser(userImpl);
            try {
                userDataService.createUserData(userImpl, data.email);
            } catch (AlreadyExistsException e) {
                userService.deleteUser(userImpl.getUsername());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        logger.info("ADDED USER" + userImpl.toString());
        mailService.sendVerificationMessage(data.getEmail());
        logger.info("Sent Verification Mail to " + data.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // tries to verify the Users Email
    @GetMapping("/verify")
    public ResponseEntity<Object> verifyMail(HttpServletRequest request) {
        String token = request.getParameter("token");
        // verify token and get the mail address

        try {
            byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

            Jws<Claims> parsedToken = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token);

            String mail = parsedToken
                    .getBody()
                    .getSubject();

            if (!StringUtils.isEmpty(mail)) {
                userService.verifyMail(userDataService.getUsernameFromEmail(mail));
                logger.info("verified Email " + mail);
            }

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
        }catch (EntityNotFoundException exception){
            logger.warn(exception.toString());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

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
