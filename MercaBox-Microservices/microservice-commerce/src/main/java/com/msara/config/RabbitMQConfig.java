package com.msara.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "comercio.usuario.queue";
    public static final String EXCHANGE_NAME = "usuarios.exchange";

    @Bean
    public Queue userQueue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Binding binding(Queue userQueue, TopicExchange userExchange) {
        return BindingBuilder.bind(userExchange).to(userExchange).with("usuario.#");
    }
}
