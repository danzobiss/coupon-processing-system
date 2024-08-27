package com.danzobiss.couponprocessing.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue queueCoupon() {
        return new Queue("coupon-validated-pub", true);
    }

    @Bean
    public Queue queueBuyer() {
        return new Queue("buyer-data-sub", true);
    }

}
