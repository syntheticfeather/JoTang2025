# RESTful API设计规范

### 1. 资源导向设计 (Resource-Oriented)
核心思想：将一切视为资源，使用名词而非动词

|资源类型	|示例	|说明|
|-|-|-|
|集合资源|	/users|	用户集合|
|单个资源	|/users/{id}	|特定用户|
|子资源	|/users/{id}/orders	|用户的订单|

### 2. 统一接口 (Uniform Interface)
使用标准的 HTTP 方法操作资源：

|HTTP| 方法	|操作	|幂等性|	安全|
|-|-|-|-|-|
|GET	|获取资源	|是	|是|
|POST	|创建资源|	否|	否|
|PUT|	更新/替换资源|	是	|否|
|PATCH|	部分更新资源|	否|	否|
|DELETE|	删除资源	|是	|否|

对数据的正则处理。

### 3.统一的相应格式 (Representational State Transfer)


```java
public class ApiResponse<T> {

    private Integer code;
    private String message;
    private T data;
    private Long timestamp;

}
```

见entity/apiResponse.java


