# 对用户登录信息的储存，存id

## register

### 注册

**请求方式：** POST

**网址:** http://192.168.6.128:8080/user/register

**请求参数:**

| 字段名         | 类型     | 是否必填 | 说明      | 示例值              |
| ----------- | ------ | ---- | ---------------- |
| username    | string | 是    | 用户名，唯一标识符 | "gcc"             |
| password    | string | 是    | 密码      | "123456"          |

**请求示例**

    http://192.168.6.128:8080/user/register?username=gcc&password=123456

**回应结构:**

```
{
  "code": 200,       // 业务状态码（200表示成功，其他表示失败）
  "message": "操作成功",
  "data": {
    "username": "gcc", // 用户名
    "password": "123456" // 密码
  }
}
```
