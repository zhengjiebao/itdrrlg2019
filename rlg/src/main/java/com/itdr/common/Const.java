package com.itdr.common;

public class Const {

    public static final String CURRENTUSER = "current_user";
    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";
    public static final String AUTOLOGINTOKEN = "AUTOLOGINTOKEN";
    public static final String JESSESSIONID = "JESSESSIONID";
    public static final String EMAIL = "EMAIL";
    public static final String USERNAME = "USERNAME";


    public static final int SUCCESS = 0;
    public static final int ERROR = 100;

    public interface Cart{
        String LIMITQUANTITYSUCCESS="LIMIT_NUM_SUCCESS";
        String LIMITQUANTITYFAILED="LIMIT_NUM_FAILED";
        Integer CHECK=1;
        Integer UNCHECK=0;
    }

    public enum UsersEnum{
        NEED_LOGIN(2,"需要登录"),
        NO_LOGIN(101,"用户未登录"),
        NULL_USERNAME(102,"用户名不能为空"),
        NULL_PASSWORD(103,"密码不能为空"),
        NO_USER(104,"用户不存在"),
        WRONG_LOGIN(105,"用户名或密码错误"),
        ALREADY_REGISTER(110,"注册的用户名已经存在"),
        DEFEAT_REGISTER(111,"用户注册失败"),
        NULL_PARAMETER(120,"参数不能为空"),
        NULL_PARAMETERTYPE(121,"参数类型不能为空"),
        ALREADY_USER(123,"用户名已经存在"),
        ALREADY_EMAIL(124,"邮箱已经存在"),
        DEFEAT_UPDATE(130,"更新失败"),
        NO_QUESTION(140,"用户未设置找回密码问题"),
        NULL_QUESTION(150,"答案不能为空"),
        NULL_ANSWER(151,"答案不能为空"),
        WRONG_ANSWER(152,"问题答案错误"),
        NULL_NEWPASSWORD(160,"新密码不能空"),
        WRONG_TOKEN(161,"非法的令牌参数"),
        OVER_TOKEN(162,"token已经过期"),
        ERROR_TOKEN(163,"非法的token"),
        DEFEAT_RESTPASSWORD(164,"修改密码失败"),
        WRONG_OLDPASSWORD(170,"旧密码输入错误");

        private int code;
        private String desc;

        private UsersEnum(int code,String desc){
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

}
