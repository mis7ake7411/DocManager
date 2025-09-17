package com.docmanager.security;

import com.docmanager.constants.ErrorCode;
import com.docmanager.exception.GlobalException;
import com.docmanager.model.entity.Roles;
import com.docmanager.model.entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.security.SignatureException;
import java.util.Date;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * JWT 工具類別，用於生成和驗證 JSON Web Tokens。
 * <p>
 * 此類別提供生成訪問令牌和刷新令牌的方法，以及從令牌中提取帳戶信息和驗證令牌的方法。
 * </p>
 */

@Component
public class JwtUtil {
  private final String SECRET_KEY = "DocManagerSuperLongSecretKeySideProject123456";
  private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
  private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15; // 15分鐘
  private final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24; // 1天

  /**
   * 生成訪問令牌。
   *
   * @param user 用戶實體，包含帳戶和角色信息
   * @return 生成的訪問令牌
   */
  public String generateAccessToken(Users user) {
    return Jwts.builder()
        .setSubject(user.getAccount())
        .setIssuer("docmanager")            // iss
        .claim("type", "access")            // type
        .claim("roles", user.getRoles().stream().map(Roles::getRoleName).toList())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * 生成刷新令牌。
   *
   * @param user 用戶實體，包含帳戶信息
   * @return 生成的刷新令牌
   */
  public String generateRefreshToken(Users user) {
    return Jwts.builder()
        .setSubject(user.getAccount())
        .setIssuer("docmanager")
        .claim("type", "refresh")
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * 從令牌中提取帳戶信息。
   *
   * @param token JSON Web Token
   * @return 提取的帳戶名稱
   */
  public String extractAccount(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public String extractAccountFromRefresh(String token) {
    String raw = stripBearer(token); // 有時前端會帶 Bearer
    try {
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(key)
          .requireIssuer("docmanager")
          .require("type", "refresh")  // 確保不是拿 access 來冒充
          .setAllowedClockSkewSeconds(60)
          .build()
          .parseClaimsJws(raw)
          .getBody();

      return claims.getSubject(); // 只取帳號即可

    } catch (ExpiredJwtException e) {
      throw new GlobalException(ErrorCode.UNAUTHORIZED, "Refresh token 已過期");
    } catch (SecurityException |
             MalformedJwtException | UnsupportedJwtException e) {
      throw new GlobalException(ErrorCode.UNAUTHORIZED, "Refresh token 無效");
    } catch (IllegalArgumentException e) {
      throw new GlobalException(ErrorCode.BAD_REQUEST, "Refresh token 格式錯誤");
    }
  }

  private String stripBearer(String token) {
    if (token == null) return null;
    String t = token.trim();
    return (t.regionMatches(true, 0, "Bearer ", 0, 7)) ? t.substring(7).trim() : t;
  }

  /**
   * 驗證令牌的有效性。
   *
   * @param token       JSON Web Token
   * @param userDetails 用戶詳細信息
   * @return 如果令牌有效且與用戶匹配，則返回 true，否則返回 false
   */
  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractAccount(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  /**
   * 檢查令牌是否已過期。
   *
   * @param token JSON Web Token
   * @return 如果令牌已過期，則返回 true，否則返回 false
   */
  private boolean isTokenExpired(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getExpiration()
        .before(new Date());
  }

  /**
   * 從令牌中提取角色信息。
   *
   * @param token JSON Web Token
   * @return 提取的角色列表
   */
  public List<String> extractRoles(String token) {
    var claims = Jwts.parserBuilder().setSigningKey(key).build()
        .parseClaimsJws(token).getBody();
    Object rolesObj = claims.get("roles");
    if (rolesObj instanceof List<?> list) {
      return list.stream().map(Object::toString).toList();
    }
    return List.of();
  }
}
