package com.cccc.common;

public class R {
    private int code;// 成功：200, 失败:500
    private String msg;// 消息提示
    private String status;// 要么成功，要么失败
    private Object data;// 返回数据
    public static final String SUCCESS="success";
    public static final String FAIL="fail";
    public R(int code, String msg, String status, Object data) {
        this.code = code;
        this.msg = msg;
        this.status = status;
        this.data = data;
    }
    public static R ok(){
        return new R(200,"",SUCCESS,null);
    }
    public static R ok(String msg){
        return new R(200,msg,SUCCESS,null);
    }
    public static R ok(Object data) {
        return new R(200, "", SUCCESS, data);
    }
    public static R ok(Object data,String msg){
        return new R(200,msg,SUCCESS,data);
    }

    public static R error() {
        return new R(500, "", FAIL, null);
    }

    public static R error(String msg) {
        return new R(500, msg, FAIL, null);
    }
    public static R error(int code) {
        return new R(code, "", FAIL, null);
    }
    public static R error(int code,String msg) {
        return new R(code, msg, FAIL, null);
    }
    public static R error(int code,Object data,String msg) {
        return new R(code, msg, FAIL, data);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
