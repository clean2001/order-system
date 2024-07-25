package org.beyond.ordersystem.ordering.dto;

import lombok.*;
import org.beyond.ordersystem.ordering.domain.OrderDetail;
import org.beyond.ordersystem.ordering.domain.Ordering;
import org.beyond.ordersystem.product.domain.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderDetailRequest {
    private Long productId;
    private Integer quantity;

    public static OrderDetail toEntity(CreateOrderDetailRequest createOrderDetailRequest, Product product, Ordering order) {
        return OrderDetail.builder()
                .quantity(createOrderDetailRequest.getQuantity())
                .ordering(order)
                .product(product)
                .build();
    }
}
