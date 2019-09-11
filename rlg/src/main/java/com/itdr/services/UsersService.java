package com.itdr.services;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;

public interface UsersService {
    //用户登录
    ServerResponse<Users> login(String username, String password);

    ServerResponse<Users> register(Users u);

    ServerResponse<Users> checkValid(String str, String type);

    ServerResponse getInforamtion(Users users);

    ServerResponse updateInformation(Users u);

    ServerResponse<Users> forgetGetQuestion(String username);

    ServerResponse<Users> forgetCheckAnswer(String username, String question, String answer);

    //忘记密码的重设密码
    ServerResponse<Users> forgetResetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse<Users> resetPassword(Users users, String passwordOld, String passwordNew);
}
