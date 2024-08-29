package com.haratres.ecommerce.service;

import com.haratres.ecommerce.dto.PageRequestDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PaginationService {
    @Autowired
    private EntityManager entityManager;

    public Pageable getPageable(PageRequestDto pageRequestDto) {
        int page = Objects.nonNull(pageRequestDto.getPageNumber()) ? pageRequestDto.getPageNumber() : 0;
        int size = Objects.nonNull(pageRequestDto.getPageSize()) ? pageRequestDto.getPageSize() : 10;
        Sort.Direction sort = Objects.nonNull(pageRequestDto.getSort()) ? pageRequestDto.getSort() : Sort.Direction.ASC;
        String sortByColumn = Objects.nonNull(pageRequestDto.getSortByColumn()) ? pageRequestDto.getSortByColumn() : "id";

        return PageRequest.of(page, size, sort, sortByColumn);
    }

    public List<String> getValidSortColumns(Class<?> entityClass) {
        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<?> entityType = metamodel.entity(entityClass);
        return entityType.getAttributes().stream()
                .map(attribute -> attribute.getName())
                .collect(Collectors.toList());
    }

}
