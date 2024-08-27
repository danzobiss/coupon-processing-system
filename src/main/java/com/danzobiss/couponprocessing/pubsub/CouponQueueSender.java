package com.danzobiss.couponprocessing.pubsub;

import com.danzobiss.couponprocessing.entity.Coupon;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponQueueSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queueCoupon;

    public void sendCoupon(Coupon coupon) {
        rabbitTemplate.convertAndSend(queueCoupon.getName(), coupon);
    }
}
