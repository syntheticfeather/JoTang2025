package com.example.demo.service.ServiceImpl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.BusinessException;
import com.example.demo.service.AuthCodeService;
import com.example.demo.utils.RedisUtil;
import com.example.demo.utils.SmsUtil;

/*
 * 统一处理验证码的业务逻辑
 */
@Service
public class AuthCodeServiceImpl implements AuthCodeService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SmsUtil smsUtil;
    /*
     * 生成并存储手机号验证码
     */

    // 对应的redis key前缀
    private static final String SMS_KEY_PREFIX = "sms_code_";
    private static final String EMAIL_KEY_PREFIX = "email_code_";

    // 2分钟过期实践
    private static final int SMS_CODE_EXPIRE_TIME = 60 * 2;

    // 生成并存储手机号验证码
    @Override
    public void savePhoneAuthCode(String phone) {
        String key = SMS_KEY_PREFIX + phone;
        String code = generateRandomCode();

        if (smsUtil.sendSmsCode(phone, code) == false) {
            throw new BusinessException(400, "验证码发送失败");
        }

        redisUtil.set(key, code, SMS_CODE_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    // 将得到的验证码与存储的验证码做对比
    @Override
    public void validateCode(String phone, String code) {
        String key = SMS_KEY_PREFIX + phone;
        String redisCode = (String) redisUtil.get(key);
        if (redisCode == null) {
            throw new BusinessException(400, "验证码已过期");
        }
        if (!redisCode.equals(code)) {
            throw new BusinessException(400, "验证码错误");
        } else {
            redisUtil.delete(key);
        }
    }

    // 生成随机的6位验证码
    @Override
    public String generateRandomCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }


    /*
     * 生成并验证邮箱验证码
     */
    // 
    @Override
    public void saveEmailAuthCode(String email) {
        String key = EMAIL_KEY_PREFIX + email;
        String code = generateRandomCode();

        if (smsUtil.sendSmsCode(email, code) == false) {
            throw new BusinessException(400, "验证码发送失败");
        }

        redisUtil.set(key, code, SMS_CODE_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    @Override
    public void validateEmailCode(String email, String code) {
        String key = EMAIL_KEY_PREFIX + email;
        String redisCode = (String) redisUtil.get(key);
        if (redisCode == null) {
            throw new BusinessException(400, "验证码已过期");
        }
        if (!redisCode.equals(code)) {
            throw new BusinessException(400, "验证码错误");
        } else {
            redisUtil.delete(key);
        }
    }

}
