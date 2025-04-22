# RESTful API 文档

## 基础 URL

```
http://<您的域名>/api
```

## 端点

### 1. 用户注册

- **端点**：`POST /users/register`

- **描述**：注册新用户。

- **请求体**：

  ```json
  {
    "username": "字符串",
    "password": "字符串",
    "repeatPassword": "字符串",
    "email": "字符串",
    "captcha": "字符串"
  }
  ```

- **约束**：

  - `username`：非空，唯一。
  - `password`：需包含字母和数字，至少 8 位。
  - `repeatPassword`：需与 `password` 匹配。
  - `email`：有效邮箱格式。
  - `captcha`：可选，若提供需与 Redis 存储的验证码匹配。

- **响应**：

  - `201 Created`：注册成功。

    ```json
    {
      "id": 1,
      "username": "字符串",
      "email": "字符串"
    }
    ```

  - `400 Bad Request`：输入无效（例如用户名已存在、密码不匹配）。

    ```json
    null
    ```

- **示例**：

  ```bash
  curl -X POST http://<您的域名>/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john_doe","password":"Password123","repeatPassword":"Password123","email":"john@example.com"}'
  ```

### 2. 用户登录

- **端点**：`POST /users/login`

- **描述**：验证用户身份并返回 JWT 令牌。

- **请求体**：

  ```json
  {
    "username": "字符串",
    "password": "字符串",
    "captcha": "字符串"
  }
  ```

- **约束**：

  - `username`：必须存在。
  - `password`：需与存储的加密密码匹配。
  - `captcha`：需与 Redis 中存储的验证码匹配。

- **响应**：

  - `200 OK`：登录成功，返回 JWT 令牌。

    ```json
    "eyJhbGciOiJIUzUxMiJ9..."
    ```

  - `401 Unauthorized`：用户名、密码或验证码错误。

    ```json
    "错误信息"
    ```

- **示例**：

  ```bash
  curl -X POST http://<您的域名>/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john_doe","password":"Password123","captcha":"ABCDE"}'
  ```

### 3. 获取用户信息

- **端点**：`GET /users/{id}`

- **描述**：根据 ID 获取用户详情。

- **路径参数**：

  - `id`：整数，用户 ID。

- **请求头**：

  - `Authorization: Bearer <token>`

- **响应**：

  - `200 OK`：用户存在。

    ```json
    {
      "id": 1,
      "username": "字符串",
      "email": "字符串"
    }
    ```

  - `404 Not Found`：用户不存在。

    ```json
    null
    ```

- **示例**：

  ```bash
  curl -X GET http://<您的域名>/api/users/1 \
  -H "Authorization: Bearer <token>"
  ```

### 4. 更新用户信息

- **端点**：`PUT /users/{id}`

- **描述**：更新用户详情。

- **路径参数**：

  - `id`：整数，用户 ID。

- **请求体**：

  ```json
  {
    "username": "字符串",
    "password": "字符串",
    "repeatPassword": "字符串",
    "email": "字符串"
  }
  ```

- **约束**：

  - `username`：若更改，需唯一。
  - `password`：可选，若提供需符合约束且与 `repeatPassword` 匹配。
  - `email`：有效邮箱格式。

- **请求头**：

  - `Authorization: Bearer <token>`

- **响应**：

  - `200 OK`：更新成功。

    ```json
    {
      "id": 1,
      "username": "字符串",
      "email": "字符串"
    }
    ```

  - `400 Bad Request`：输入无效（例如用户名已存在、密码不匹配）。

    ```json
    null
    ```

  - `404 Not Found`：用户不存在。

- **示例**：

  ```bash
  curl -X PUT http://<您的域名>/api/users/1 \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"username":"john_doe_updated","email":"john_updated@example.com"}'
  ```

### 5. 删除用户

- **端点**：`DELETE /users/{id}`

- **描述**：根据 ID 删除用户。

- **路径参数**：

  - `id`：整数，用户 ID。

- **请求头**：

  - `Authorization: Bearer <token>`

- **响应**：

  - `204 No Content`：删除成功。
  - `404 Not Found`：用户不存在。

- **示例**：

  ```bash
  curl -X DELETE http://<您的域名>/api/users/1 \
  -H "Authorization: Bearer <token>"
  ```

### 6. 生成验证码

- **端点**：`GET /captcha`

- **描述**：生成验证码图片并将验证码存储到 Redis。

- **查询参数**：

  - `username`：字符串，可选（默认为 "defaultUser"）。

- **响应**：

  - `200 OK`：返回验证码 PNG 图片。
    - 内容类型：`image/png`
  - `500 Internal Server Error`：图片生成失败。

- **示例**：

  ```bash
  curl -X GET http://<您的域名>/api/captcha?username=john_doe \
  --output captcha.png
  ```

### 7. 验证验证码

- **端点**：`POST /captcha/verify`

- **描述**：验证用户提交的验证码。

- **请求体**：

  ```json
  {
    "username": "字符串",
    "captcha": "字符串"
  }
  ```

- **响应**：

  - `200 OK`：验证结果。

    ```json
    {
      "valid": true
    }
    ```

- **示例**：

  ```bash
  curl -X POST http://<您的域名>/api/captcha/verify \
  -H "Content-Type: application/json" \
  -d '{"username":"john_doe","captcha":"ABCDE"}'
  ```

## 备注

- 错误信息在 `400`、`401` 和 `404` 状态码下以响应体形式返回。
- 需确保 Redis 已配置用于验证码存储，且 `application.properties` 中设置了 JWT 密钥。