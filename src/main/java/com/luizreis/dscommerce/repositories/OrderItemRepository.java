package com.luizreis.dscommerce.repositories;

import com.luizreis.dscommerce.entities.Order;
import com.luizreis.dscommerce.entities.OrderItem;
import com.luizreis.dscommerce.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {
}
