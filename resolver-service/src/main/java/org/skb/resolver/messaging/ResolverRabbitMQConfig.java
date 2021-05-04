package org.skb.resolver.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class ResolverRabbitMQConfig {
    @Value("${spring.rabbitmq.consumer}")
    private String consumer;

    @Bean
    public TopicExchange resolverExchange() {
        return new TopicExchange(consumer);
    }

    @Bean
    public Queue resolverQueue() {
        return new Queue(consumer);
    }

    @Bean
    public Binding resolverBinding() {
        return BindingBuilder
                .bind(resolverQueue())
                .to(resolverExchange())
                .with(consumer);
    }
}
