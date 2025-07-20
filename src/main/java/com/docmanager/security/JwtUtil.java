package com.docmanager.security;

import com.docmanager.model.entity.Roles;
import com.docmanager.model.entity.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
  private final String SECRET_KEY = "DocManagerSuperLongSecretKeySideProject123456";
  private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15; // 15分鐘
  private final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24; // 1天

  public String generateAccessToken(Users user) {
    return Jwts.builder()
        .setSubject(user.getUsername())
        .claim("roles", user.getRoles().stream().map(Roles::getRoleName).toList())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
        .compact();
  }

  public String generateRefreshToken(Users user) {
    return Jwts.builder()
        .setSubject(user.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
        .compact();
  }

  public String extractAccount(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractAccount(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration().before(new Date());
  }
}
