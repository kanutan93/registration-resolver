package org.skb.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void sendMail(String toAddress, String messageBody) {
        log.info("Message sent to {}, body \"{}\".", toAddress, messageBody);
    }
}
