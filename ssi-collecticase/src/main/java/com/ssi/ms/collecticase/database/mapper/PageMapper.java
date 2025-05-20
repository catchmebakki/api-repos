package com.ssi.ms.collecticase.database.mapper;

import com.ssi.ms.collecticase.outputpayload.PaginationResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public class PageMapper {

    public static <T> PaginationResponse<T> toPaginationResponse(Page<?> page, List<T> content) {

        PaginationResponse.Pagination pagination = new PaginationResponse.Pagination(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );

        return new PaginationResponse<>(content, pagination);
    }
}
