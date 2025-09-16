# User的文档

# register

### 注册

**请求方式：** POST

**网址:** http://192.168.6.128:8080/users/register

**请求参数:**

| 字段名         | 类型     | 是否必填 | 说明      | 示例值              |
| ----------- | ------ | ---- | ---------------- |-|
| username    | string | 是    | 用户名，唯一标识符 | "gcc"             |
| password    | string | 是    | 密码      | "123456"          |



**请求示例(请求体):**

用户注册

```json
{
  "username": "gcc",
  "password": "123456"
}
```

管理员注册
```json
{
  "username": "gcc",
  "password": "123456",
  "role" : "ADMIN"
  "adminRegister" : "654321"
}
```

**回应结构:**

```json
{
    "code": 200,
    "message": "注册成功",
    "data": {
        "id": 3,
        "username": "gcc3",
        "password": "$2a$10$fy7aeqj109l2qhDU9iM3iedU0YCIh2Y0VHo0Qs1QH7Y5H9BOPvXK.",
        "role": "USER",
        "phone": null,
        "createTime": "2025-09-11T19:26:23.927307666",
        "updateTime": "2025-09-11T19:26:23.927332431"
    },
    "timestamp": 1757643983939
}
```
**Postman测试结果:**

![](UserImgs/RegSuc.png "用户注册成功")
![](UserImgs/RegAdminSuc.png "管理员注册成功")
![](UserImgs/RegAdminFail.png "管理员注册权限不足")


--- 
# login

### 登录

**请求方式：** POST

**网址:** http://192.168.6.128:8080/users/login

**请求参数(请求体):**

| 字段名         | 类型     | 是否必填 | 说明      | 示例值              |
| ----------- | ------ | ---- | ---------------- |-|
| username    | string | 是    | 用户名，唯一标识符 | "gcc"             |
| password    | string | 是    | 密码      | "123456"          |

**请求示例**

```
{
  "username": "gcc",
  "password": "123456"
}
```

**回应结构:**

```
{
    "code": 200,
    "message": "登录成功",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwidXNlcm5hbWUiOiJnY2MyIiwiaWF0IjoxNzU3NjQ0NTY0LCJleHAiOjE3NTgyNDkzNjR9.nxACA7qvA9Aep2T80Ak0ty0SuaydPFMAbo51qf7bj8E",
        "id": 2,
        "username": "gcc2",
        "role": "USER",
        "phone": null
    },
    "timestamp": 1757644564934
}
```
**Postman测试结果:**

![](UserImgs/LoginSuc.png "登录成功")
![](UserImgs/LoginFail.png "登录失败")

# getCurrentUser

### 获取当前用户信息

**请求方式：** GET

**网址:** http://192.168.6.128:8080/users/me

**请求参数(请求头中):**

| 字段名         | 类型     | 是否必填 | 说明      | 示例值              |
| ----------- | ------ | ----     | ---------------- |-|
| Authorization | string    | 是           | 登录token      | "eyJhbGciOiJI......" |

**请求示例**

    http://192.168.6.128:8080/users/me

**回应结构:**   

```
{
    "code": 200,
    "message": "成功",
    "data": {
        "id": 2,
        "username": "gcc2",
        "password": "$2a$10$9k/kdGswYAaIPoDFYZGk5urrS3Py5P/ttmJJBQ6S0xr6lkA4MRdZ2",
        "role": "USER",
        "phone": null,
        "createTime": null,
        "updateTime": null
    },
    "timestamp": 1757647078412
}
```
**Postman测试结果:**
![](UserImgs/Me.png "获取当前用户信息")


# resetPassword

### 重置密码

**请求方式：** PUT

**网址:** http://192.168.6.128:8080/users/resetpwd

**请求参数(请求体):**

| 字段名         | 类型     | 是否必填 | 说明      | 示例值              |
| ----------- | ------ | ---- | ---------------- |-|
| oldPassword    | string | 是    | 旧密码      | "123456"          |
| newPassword    | string | 是    | 新密码      | "654321"          |
| confirmPassword    | string | 是    | 确认新密码      | "654321"          |


**请求示例**

```
{
  "oldPassword": "123456",
  "newPassword": "654321",
  "confirmPassword": "654321"
}
```

**回应结构:**

```
{
    "code": 200,
    "message": "密码重置成功",
    "data": "654321",
    "timestamp": 1757649481622
}
```
**Postman测试结果:**
![](UserImgs/resetpwd.png "密码重置成功")

# upgradeAdmin

### 升级管理员

**请求方式：** PUT

**网址:** http://192.168.6.128:8080/users/upgrade

**请求参数(请求体):**

| 字段名         | 类型     | 是否必填 | 说明      | 示例值              |
| ----------- | ------ | ---- | ---------------- |-|
| userId    | string | 是    | 用户id     | 5      |

**请求示例**

http://192.168.6.128:8080/users/upgrade?userId=5

**回应结构:**

```json
{
    "code": 200,
    "message": "用户升级为管理员",
    "data": {
        "id": 2,
        "username": "gcc2",
        "password": "$2a$10$IQhqTR405gS0Hw1fWXfTqOtk1RqslO9ComiULiG2RBwkxcZBFWGkq",
        "role": "ADMIN",
        "phone": null,
        "createTime": "2025-09-11 19:21:20",
        "updateTime": "2025-09-11 20:58:02"
    },
    "timestamp": 1757750864192
}
```
**Postman测试结果:**
![](UserImgs/upgradeAdminSuc.png "升级管理员成功")
![](UserImgs/upgradeAdminFail.png "升级管理员失败")

# updateUser

### 更新用户信息

**请求方式：** PUT

**网址:** http://192.168.6.128:8080/users/update

**请求参数(请求体):**

| 字段名         | 类型     | 是否必填 | 说明      | 示例值              |  
| ----------- | ------ | ---- | ---------------- |-|
|username    | string | 否    | 用户名，唯一标识符 | "gcc"             |
|phone    | string | 否    | 手机号      | "13812345678"          |

**请求示例**

```json
{
  "username": "gcc2",
  "phone": "13812345678"
}
```

**回应结构:**

```json
{
    "code": 200,
    "message": "用户信息更新成功",
    "data": null,
    "timestamp": 1757751279482
}
```

**Postman测试结果:**
![](UserImgs/updateSuc.png "更新用户信息成功")
![](UserImgs/updateFail.png "更新用户信息失败")
