package com.github.jadepeng.pipeline.core.dto;

import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class BaseRequest {
  /**
   * 工作流名称
   */
  private String name;

  @Min(value = 1, message = "pageIndex必须为正整数")
  private Integer pageIndex = 1;

  @Min(value = 1, message = "pageSize必须为正整数")
  private Integer pageSize = 10;
}
