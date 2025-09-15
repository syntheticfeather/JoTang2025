package com.example.demo.service.ServiceImpl;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class AlipayServiceImpl {

    @Value("${alipay.app-id}")
    private String appId;
    @Value("${alipay.merchant-private-key}")
    private String privateKey;
    @Value("${alipay.alipay-public-key}")
    private String alipayPublicKey;
    @Value("${alipay.gateway}")
    private String gateway;
    @Value("${alipay.notify-url}")
    private String notifyUrl;
    @Value("${alipay.return-url}")
    private String returnUrl;

    @PostConstruct
    public void init() {
        // 初始化支付宝SDK配置
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = this.gateway.replace("https://", "").split("/")[0];
        config.signType = "RSA2";
        config.appId = this.appId;
        config.merchantPrivateKey = this.privateKey;
        config.alipayPublicKey = this.alipayPublicKey;
        config.notifyUrl = this.notifyUrl;
        Factory.setOptions(config);
    }

    public String createOrder(String outTradeNo, Double totalAmount, String subject) throws Exception {
        // 调用SDK，创建电脑网站支付请求，返回的是表单HTML字符串，前端可渲染或重定向
        AlipayTradePagePayResponse response = Factory.Payment
                .Page()
                .pay(subject, outTradeNo, totalAmount.toString(), returnUrl);
        return response.getBody();
    }
}
