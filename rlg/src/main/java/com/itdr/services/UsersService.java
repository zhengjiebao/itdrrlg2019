package com.itdr.services;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;

public interface UsersService {
    //用户登录
    ServerResponse<Users> login(String username, String password);

    ServerResponse<Users> register(Users u);

    //检查用户是否有效
    ServerResponse<Users> checkValid(String str, String type);

    //获取当前用户详细信息
    ServerResponse getInforamtion(Users users);

    //更新当前用户信息
    ServerResponse updateInformation(Users u);

    //忘记密码
    ServerResponse<Users> forgetGetQuestion(String username);

    //提交问题答案
    ServerResponse<Users> forgetCheckAnswer(String username, String question, String answer);

    //忘记密码的重设密码
    ServerResponse<Users> forgetResetPassword(String username, String passwordNew, String forgetToken);

    //登录中状态重置密码
    ServerResponse<Users> resetPassword(Users users, String passwordOld, String passwordNew);
}
