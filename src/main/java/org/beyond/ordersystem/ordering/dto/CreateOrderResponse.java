package org.beyond.ordersystem.ordering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beyond.ordersystem.ordering.domain.Ordering;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderResponse {
    private Long orderId;
    private Long memberId;
    private List<CreateOrderDetailResponse> orderDetails;

    public static CreateOrderResponse fromEntity(Ordering order) {
        List<CreateOrderDetailResponse> orderDetailResponseList = order.getOrderDetails().stream()
                .map(CreateOrderDetailResponse::fromEntity)
                .collect(Collectors.toList());

        return CreateOrderResponse.builder()
                .orderId(order.getId())
                .memberId(order.getMember().getId())
                .orderDetails(orderDetailResponseList)
                .build();
    }

}
