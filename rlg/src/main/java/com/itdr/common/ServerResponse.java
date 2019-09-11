package com.itdr.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {
    private int status;
    private T data;
    private String msg;

    private ServerResponse(T data){
        this.data = data;
        this.status = 200;
    }
    private ServerResponse(String msg){
        this.msg = msg;
        this.status = 100;
    }
    private ServerResponse(int status,T data){
        this.status = status;
        this.data = data;
    }
    private ServerResponse(int status,String msg){
        this.status = status;
        this.msg = msg;
    }
    private ServerResponse(int status,T data,String msg){
        this.status = status;
        this.data = data;
        this.msg = msg;
    }


    public static <T> ServerResponse successRs(T data){
        return new ServerResponse(data);
    }

    public static <T> ServerResponse successRs(Integer status,T data){
        return new ServerResponse(status,data);
    }

    public static <T> ServerResponse successRs(Integer status,T data,String msg){
        return new ServerResponse(status,data,msg);
    }


/*    public static <T> ServerResponse successRs(Integer status,String msg){
        return new ServerResponse(status,msg);
    }

    public static <T> ServerResponse successRs(String msg){
        return new ServerResponse(msg);
    }*/

    public static <T> ServerResponse defeatedRs(Integer status,String msg){
        return new ServerResponse(status,msg);
    }

    public static <T> ServerResponse defeatedRs(String msg){
        return new ServerResponse(msg);
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

    @JsonIgnore
    public boolean isSuccess(){
        return this.status == 200;
    }
}
