package com.luizreis.dscommerce.services;

import com.luizreis.dscommerce.dto.CategoryDTO;
import com.luizreis.dscommerce.dto.ProductDTO;
import com.luizreis.dscommerce.dto.ProductMinDTO;
import com.luizreis.dscommerce.entities.Category;
import com.luizreis.dscommerce.entities.Product;
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

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Produto não encontrado"));
        return new ProductDTO(product);
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    @Transactional(readOnly = true)
    public Page<ProductMinDTO> findAll(String name, Pageable pageable){
        Page<Product> result = repository.searchByName(name,pageable);
        return result.map(product -> new ProductMinDTO(product));
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto){
        Product entity = new Product();
        copyDtoToEntity(entity, dto);
        entity = repository.save(entity);
        return  new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(ProductDTO dto, Long id){
        try{
            Product entity = repository.getReferenceById(id);
            copyDtoToEntity(entity, dto);
            entity = repository.save(entity);
            return  new ProductDTO(entity);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Produto não encontrado");
        }

    }

    private void copyDtoToEntity(Product entity, ProductDTO dto) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());

        entity.getCategories().clear();
        for(CategoryDTO catDto : dto.getCategories()){
            Category cat = new Category();
            cat.setId(catDto.getId());
            entity.getCategories().add(cat);
        }
    }
}
