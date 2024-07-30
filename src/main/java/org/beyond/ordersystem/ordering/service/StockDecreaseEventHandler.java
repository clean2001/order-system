package org.beyond.ordersystem.ordering.service;

import lombok.RequiredArgsConstructor;
import org.beyond.ordersystem.common.config.RabbitMqConfig;
import org.beyond.ordersystem.ordering.dto.StockDecreaseEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class StockDecreaseEventHandler {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqConfig rabbitMqConfig;


    public void publish(StockDecreaseEvent event) {
//        rabbitTemplate.convertAndSend(큐이름, 자바객체(json)이 들어옴);
        rabbitTemplate.convertAndSend(/*큐이름*/RabbitMqConfig.STOCK_DECREASE_QUEUE, event);
    }

//    @Transactional // transactional은 @Component가 붙어있는 곳에 붙을 수 있다.
//    @RabbitListener(queues = /* 큐 이름 */ RabbitMqConfig.STOCK_DECREASE_QUEUE)
//    public void listen() {
//
//    }
}
