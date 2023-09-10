package com.luizreis.dscommerce.services;

import com.luizreis.dscommerce.dto.OrderDTO;
import com.luizreis.dscommerce.dto.ProductDTO;
import com.luizreis.dscommerce.entities.Order;
import com.luizreis.dscommerce.entities.Product;
import com.luizreis.dscommerce.repositories.OrderRepository;
import com.luizreis.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Produto não encontrado"));
        return new OrderDTO(order);
    }
}
