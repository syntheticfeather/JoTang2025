# 伪支付功能实现

## 流程

后端请求，创建订单

请求支付网关

生成支付页面

支付完成，支付网关通知后端

后端更新订单状态

## 依赖

**阿里云的alipay:

```xml
<dependency>
    <groupId>com.alipay.sdk</groupId>
    <artifactId>alipay-easysdk</artifactId>
    <version>2.3.4</version> <!-- 示例版本，请以官方为准 -->
</dependency>
```

## 实现

### 1.后端请求，创建订单

修改