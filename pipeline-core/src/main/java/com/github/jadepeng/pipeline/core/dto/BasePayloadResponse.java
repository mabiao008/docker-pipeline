package com.github.jadepeng.pipeline.core.dto;

/**
 * 带payload的BaseResponse泛型类
 *
 * @param <T> 泛型类型
 */
public class BasePayloadResponse<T> extends BaseResponse {

  T data;

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public static <T> BasePayloadResponse<T> success() {
    BasePayloadResponse<T> response = new BasePayloadResponse<>();
    return response;
  }

  public static <T> BasePayloadResponse<T> success(T data) {
    BasePayloadResponse<T> response = new BasePayloadResponse<>();
    response.setData(data);
    return response;
  }

  public static <T> BasePayloadResponse<T> error(RetCode code) {
    BasePayloadResponse<T> response = new BasePayloadResponse<>();
    response.setRetcode(code.getCode());
    response.setDesc(code.getMsg());
    return response;
  }

  public static <T> BasePayloadResponse<T> error(String code, String error) {
    BasePayloadResponse<T> response = new BasePayloadResponse<>();
    response.setRetcode(code);
    response.setDesc(error);
    return response;
  }
}
