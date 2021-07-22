package com.github.jadepeng.pipeline.core.dto;

import javax.validation.constraints.Min;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.Data;

@Data
public class BaseRequest implements Pageable {

    /**
     * 工作流名称
     */
    private String name;

    @Min(value = 0, message = "pageNumber必须为正整数")
    private int pageNumber = 0;

    @Min(value = 1, message = "pageSize必须为正整数")
    private int pageSize = 10;

    private String orderBy;

    private boolean descending;


    @Override
    public long getOffset() {
        return pageNumber * pageSize;
    }

    @Override
    public Sort getSort() {
        if(orderBy == null) {
            return Sort.unsorted();
        }

        return descending ? Sort.by(orderBy).descending() : Sort.by(orderBy).ascending();
    }

    @Override
    public Pageable next() {
        BaseRequest request =  this.clone();
        request.setPageNumber(this.pageNumber + 1);
        return request;
    }

    @Override
    public Pageable previousOrFirst() {
        BaseRequest request =  this.clone();
        request.setPageNumber(this.pageNumber - 1);
        return request;
    }

    @Override
    public BaseRequest clone() {
        BaseRequest request = new BaseRequest();
        request.pageNumber = this.pageNumber;
        request.pageSize = this.pageSize;
        request.orderBy = this.orderBy;
        request.descending = this.descending;
        return request;
    }

    @Override
    public Pageable first() {
        BaseRequest request =  this.clone();
        request.setPageNumber(0);
        return request;
    }

    @Override
    public boolean hasPrevious() {
        return this.pageNumber >= 1;
    }
}
