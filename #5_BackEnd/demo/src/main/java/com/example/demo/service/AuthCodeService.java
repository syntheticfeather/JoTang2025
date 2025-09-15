package com.example.demo.service;

public interface AuthCodeService {

    // 生成并存储手机号验证码
    public void savePhoneAuthCode(String phone);

    // 生成随机的6位验证码
    public String generateRandomCode();

    // 将得到的验证码与存储的验证码做对比
    public void validateCode(String phone, String code);

    /*
     * 生成并验证邮箱验证码
     */
    // 
}
