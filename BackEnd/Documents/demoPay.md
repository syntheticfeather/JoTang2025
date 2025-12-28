# 伪支付功能实现

## 流程

后端请求，创建订单

请求支付网关

生成前端的支付页面

支付完成，支付网关异步通知后端

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

## ~~实现~~

阿里云的支付申请凭证无法获取。

## 代码层面

### controller层正常调用接口

```java
# 对已有订单进行支付
@PostMapping("/{orderId}/pay")
public ResponseEntity<ApiResponse<String>> payOrder(@PathVariable Long orderId, HttpServletRequest request) {
    try {
        Order order = orderService.getOrderById(orderId);
        Double price = productService.getProduct(order.getProductId()).getPrice();
        /*
         * 主要是这步，返回的是支付宝的支付页面，直接返回给前端
         * 前端显示给用户进行付款
         */
        String payUrl = alipayService.createOrder(orderId.toString(), price.toString(), "测试商品");
        return ResponseEntity.ok(ApiResponse.success(payUrl));
    } catch (AlipayApiException e) {
        return ResponseEntity.ok(ApiResponse.error(500, "支付宝支付失败"));
    }
}
```

### Service层调用阿里云的接口，需要先配置自己的参数

```java
@Service
public class AlipayService {

    // 1. 初始化配置参数 (这些值需要替换成你自己的)
    private static final String APP_ID = "你的APPID";
    private static final String APP_PRIVATE_KEY = "你的应用私钥";
    private static final String FORMAT = "json";
    private static final String CHARSET = "UTF-8";
    private static final String ALIPAY_PUBLIC_KEY = "你的支付宝公钥";
    private static final String SIGN_TYPE = "RSA2"; // 推荐使用RSA2

    // 支付宝网关（注意：沙箱环境和生产环境不同）
    // 沙箱环境网关：https://openapi.alipaydev.com/gateway.do
    private static final String GATEWAY = "https://openapi.alipay.com/gateway.do";

    public String createOrder(String outTradeNo, String totalAmount, String subject) throws AlipayApiException {

        // 2. 创建AlipayClient实例
        AlipayClient alipayClient = new DefaultAlipayClient(
                GATEWAY, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);

        // 3. 创建API对应的Request对象
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        // 支付成功后，支付宝会异步通知（回调）这个地址（需要公网可访问）
        request.setNotifyUrl("https://your-domain.com/notify");
        // 支付成功后，同步跳转返回的页面（可以是你网站的订单页）
        request.setReturnUrl("https://your-domain.com/return_url");

        // 4. 组装业务参数
        String bizContent = "{"
                + "    \"out_trade_no\":\"" + outTradeNo + "\","
                + // 你的商户订单号
                "    \"total_amount\":\"" + totalAmount + "\","
                + // 订单金额（元）
                "    \"subject\":\"" + subject + "\","
                + // 订单标题
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\""
                + // 销售产品码（固定值）
                "  }";
        request.setBizContent(bizContent);

        // 5. 执行请求，获取表单字符串
        String form = alipayClient.pageExecute(request).getBody();

        // 这个form就是一个完整的HTML表单，输出到浏览器即可自动跳转到支付宝支付页面
        return form;
    }
}
```

### 异步通知的响应

更新订单状况

```java
@PostMapping("/{orderId}/notify")
public ResponseEntity<ApiResponse<Void>> handleNotify(@PathVariable Long orderId, HttpServletRequest request) {
    // 处理支付宝异步通知
    // TODO: 处理支付宝异步通知
    OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest();
    orderUpdateRequest.setOrderId(orderId);
    orderUpdateRequest.setStatus("已下单");
    orderService.updateOrder(orderUpdateRequest);
    return ResponseEntity.ok(ApiResponse.success(null));
    }
```