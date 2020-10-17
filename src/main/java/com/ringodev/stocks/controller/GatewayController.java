package com.ringodev.stocks.controller;

import com.ringodev.stocks.data.AlreadyExistsException;
import com.ringodev.stocks.service.auth.security.SecurityConstants;
import com.ringodev.stocks.service.user.UserDetailsManagerImpl;
import com.ringodev.stocks.service.userdata.UserDataService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;


@RestController
@RequestMapping("api")
public class GatewayController {

    private final Logger logger = LoggerFactory.getLogger(GatewayController.class);
    private final UserDetailsManagerImpl userService;
    private final UserDataService userDataService;


    @Autowired
    GatewayController(UserDetailsManagerImpl userService, UserDataService userDataService) {
        this.userDataService = userDataService;
        this.userService = userService;
    }


    // tries to signup a new user
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(HttpServletRequest request) {
        User user = new User(request.getParameter("username"), request.getParameter("password"), new ArrayList<>());
        if (userService.userExists(user.getUsername())) {
            logger.warn(user.getUsername() + " already exists and cant be inserted");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            userService.createUser(user);
            try {
                userDataService.createUserData(user);
            } catch (AlreadyExistsException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        logger.info("ADDED USER" + user.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // tries to verify the Users Email
    @PostMapping("/verify")
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
//                userService.verifyMail(mail);
                logger.info("verified Email "+mail);
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
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/createGuest")
    public ResponseEntity<Object> createGuestAccount(HttpServletRequest request) {
        User guest = userService.createGuest();
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
