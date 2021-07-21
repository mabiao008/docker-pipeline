package com.github.jadepeng.pipeline.service;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jadepeng.pipeline.core.dto.RetCode;

public class PipelineException extends RuntimeException {
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 291977197110422770L;
  /**
   * 错误码
   */
  private RetCode retCode = RetCode.UNKNOWN_ERROR;
  /**
   * 错误详情
   */
  private String errorInfo;

  private Logger LOGGER = LoggerFactory.getLogger(PipelineException.class);

  public RetCode getRetCode() {
    return retCode;
  }

  public String getErrorInfo() {
    return errorInfo;
  }

//    public void setRetCode(RetCode retCode) {
//      this.retCode = retCode;
//    }

  public void setErrorInfo(String errorInfo) {
    this.errorInfo = errorInfo;
  }

  public PipelineException(RetCode retCode, String errorInfo) {
    super(errorInfo);
    this.retCode = retCode;
    this.errorInfo = errorInfo;
  }

  public PipelineException(String errorInfo) {
    super(errorInfo);
    this.errorInfo = errorInfo;
  }


  public PipelineException(RetCode retCode) {
    this.retCode = retCode;
  }

  public PipelineException(Throwable e) {
    super(e);
    LOGGER.error(ExceptionUtils.getStackTrace(e));
  }

  public PipelineException(RetCode retCode, Throwable e) {
    super(e);
    this.retCode = retCode;
    LOGGER.error(ExceptionUtils.getStackTrace(e));
  }
}
