package com.example.junitstudy.util;

//@Component
public class MailSenderStub implements MailSender{

    @Override
    public boolean send() {
        return true;
    }
}
