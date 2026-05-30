package com.crm.crmbackend.modules.sms;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber; // ✅ Semicolon laga kar variable yahan khatam hua

    // ✅ Naya method sahi syntax aur brackets ke saath yahan shuru hua
    public void sendSms(String toPhoneNumber, String messageBody) {
        try {
            // Twilio initialize kiya
            Twilio.init(accountSid, authToken);

            // Message create karne ka sahi tareeqa (commas ke saath)
            Message message = Message.creator(
                    new PhoneNumber(toPhoneNumber),       // 1st parameter (Comma ✅)
                    new PhoneNumber(twilioPhoneNumber),   // 2nd parameter (Comma ✅)
                    messageBody                           // 3rd parameter
            ).create();

            System.out.println("✅ SMS Sent Successfully! SID: " + message.getSid());
        } catch (Exception e) {
            System.err.println("❌ Twilio SMS Failed: " + e.getMessage());
        }
    }
}