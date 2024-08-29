package com.haratres.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Sort;
@Data
@AllArgsConstructor
public class PageRequestDto {
    private int pageNumber;
    private int pageSize;
    private Sort.Direction sort;
    private String sortByColumn;
}
