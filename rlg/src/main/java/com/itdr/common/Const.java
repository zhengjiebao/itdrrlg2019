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

    public enum UsersEnum{
        NEED_LOGIN(2,"需要登录"),
        NO_LOGIN(101,"用户未登录");

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
