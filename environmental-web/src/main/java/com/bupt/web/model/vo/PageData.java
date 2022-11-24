package com.bupt.web.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

@Data
/**
 * 对spring提供的page类，进行了定制化封装
 */
public class PageData<T> {
    private long pageNum; // 页数
    private long pageSize; // 每页大小
    private long totalPages; // 总页数
    private long totalElements; // 总数据数
    private List<T> content;

    public PageData(Page<T> page) {
        this.totalPages = page.getPages();
        this.pageSize = page.getSize();
        this.pageNum = page.getCurrent();
        this.totalElements = page.getTotal();
        this.content = page.getRecords();
    }
}
