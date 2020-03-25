package com.example.myapplication.listeners;

public interface OTPListener {
    void onOtpReceived(String otp);
    void onOtpTimeout();

//    void onOTPReceived(String otp);
}
