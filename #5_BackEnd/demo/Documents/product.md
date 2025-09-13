# product的文档

## getProduct

### 查询产品信息

**请求方式:** GET

**网址: http://192.168.6.128:8080/products**

**请求参数:**

| 字段名         | 类型     | 是否必填 | 说明      | 示例值              |
| ----------- | ------ | ---- | ---------------- |-|
| id          | number | 是    | 产品ID，唯一标识符 | 5                |

**请求示例**

    http://192.168.6.128:8080/products?id=5

**回应结构:**

```
{
    "code": 200,
    "message": "成功",
    "data": {
        "id": 5,
        "title": "全新笔记本电",
        "description": "九成新，2022年购入...",
        "type": "二手物品",
        "price": 2500.0,
        "status": "已售出",
        "publisherId": null,
        "publishTime": null,
        "updateTime": null
    },
    "timestamp": 1757642675859
}
```

**Postman测试结果:**


## getProductList

### 查询产品列表 

**请求方式:** GET

**网址: http://192.168.6.128:8080/products/list**

**请求参数:**  

无

**请求示例**

    http://192.168.6.128:8080/products/list

**回应结构:**

```
{
    "code": 200,
    "message": "成功",
    "data": [
        {
            "id": 4,
            "title": "全新笔记本电脑",
            "description": "九成新，2022年购入...",
            "type": "二手物品",
            "price": 2500.0,
            "status": "已售出",
            "publisherId": null,
            "publishTime": null,
            "updateTime": null
        },
        {
            "id": 5,
            "title": "全新笔记本电",
            "description": "九成新，2022年购入...",
            "type": "二手物品",
            "price": 2500.0,
            "status": "已售出",
            "publisherId": null,
            "publishTime": null,
            "updateTime": null
        },
        {
            "id": 6,
            "title": "二手笔记",
            "description": "全套笔记",
            "type": "二手物品",
            "price": 50.0,
            "status": "未售出",
            "publisherId": null,
            "publishTime": null,
            "updateTime": null
        }
    ],
    "timestamp": 1757642805586
}
```

## addProduct

### 添加产品信息

**请求方式:** POST

**网址: http://192.168.6.128:8080/products**

**请求参数:**

| 字段名         | 类型     | 是否必填 | 说明      | 示例值              |
| ----------- | ------ | ---- | ------- | ---------------- |
| title       | string | 是    | 商品标题    | "二手笔记本电脑"        |
| description | string | 否    | 商品详细描述  | "九成新，2022年购入..." |
| type        | string | 是    | 商品类型    | "二手物品"/"代取需求"   |
| price       | number | 是    | 价格      | 2500.00          |
| publisherId | number | 是    | 发布者用户ID | 1                |


**请求示例:**
```
{
    "title": "二手笔记本电脑",
    "description": "九成新，2022年购入...",
    "type": "二手物品",
    "price": 2500.00,
    "publisherId": 1
}
```

**回应结构:**

```
{
  "code": 200,       // 业务状态码（200表示成功，其他表示失败）
  "message": "操作成功", // 给开发者的提示信息
  "data": null
}
```

## updateProduct

**请求方式:** PUT

**网址: http://192.168.6.128:8080/products**

**请求参数:**

| 字段名         | 类型     | 是否必填 | 说明      | 示例值              |
| ----------- | ------ | ---- | ---------------- |-|
| id          | number | 是    | 产品ID，唯一标识符 | 5                |
| title       | string | 否    | 商品标题    | "二手笔记本电脑"        |
| description | string | 否    | 商品详细描述  | "九成新，2022年购入..." |
| type        | string | 否    | 商品类型    | "二手物品"/"代取需求"   |
| price       | number | 否    | 价格      | 2500.00          |

**请求示例:**
```
{
    "id": 5,
    "title": "二手笔记本电脑",
    "description": "九成新，2022年购入...",
    "type": "二手物品",
    "price": 2500.00,
}
```

**回应结构:**

```
{
  "code": 200,       // 业务状态码（200表示成功，其他表示失败）
  "message": "操作成功", // 给开发者的提示信息
  "data": null
}
```

## deleteProduct

**请求方式:** DELETE

**网址: http://192.168.6.128:8080/products**

**请求参数:**

| 字段名         | 类型     | 是否必填 | 说明      | 示例值              |
| ----------- | ------ | ---- | ---------------- |-|
| id          | number | 是    | 产品ID，唯一标识符 | 5                |

**请求示例:**

    http://192.168.6.128:8080/products?id=5

**回应结构:**

```
{
  "code": 200,       // 业务状态码（200表示成功，其他表示失败）
  "message": "操作成功", // 给开发者的提示信息
  "data": null
}




响应示例
1. 获取产品成功
json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "title": "测试产品",
    "price": 100.0,
    "createTime": "2023-10-15T10:30:45",
    "updateTime": "2023-10-15T10:30:45"
  },
  "timestamp": 1697363445000
}
2. 获取产品失败（不存在）
json
{
  "code": 404,
  "message": "产品不存在",
  "data": null,
  "timestamp": 1697363445000
}
3. 添加产品成功
json
{
  "code": 200,
  "message": "产品添加成功",
  "data": {
    "id": 2,
    "title": "新产品",
    "price": 200.0,
    "createTime": "2023-10-15T10:35:22",
    "updateTime": "2023-10-15T10:35:22"
  },
  "timestamp": 1697363722000
}
4. 删除产品成功
json
{
  "code": 200,
  "message": "产品删除成功",
  "data": null,
  "timestamp": 1697363445000
}