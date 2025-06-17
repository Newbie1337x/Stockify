package org.stockify.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
/**
 * Service responsible for JWT (JSON Web Token) operations.
 * Handles token generation, validation, extraction of claims, and token invalidation.
 */
@Transactional
@Service
@RequiredArgsConstructor
public class JwtService {

    /**
     * Secret key used for signing JWT tokens
     */
    @Value("${jwt.secret}")
    private String jwtSecretKey;

    /**
     * Token expiration time in milliseconds
     */
    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    /**
     * Set of invalidated tokens that are no longer accepted
     */
    private Set<String> invalidatedToken  = ConcurrentHashMap.newKeySet();

    /**
     * Extracts the username (subject) from a JWT token
     *
     * @param token The JWT token to extract the username from
     * @return The username extracted from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Generates a new JWT token for the specified user
     * Includes user authorities in the token claims
     *
     * @param userDetails The user details for whom to generate the token
     * @return A new JWT token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());
        return buildToken(claims, userDetails, jwtExpiration);
    }
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    /**
     * Validates if a token is valid for the specified user
     * Checks if the token belongs to the user, is not expired, and the user account is valid
     *
     * @param token The JWT token to validate
     * @param userDetails The user details to validate against
     * @return True if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails)
    {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()))
                && !isTokenExpired(token)
                && userDetails.isAccountNonLocked()
                && userDetails.isEnabled();
    }
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() +
                        expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    /**
     * Invalidates a JWT token so it can no longer be used for authentication
     * Removes the "Bearer" prefix if present
     *
     * @param token The JWT token to invalidate
     */
    public void invalidateToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        invalidatedToken.add(token);
    }

    /**
     * Extracts the JWT token from the Authorization header of the current HTTP request.
     *
     * @return the JWT token as a String
     * @throws ResponseStatusException if the request attributes cannot be obtained or the Authorization header is invalid
     */
    public String extractTokenFromSecurityContext() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No se pudo obtener la request actual");
        }

        HttpServletRequest request = attributes.getRequest();
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token no válido o no presente");
        }

        return authHeader.substring(7);
    }


    /**
     * Checks if a token has been invalidated
     *
     * @param token The JWT token to check
     * @return True if the token has been invalidated, false otherwise
     */
    public boolean isTokenInvalidated(String token) {
        return invalidatedToken.contains(token);
    }
}
