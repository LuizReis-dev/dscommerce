package com.luizreis.dscommerce.services;

import com.luizreis.dscommerce.dto.CategoryDTO;
import com.luizreis.dscommerce.dto.ProductDTO;
import com.luizreis.dscommerce.dto.ProductMinDTO;
import com.luizreis.dscommerce.entities.Category;
import com.luizreis.dscommerce.entities.Product;
import com.luizreis.dscommerce.repositories.CategoryRepository;
import com.luizreis.dscommerce.repositories.ProductRepository;
import com.luizreis.dscommerce.services.exceptions.DatabaseException;
import com.luizreis.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;
    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        List<Category> result = repository.findAll();

        return result.stream().map(x -> new CategoryDTO(x)).toList();
    }
}
