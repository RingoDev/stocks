package com.ringodev.stocks.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ringodev.stocks.service.auth.security.SecurityConstants;
import com.ringodev.stocks.service.user.UserDetailsManagerImpl;
import com.ringodev.stocks.service.user.UserImpl;
import com.ringodev.stocks.service.userdata.UserDataService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.ringodev.stocks.service.auth.security.SecurityConstants.JWT_COOKIE_NAME;

/**
 * verifies Username + Password and returns JWT Token in header
 */

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final UserDataService userDataService;
    private final AuthenticationManager authenticationManager;
    private final Environment env;
    private final UserDetailsManagerImpl userService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserDataService userDataService, Environment environment, UserDetailsManagerImpl userService) {
        this.userService = userService;
        setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
        this.userDataService = userDataService;
        this.authenticationManager = authenticationManager;
        this.env = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        Credentials credentials = this.getCredentials(request);
        assert credentials != null;
        Assert.notNull(credentials, "Didn't receive JSON Credentials");

        if (credentials.getUsername() == null && credentials.getEmail() != null) {
            try {
                credentials.setUsername(userDataService.getUsernameFromEmail(credentials.getEmail()));
            } catch (Exception e) {
                logger.warn("No Username could be found for email: " + credentials.getEmail());
                response.setStatus(401);
                return null;
            }
        }

        logger.info("Attempting Authentication with credentials: " + credentials.toString());

        if(!userService.loadUserByUsername(credentials.getUsername()).isEnabled()) {
            response.setStatus(403);
            return null;
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());

        logger.info("Authenticating authToken");
        return this.authenticationManager.authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {
        UserImpl user = ((UserImpl) authentication.getPrincipal());

        int expirationInSeconds = 60 * 60 * 4;
        Cookie cookie = createCookie(user, expirationInSeconds);
        response.addCookie(cookie);

        logger.info("Logged " + user.getUsername() + " in");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        SecurityContextHolder.clearContext();
        logger.info("Unsuccessful login");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    private Cookie createCookie(UserImpl user, int expirationInSeconds) {

        String token = createToken(user, expirationInSeconds);

        Cookie cookie = new Cookie(JWT_COOKIE_NAME, token);

        Assert.notNull(env);
        if (Arrays.asList(env.getActiveProfiles()).contains("prod")) cookie.setSecure(true);

        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(expirationInSeconds);
        return cookie;
    }

    private String createToken(UserImpl user, int expirationInSeconds) {

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(SecurityConstants.JWT_SECRET.getBytes()), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * expirationInSeconds)))
                .claim("rol", roles)
                .compact();
    }

    private Credentials getCredentials(HttpServletRequest request) {

        logger.info("Attempting to grab credentials");

        try {
            /*
             * HttpServletRequest can be read only once
             */
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            reader.mark(10000);
            String line = reader.readLine();

            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }

            reader.close();
            logger.info("Grabbed POST data: " + sb.toString());
            //json transformation
            ObjectMapper mapper = new ObjectMapper();
            Credentials credentials = mapper.readValue(sb.toString(), Credentials.class);
            logger.info("Created Credentials Object: " + credentials.toString());
            return credentials;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static class Credentials {
        String password;
        String username;
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

        @Override
        public String toString() {
            return "Credentials{" +
                    "password='" + password + '\'' +
                    ", username='" + username + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }
    }

}
