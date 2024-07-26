package org.beyond.ordersystem.ordering.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.beyond.ordersystem.member.domain.Member;
import org.beyond.ordersystem.member.repository.MemberRepository;
import org.beyond.ordersystem.ordering.domain.OrderDetail;
import org.beyond.ordersystem.ordering.domain.Ordering;
import org.beyond.ordersystem.ordering.dto.CreateOrderDetailRequest;
import org.beyond.ordersystem.ordering.dto.CreateOrderDetailResponse;
import org.beyond.ordersystem.ordering.dto.CreateOrderRequest;
import org.beyond.ordersystem.ordering.dto.CreateOrderResponse;
import org.beyond.ordersystem.ordering.repository.OrderDetailRepository;
import org.beyond.ordersystem.ordering.repository.OrderingRepository;
import org.beyond.ordersystem.product.domain.Product;
import org.beyond.ordersystem.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW) // 전파레벨 새로운 트랜잭션 생성하도록 바꿈
@RequiredArgsConstructor
@Service
public class OrderingService {

    private final OrderingRepository orderingRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    // 방법 1. 쉬운 방식
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CreateOrderResponse createOrder2(CreateOrderRequest dto) {
        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(()->new EntityNotFoundException("없음"));
        Ordering ordering = orderingRepository.save(CreateOrderRequest.toEntity(dto, member));

//        OrderDetail생성 : order_id, product_id, quantity
        for(CreateOrderDetailRequest orderDto : dto.getOrderDetailList()){
            createOrderDetailItem(orderDto, ordering); // 따로 트랜잭션으로 저장하기
        }
        return CreateOrderResponse.fromEntity(ordering);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createOrderDetailItem(CreateOrderDetailRequest orderDto, Ordering ordering) {
        Product product = productRepository.findById(orderDto.getProductId()).orElse(null);
        int quantity = orderDto.getQuantity();
        OrderDetail orderDetail =  OrderDetail.builder()
                .product(product)
                .quantity(quantity)
                .ordering(ordering)
                .build();

        orderDetailRepository.save(orderDetail);
    }

    // 방법 2. JPA 최적화된 방식
    public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest) {

        // 주문한 회원
        Member member = memberRepository.findByIdOrThrow(createOrderRequest.getMemberId());
        Ordering ordering = CreateOrderRequest.toEntity(createOrderRequest, member);

        List<CreateOrderDetailRequest> orderDetailList = createOrderRequest.getOrderDetailList();;

        for (CreateOrderDetailRequest createOrderDetailRequest : orderDetailList) {
            Product product = productRepository.findByIdOrThrow(createOrderDetailRequest.getProductId());
            OrderDetail orderDetail = CreateOrderDetailRequest.toEntity(createOrderDetailRequest, product, ordering);
//            orderDetailRepository.save(orderDetail); // 이거 안해줘도 되네. cascade PERSIST 되나보다
            ordering.getOrderDetails().add(orderDetail);
        }

        Ordering savedOrder = orderingRepository.save(ordering);

        return CreateOrderResponse.fromEntity(savedOrder);
    }

    public Page<CreateOrderResponse> orderList(Pageable pageable) {
        Page<Ordering> orderList = orderingRepository.findAll(pageable);

        return orderList.map(CreateOrderResponse::fromEntity);
    }
}
