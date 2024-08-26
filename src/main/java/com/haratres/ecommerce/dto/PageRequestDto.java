package com.haratres.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@Data
@AllArgsConstructor
public class PageRequestDto {
    private int pageNumber = 0;
    private int pageSize = 10;
    private Sort.Direction sort = Sort.Direction.ASC;
    private String sortByColumn = "id";

    public Pageable getPageable(PageRequestDto pageRequestDto) {
        int page = Objects.nonNull(pageRequestDto.getPageNumber()) ? pageRequestDto.getPageNumber() : this.pageNumber;
        int size = Objects.nonNull(pageRequestDto.getPageSize()) ? pageRequestDto.getPageSize() : this.pageSize;
        Sort.Direction sort = Objects.nonNull(pageRequestDto.getSort()) ? pageRequestDto.getSort() : this.sort;
        String sortByColumn = Objects.nonNull(pageRequestDto.getSortByColumn()) ? pageRequestDto.sortByColumn : this.sortByColumn;
        return PageRequest.of(page, size, sort, sortByColumn);
    }
}
