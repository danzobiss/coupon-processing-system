package com.danzobiss.couponprocessing.pubsub;

import com.danzobiss.couponprocessing.dto.BuyerDataDTO;
import com.danzobiss.couponprocessing.service.CouponService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BuyerDataQueueConsumer {

    @Autowired
    CouponService service;

    @RabbitListener(queues = {"buyer-data-sub"})
    public void receive(@Payload byte[] body) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        BuyerDataDTO buyerData = objectMapper.readValue(body, BuyerDataDTO.class);
        service.updateCouponWithBuyerData(buyerData);
    }

}
