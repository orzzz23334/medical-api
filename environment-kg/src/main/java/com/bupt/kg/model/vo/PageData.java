package com.bupt.kg.model.vo;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
/**
 * 对spring提供的page类，进行了定制化封装
 */
public class PageData<T> {
    private int pageNum;
    private int pageSize;
    private int contentSize;
    private int totalPages;
    private Long totalElements;
    private List<T> content;

    public PageData(Page<T> page) {
        this.totalPages = page.getTotalPages();
        this.pageSize = page.getSize();
        this.contentSize = page.getNumberOfElements();
        this.pageNum = page.getNumber() + 1;
        this.totalElements = page.getTotalElements();
        this.content = page.getContent();
    }
    public PageData(){

    }
}
