package com.github.jadepeng.pipeline.core.dto;

public class BaseResponse {

  private String retcode = RetCode.SUCCESS.getCode();

  private String desc = RetCode.SUCCESS.getMsg();

  public BaseResponse() {
    super();
  }


  public BaseResponse(String retCode, String desc) {
    super();
    this.retcode = retCode;
    this.desc = desc;
  }

  public String getRetcode() {
    return retcode;
  }

  public void setRetcode(String retcode) {
    this.retcode = retcode;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }
}
