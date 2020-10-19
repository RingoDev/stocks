package com.ringodev.stocks.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ringodev.stocks.service.auth.security.SecurityConstants;
import com.ringodev.stocks.service.user.UserImpl;
import com.ringodev.stocks.service.userdata.UserDataService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * verifies Username + Password and returns JWT Token in header
 */

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private UserDataService userDataService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(UserDataService userDataService, AuthenticationManager authenticationManager) {
        setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
        this.userDataService = userDataService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        Credentials credentials = this.getCredentials(request);
        assert credentials != null;
        Assert.notNull(credentials, "Didn't receive JSON Credentials");
        Assert.notNull(userDataService,"UserDataService wasn't initialized");

        if (credentials.getUsername() == null && credentials.getEmail() != null) {
            credentials.setUsername(userDataService.getUsernameFromEmail(credentials.getEmail()));
        }

        logger.info("Attempting Authentication with credentials: " + credentials.toString());


        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());

        logger.info("Authenticating authToken");
        return this.authenticationManager.authenticate(authRequest);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) throws IOException {
        UserImpl user = ((UserImpl) authentication.getPrincipal());

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

        System.out.println("Logged somebody in");

        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 4)))
                .claim("rol", roles)
                .compact();


        response.getWriter().write(token);
        response.getWriter().flush();

        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
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

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        SecurityContextHolder.clearContext();
        System.out.println("Unsuccessful login");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
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
