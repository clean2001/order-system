package org.beyond.ordersystem.ordering.service;

import lombok.RequiredArgsConstructor;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderingService {

    private final OrderingRepository orderingRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest) {

        // 주문한 회원
        Member member = memberRepository.findByIdOrThrow(createOrderRequest.getMemberId());
        Ordering ordering = CreateOrderRequest.toEntity(createOrderRequest, member);
        Ordering savedOrder = orderingRepository.save(ordering);

        List<CreateOrderDetailRequest> orderDetailList = createOrderRequest.getOrderDetailList();
//        List<OrderDetail> orderDetails = new ArrayList<>();

        for (CreateOrderDetailRequest createOrderDetailRequest : orderDetailList) {
            Product product = productRepository.findByIdOrThrow(createOrderDetailRequest.getProductId());
            OrderDetail orderDetail = CreateOrderDetailRequest.toEntity(createOrderDetailRequest, product, savedOrder);
//            orderDetails.add(orderDetail);
//            orderDetailRepository.save(orderDetail); // 이거 안해줘도 되네. PERSIST 되나보다

            ordering.getOrderDetails().add(orderDetail);
        }

//        savedOrder.updateDetails(orderDetails);
        return CreateOrderResponse.fromEntity(savedOrder);
    }

    public Page<CreateOrderResponse> orderList(Pageable pageable) {
        Page<Ordering> orderList = orderingRepository.findAll(pageable);

        return orderList.map(CreateOrderResponse::fromEntity);
    }
}
