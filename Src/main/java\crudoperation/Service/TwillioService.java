package com.ums.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Value("${twilio.accountSid}")
    private String accountSid;


    @Value("${twilio.authToken}")
    private String authToken;


    public void sendSMS(String to,String body){
        Twilio.init(accountSid,authToken);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(to),
                new com.twilio.type.PhoneNumber("+17692008678"),
                body
        ).create();
        System.out.println("SMS sent with SID:"+ message.getSid());
    }
}
