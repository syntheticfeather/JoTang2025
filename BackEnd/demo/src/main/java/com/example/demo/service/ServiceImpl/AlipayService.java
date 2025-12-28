// package com.example.demo.service.ServiceImpl;

// import org.springframework.stereotype.Service;

// import com.alipay.api.AlipayApiException;
// import com.alipay.api.AlipayClient;
// import com.alipay.api.DefaultAlipayClient;
// import com.alipay.api.request.AlipayTradePagePayRequest;

// /*
//  * 没做出来
//  */
// @Service
// public class AlipayService {

//     // 1. 初始化配置参数 (这些值需要替换成你自己的)
//     private static final String APP_ID = "你的APPID";
//     private static final String APP_PRIVATE_KEY = "你的应用私钥";
//     private static final String FORMAT = "json";
//     private static final String CHARSET = "UTF-8";
//     private static final String ALIPAY_PUBLIC_KEY = "你的支付宝公钥";
//     private static final String SIGN_TYPE = "RSA2"; // 推荐使用RSA2

//     // 支付宝网关（注意：沙箱环境和生产环境不同）
//     // 沙箱环境网关：https://openapi.alipaydev.com/gateway.do
//     private static final String GATEWAY = "https://openapi.alipay.com/gateway.do";

//     public String createOrder(String outTradeNo, String totalAmount, String subject) throws AlipayApiException {

//         // 2. 创建AlipayClient实例
//         AlipayClient alipayClient = new DefaultAlipayClient(
//                 GATEWAY, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);

//         // 3. 创建API对应的Request对象
//         AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

//         // 支付成功后，支付宝会异步通知（回调）这个地址（需要公网可访问）
//         request.setNotifyUrl("https://your-domain.com/notify");
//         // 支付成功后，同步跳转返回的页面（可以是你网站的订单页）
//         request.setReturnUrl("https://your-domain.com/return_url");

//         // 4. 组装业务参数
//         String bizContent = "{"
//                 + "    \"out_trade_no\":\"" + outTradeNo + "\","
//                 + // 你的商户订单号
//                 "    \"total_amount\":\"" + totalAmount + "\","
//                 + // 订单金额（元）
//                 "    \"subject\":\"" + subject + "\","
//                 + // 订单标题
//                 "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\""
//                 + // 销售产品码（固定值）
//                 "  }";
//         request.setBizContent(bizContent);

//         // 5. 执行请求，获取表单字符串
//         String form = alipayClient.pageExecute(request).getBody();

//         // 这个form就是一个完整的HTML表单，输出到浏览器即可自动跳转到支付宝支付页面
//         return form;
//     }
// }
