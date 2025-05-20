package com.ssi.ms.collecticase.outputpayload;

import lombok.Data;

import java.util.List;

@Data
public class PaginationResponse<T> {

    private List<T> content;
    private Pagination pagination;

    public PaginationResponse(List<T> content, Pagination pagination) {
        this.content = content;
        this.pagination = pagination;
    }

    @Data
    public static class Pagination {

        private int currentPage;
        private int currentPageSize;
        private long totalElements;
        private int totalPages;
        private boolean hasNext;
        private boolean hasPrevious;

        public Pagination(int page, int size, long totalElements, int totalPages, boolean hasNext,
                          boolean hasPrevious) {
            this.currentPage = page;
            this.currentPageSize = size;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.hasNext = hasNext;
            this.hasPrevious = hasPrevious;
        }

    }
}
