package org.beyond.ordersystem.ordering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beyond.ordersystem.ordering.domain.OrderDetail;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDetailResponse {
    private Long orderDetailId;
    private Long productId;
    private String productName;
    private Integer quantity;

    public static CreateOrderDetailResponse fromEntity(OrderDetail orderDetail) {
        return CreateOrderDetailResponse.builder()
                .orderDetailId(orderDetail.getId())
                .productId(orderDetail.getProduct().getId())
                .productName(orderDetail.getProduct().getName())
                .quantity(orderDetail.getQuantity())
                .build();
    }
}
