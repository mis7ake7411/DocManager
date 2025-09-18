# DocManager

一個 Spring Boot 3 的文件管理系統，內建 Swagger/OpenAPI、JWT 驗證（Access/Refresh Token）、Users 管理（新增/修改/鎖定/解鎖/刪除），並採分層架構（Controller/Service/Repository/DTO/VO）。

## 功能總覽

- Swagger UI 與 OpenAPI 文件（springdoc-openapi）
- JWT 認證授權
  - Access Token：15 分鐘
  - Refresh Token：1 天
  - Refresh 兌換 API 與 Logout（可選擇黑名單或前端清除機制）
- Users 模組
  - 建立/更新（使用 DTO 驗證）、刪除
  - 鎖定/解鎖帳號
  - 建立時密碼使用 BCrypt 加密
  - 回傳使用 VO（隱藏敏感資訊）

## 技術棧

- Java 17+
- Spring Boot 3.5.x
- Spring Web, Spring Security, Spring Data JPA
- springdoc-openapi-starter-webmvc-ui
- PostgreSQL（可替換）
- Lombok, Jakarta Validation
- JJWT（io.jsonwebtoken）

## 專案結構（重點）

- com.docmanager.security
  - SecurityConfig：安全性設定、JWT 過濾器註冊、Swagger 路徑白名單
  - JwtAuthenticationFilter：攔截請求解析 JWT，設定 SecurityContext
  - JwtUtil：簽發與驗證 Access/Refresh Token（HS256）
- resources/application.yml：springdoc 與 CORS/資料庫等設定

已存在重點程式：
- Swagger 路徑白名單與設定（SecurityConfig, application.yml）
- JWT 過濾與例外處理（JwtAuthenticationFilter）
- JWT 產出與解析（JwtUtil，Access/Refresh 期限分別為 15 分鐘/1 天）

## 安裝與啟動

1) 先準備環境
- Java 17+
- Maven 3.9+
- PostgreSQL（或調整為你的資料庫）
- 設定環境變數：JASYPT_ENCRYPTOR（供資料庫密碼解密）

2) 設定 application.yml（僅摘錄重點）

```yaml
springdoc:
  api-docs:
    enabled: true
  swagger-ui:
    enabled: true
```

3) 啟動

```bash
mvn spring-boot:run
```

服務預設埠：8080

## Swagger / OpenAPI

- Swagger UI：/swagger-ui/index.html
- OpenAPI JSON：/v3/api-docs

你的 SecurityConfig 已放行：/swagger-ui/** 與 /v3/api-docs/**。

## 認證流程（JWT）

- Access Token：存活 15 分鐘，放在 Authorization: Bearer <token>
- Refresh Token：存活 1 天，用來兌換新的 Access Token

JwtUtil（節錄概念）：
- 生成 Access/Refresh：Jwts.builder().signWith(key, SignatureAlgorithm.HS256)
- 解析與驗證：Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)

建議 API（對應 AuthController/AuthService）：
- POST /auth/login：帳密登入，回傳 accessToken、refreshToken、expiresIn
- POST /auth/refresh：用 refreshToken 兌換新的 accessToken
- POST /auth/logout：可選擇黑名單機制或前端清除 Token

範例請求：

```bash
# login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"account":"admin","password":"password"}'

# refresh
curl -X POST http://localhost:8080/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"<your_refresh_token>"}'
```

過濾器放行路徑（JwtAuthenticationFilter.shouldNotFilter）：
- /auth/**
- /public/**
- /swagger-ui/**
- /v3/api-docs/**

## Users 模組（建議實作）

Controller：UsersController
Service：UsersService

- POST /users：建立使用者（使用 CreateUserDto），回傳 UserVO（不含密碼）
- PUT /users/{id}：更新使用者（使用 UpdateUserDto）
- POST /users/{id}/lock：鎖定
- POST /users/{id}/unlock：解鎖
- DELETE /users/{id}：刪除

DTO/VO 建議結構：
- CreateUserDto：account, password, name, email, roles[]
- UpdateUserDto：name, email, roles[]（需視實際需求）
- UserVO：id, account, name, email, roles[], locked, createdAt, updatedAt（不含 password）

密碼加密：
- 使用 BCryptPasswordEncoder（SecurityConfig 中已提供 PasswordEncoder Bean）

## 常見問題（jjwt 棄用說明）

- 為什麼 signWith 顯示棄用？
  - 舊版用法如 signWith(SignatureAlgorithm, String/byte[]) 已被棄用。
  - 正確做法：先建立 Key，再使用 signWith(Key, SignatureAlgorithm)。

```java
Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
String jwt = Jwts.builder()
    .signWith(key, SignatureAlgorithm.HS256)
    .compact();
```

- 為什麼 Jwts.parser().setSigningKey(...) 顯示棄用？
  - 請改用 Jwts.parserBuilder().setSigningKey(key).build()。

```java
Claims claims = Jwts.parserBuilder()
    .setSigningKey(key)
    .build()
    .parseClaimsJws(token)
    .getBody();
```

## 驗證與錯誤處理

SecurityConfig.authenticationEntryPoint 已對 TOKEN_EXPIRED 與 TOKEN_INVALID 回傳一致格式的 401 錯誤訊息，可直接在前端統一處理。

## 後續可加值項目

- Refresh Token 黑名單（儲存於 DB/Redis），支援強制登出
- Swagger 分群組與範例 Schema（DTO/VO）
- 單元/整合測試：Auth 與 Users 關鍵流程

## 授權

僅供學習與內部使用，請依公司與專案政策調整。

