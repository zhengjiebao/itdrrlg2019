package com.itdr.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> {
    private int status;
    private T data;
    private String msg;

    public ServerResponse(){};
    public ServerResponse(int status){
        this.status = status;
    }
    public ServerResponse(int status,T data){
        this.status = status;
        this.data = data;
    }
    public ServerResponse(int status,String msg){
        this.status = status;
        this.msg = msg;
    }
    public ServerResponse(int status,T data,String msg){
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public static ServerResponse serverResponseBySuccess(){
        return new ServerResponse(ResponseCode.SUCCESS);
    }

    public static <T> ServerResponse serverResponseBySuccess(T data){
        return new ServerResponse(ResponseCode.SUCCESS,data);
    }

    public static <T> ServerResponse serverResponseBySuccess(T data,String msg){
        return new ServerResponse(ResponseCode.SUCCESS,data,msg);
    }

    public static ServerResponse serverResponseByError(){
        return new ServerResponse(ResponseCode.ERROR);
    }

    public static ServerResponse serverResponseByError(String msg){
        return new ServerResponse(ResponseCode.ERROR,msg);
    }

    public static ServerResponse serverResponseByError(int status){
        return new ServerResponse(status);
    }

    public static ServerResponse serverResponseByError(int status,String msg){
        return new ServerResponse(status,msg);
    }

    @JsonIgnore
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
