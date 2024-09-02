package com.haratres.ecommerce.elasticRepository;

import com.haratres.ecommerce.dto.ProductDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductElasticSearchRepository extends ElasticsearchRepository<ProductDto, String> {

      List<ProductDto> findByName(String name);

      List<ProductDto> findByCodeIgnoreCaseOrNameIgnoreCase(String code, String name);

      List<ProductDto> findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(String code, String name);

      Page<ProductDto> findAll(Pageable pageable);

      // Add fuzzy search method
      @Query("{\"bool\": {\"should\": [{\"fuzzy\": {\"name\": \"?0\"}}, {\"fuzzy\": {\"code\": \"?0\"}}]}}")
      List<ProductDto> findByNameOrCodeFuzzy(String text);
}