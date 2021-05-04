package org.skb.notification.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class NotificationRabbitMQConfig {

    @Value("${spring.rabbitmq.consumer}")
    private String consumer;

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(consumer);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(consumer);
    }

    @Bean
    public Binding notificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(notificationExchange())
                .with(consumer);
    }

}
