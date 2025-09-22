package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;

@Service
public class SmsUtil {

//     @Value("${tencent.cloud.sms.secret-id}")
//     private String secretId;

//     @Value("${tencent.cloud.sms.secret-key}")
//     private String secretKey;

//     @Value("${tencent.cloud.sms.sdk-app-id}")
//     private String sdkAppId;

//     @Value("${tencent.cloud.sms.sign-name}")
//     private String signName;

//     @Value("${tencent.cloud.sms.template-id}")
//     private String templateId;

//     /**
//      * 发送短信验证码
//      *
//      * @param phoneNumber 手机号（国内手机号不需+86，如 13800138000）
//      * @param code 验证码
//      * @return 是否发送成功
//      */
//     public boolean sendSmsCode(String phoneNumber, String code) {
//         try {
//             // 实例化一个认证对象，入参需要传入腾讯云账户密钥对 secretId，secretKey
//             Credential cred = new Credential(secretId, secretKey);

//             // 实例化一个http选项，可选的，没有特殊需求可以跳过
//             HttpProfile httpProfile = new HttpProfile();
//             httpProfile.setEndpoint("sms.tencentcloudapi.com"); // 指定接入地域域名（默认就近接入）
//             // 实例化一个客户端配置对象，可以指定超时时间等配置
//             ClientProfile clientProfile = new ClientProfile();
//             clientProfile.setHttpProfile(httpProfile);
//             // 实例化要请求产品(以sms为例)的client对象
//             SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile); // 第二个参数是地域信息            

//             // 实例化一个请求对象，根据调用的接口和实际情况，可以进一步设置请求参数
//             SendSmsRequest req = new SendSmsRequest();
//             // 设置短信应用ID
//             req.setSmsSdkAppId(sdkAppId);
//             // 设置短信签名内容
//             req.setSignName(signName);
//             // 设置模板 ID
//             req.setTemplateId(templateId);
//             // 设置模板参数。模板中有多个参数时，需按模板中定义的顺序放入数组中
//             req.setTemplateParamSet(new String[]{code, "5"}); // 假设模板有两个参数：验证码和有效期(分钟)
//             // 设置下发手机号码
//             req.setPhoneNumberSet(new String[]{"+86" + phoneNumber}); // 南美洲地区手机号格式为：+国家码+手机号

//             // 通过client对象调用DescribeInstances方法发起请求。注意请求方法名与请求对象是对应的。
//             SendSmsResponse resp = client.SendSms(req);

//             // 可以根据返回结果判断是否发送成功，例如判断 resp.getSendStatusSet()[0].getCode() 是否等于 "Ok"
//             if (resp.getSendStatusSet() != null && resp.getSendStatusSet().length > 0) {
//                 return "Ok".equals(resp.getSendStatusSet()[0].getCode());
//             }
//             return false;

//         } catch (TencentCloudSDKException e) {
//             System.err.println("腾讯云短信发送失败: " + e.toString());
//             return false;
//         }
//     }
}
