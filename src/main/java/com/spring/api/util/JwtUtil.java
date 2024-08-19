package com.spring.api.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class JwtUtil {
    static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${spring.jwt-secret-key}")
    private String JWT_SECRET_KEY;

    /*
     This Secret key (JWT_SECRET_KEY) generate by 
    
    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    String secretString = Encoders.BASE64.encode(key.getEncoded());

     */


    public String generateToken(String username, long expiration, Collection<? extends GrantedAuthority> authorities) {
        List<String> roles = authorities.stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .collect(Collectors.toList());
        String genratedToken =Jwts.builder()
        .subject(username)
        .claim("roles", roles) 
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(key())
        .compact();
        System.out.println("Generate Token is" +genratedToken);
        return genratedToken;
    }

    private Key key()
    {
        Key key=Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET_KEY));
        return key;
    }

    public String getJwtFromHeader(HttpServletRequest request)
    {
        String bearerToken=request.getHeader("Authorization");
        logger.debug("Authorization Header token: {}",bearerToken);
        if(bearerToken !=null && bearerToken.startsWith("Bearer "))
        {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String getUsernameFromToken(String token) {

        System.out.println("Authorization Header: 4" + token);
        return Jwts
                .parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }

    public boolean validateJwtToken(String token)
    {
        try 
        {
            System.out.println("Validated");
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token); 
            return true;
        } 
        catch(MalformedJwtException e)
        {
            logger.error(ResponseMessage.INVALID_TOKEN+"{}", e.getMessage());
        }
        catch(ExpiredJwtException e)
        {
            logger.error(ResponseMessage.EXPIRE_TOKEN+"{}", e.getMessage());
        }
        catch(UnsupportedJwtException e)
        {
            logger.error(ResponseMessage.UNSUPPORTE_TOKEN+"{}", e.getMessage());
        }
        catch(IllegalArgumentException e)
        {
            logger.error(ResponseMessage.EMPTY_TOKEN+"{}", e.getMessage());
        }
        return false;
    }
    
}