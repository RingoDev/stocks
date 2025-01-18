package com.ringodev.stocks.service.auth;

import com.ringodev.stocks.service.auth.security.SecurityConstants;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtBuilderService {

    UsernamePasswordAuthenticationToken verifyToken(String token)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {

        byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

        Jws<Claims> parsedToken = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token.replace("Bearer ", ""));

        String username = parsedToken
                .getBody()
                .getSubject();

        List<SimpleGrantedAuthority> authorities = ((List<?>) parsedToken.getBody()
                .get("rol")).stream()
                .map(authority -> new SimpleGrantedAuthority((String) authority))
                .collect(Collectors.toList());

        if (!StringUtils.isEmpty(username)) {
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }

        return null;
    }
}
