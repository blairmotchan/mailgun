package com.samurai.mailgun.service;

import com.samurai.mailgun.builder.Message;
import com.samurai.mailgun.data.MessageResponse;

public interface MailgunService {
    MessageResponse sendEmail(Message message);
}
