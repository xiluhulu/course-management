package com.cccc.resp;

import lombok.Data;

/**
 * @author xyl
 * @date 2020-12-10 18:01
 * @desc 响应客户端结果实体类
 */
@Data
public class ResponseResult<T> {

    /**
     * 响应状态码
     */
    private Integer code;
    /**
     * 响应描述
     */
    private String desc;
    /**
     * 携带的其他数据
     */
    private T data;

    public ResponseResult() {
        this.code = ResponseStatusCode.SUCCESS.getCode();
        this.desc = ResponseStatusCode.SUCCESS.getDesc();
    }

    public ResponseResult(T data) {
        this.code = ResponseStatusCode.SUCCESS.getCode();
        this.desc = ResponseStatusCode.SUCCESS.getDesc();
        this.data = data;
    }

    public ResponseResult(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public ResponseResult(Integer code, String desc, T data) {
        this.code = code;
        this.desc = desc;
        this.data = data;
    }

    public ResponseResult(ResponseStatusCode responseStatusCode){
        this.code = responseStatusCode.getCode();
        this.desc = responseStatusCode.getDesc();
    }

    public ResponseResult(ResponseStatusCode responseStatusCode,T data){
        this.code = responseStatusCode.getCode();
        this.desc = responseStatusCode.getDesc();
        this.data = data;
    }
}
