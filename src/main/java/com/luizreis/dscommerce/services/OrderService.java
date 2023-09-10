package com.luizreis.dscommerce.services;

import com.luizreis.dscommerce.dto.OrderDTO;
import com.luizreis.dscommerce.dto.OrderItemDTO;
import com.luizreis.dscommerce.dto.ProductDTO;
import com.luizreis.dscommerce.entities.Order;
import com.luizreis.dscommerce.entities.OrderItem;
import com.luizreis.dscommerce.entities.OrderStatus;
import com.luizreis.dscommerce.entities.Product;
import com.luizreis.dscommerce.repositories.OrderItemRepository;
import com.luizreis.dscommerce.repositories.OrderRepository;
import com.luizreis.dscommerce.repositories.ProductRepository;
import com.luizreis.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Produto não encontrado"));
        return new OrderDTO(order);
    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {
        Order order = new Order();
        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);
        order.setClient(userService.authenticated());
        for(OrderItemDTO itemDTO : dto.getItems()){
            Product product = productRepository.getReferenceById(itemDTO.getProductId());
            OrderItem item = new OrderItem(order, product, itemDTO.getQuantity(), product.getPrice());
            order.getItems().add(item);
        }

        repository.save(order);
        orderItemRepository.saveAll(order.getItems());

        return new OrderDTO(order);
    }
}
