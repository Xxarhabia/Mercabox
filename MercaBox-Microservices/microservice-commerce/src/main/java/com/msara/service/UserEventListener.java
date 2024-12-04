package com.msara.service;

import com.msara.config.RabbitMQConfig;
import com.msara.domain.event.UserEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventListener {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void processUserEvent(UserEvent event) {
        System.out.println("Evento recibido: " + event);
    }
}
