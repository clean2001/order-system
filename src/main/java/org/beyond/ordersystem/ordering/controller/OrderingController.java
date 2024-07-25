package org.beyond.ordersystem.ordering.controller;

import lombok.RequiredArgsConstructor;
import org.beyond.ordersystem.common.dto.SuccessResponse;
import org.beyond.ordersystem.ordering.dto.CreateOrderDetailResponse;
import org.beyond.ordersystem.ordering.dto.CreateOrderRequest;
import org.beyond.ordersystem.ordering.dto.CreateOrderResponse;
import org.beyond.ordersystem.ordering.service.OrderingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderingController {
    private final OrderingService orderingService;

    @GetMapping("/order/create")
    public ResponseEntity<SuccessResponse> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        CreateOrderResponse createOrderResponse = orderingService.createOrder(createOrderRequest);

        SuccessResponse response = SuccessResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .result(createOrderResponse)
                .statusMessage("주문이 등록되었습니다.")
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/order/list")
    public ResponseEntity<SuccessResponse> productList(@PageableDefault(size = 10) Pageable pageable) {
        Page<CreateOrderResponse> orderList = orderingService.orderList(pageable);
        SuccessResponse response = SuccessResponse.builder()
                .statusMessage("주문 리스트입니다.")
                .httpStatus(HttpStatus.OK)
                .result(orderList)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
