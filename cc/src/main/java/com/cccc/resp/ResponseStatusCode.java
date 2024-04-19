package com.cccc.resp;

/**
 * @author xyl
 * @date 2020-12-10 17:50
 * @desc 响应状态码
 */
public enum ResponseStatusCode {

    /**
     *
     */
    SUCCESS(200,"请求成功"),
    ERROR(500,"服务器异常"),
    URL_ERROR(404,"路径有误"),
    LOGIN_FAILED(10000,"登录失败"),
    NO_AUTHORITY(10010,"无权限");
    /**
     * 响应状态码
     */
    private Integer code;
    /**
     * 响应结果描述
     */
    private String desc;

    ResponseStatusCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
