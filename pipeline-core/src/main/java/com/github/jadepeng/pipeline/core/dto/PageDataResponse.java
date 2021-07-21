package com.github.jadepeng.pipeline.core.dto;

import java.util.List;

import org.springframework.data.domain.Page;

public class PageDataResponse<T> extends BaseResponse {
  private long total;
  private int page;
  private int size;
  private List<T> items;

  public PageDataResponse() {
  }

  public PageDataResponse(Page<T> page) {
    this.total = page.getTotalElements();
    this.page = page.getPageable().getPageNumber();
    this.size = page.getPageable().getPageSize();
    this.items = page.getContent();
  }

  public PageDataResponse(List<T> list) {
    this.total = list.size();
    this.items = list;
  }

  public PageDataResponse(int size, long total) {
    this.total = total;
    this.size = size;
  }

  public PageDataResponse(int page, int size, long total) {
    this.size = size;
    this.total = total;
    this.page = page;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
  }

  public List<T> getItems() {
    return items;
  }

  public void setItems(List<T> items) {
    this.items = items;
  }
}
