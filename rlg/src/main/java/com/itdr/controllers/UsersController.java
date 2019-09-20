package com.itdr.controllers;

import com.itdr.common.Const;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;
import com.itdr.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@ResponseBody
@RequestMapping("/user/")
public class UsersController {

    @Autowired
    UsersService usersService;

    //用户登录
    @RequestMapping("login.do")
    public ServerResponse<Users> login(String username, String password, HttpSession session){
        ServerResponse<Users> sr = usersService.login(username,password);
        Users u = sr.getData();
        session.setAttribute("users",u);

        if (sr.isSuccess()) {
            Users u2 = new Users();
            u2.setId(u.getId());
            u2.setUsername(u.getUsername());
            u2.setEmail(u.getEmail());
            u2.setPhone(u.getPhone());
            u2.setCreateTime(u.getCreateTime());
            u2.setUpdateTime(u.getUpdateTime());
            u2.setPassword("");

            sr.setData(u2);
        }
        return sr;
    }

    //用户注册
    @RequestMapping("register.do")
    public ServerResponse<Users> register(Users u){
        ServerResponse<Users> sr = usersService.register(u);
        return sr;
    }

    //检查用户是否有效
    @RequestMapping("check_valid.do")
    public ServerResponse<Users> checkValid(String str,String type){
        ServerResponse<Users> sr = usersService.checkValid(str,type);
        return sr;
    }

    //获取当前用户信息
    @RequestMapping("get_user_info.do")
    public ServerResponse<Users> getUsersInfo(HttpSession session){
        Users users = (Users) session.getAttribute("users");
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return ServerResponse.successRs(users);
        }
    }

    //退出登录
    @RequestMapping("logout.do")
    public ServerResponse<Users> logout(HttpSession session){
        session.removeAttribute("users");
        return ServerResponse.successRs("退出成功");
    }

    //获取当前用户详细信息
    @RequestMapping("get_inforamtion.do")
    public ServerResponse<Users> getInforamtion(HttpSession session){
        Users users = (Users) session.getAttribute("users");
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            ServerResponse sr = usersService.getInforamtion(users);
            return sr;
        }
    }

    //更新当前用户信息
    @RequestMapping("update_information.do")
    public ServerResponse<Users> updateInformation(HttpSession session,Users u){
        Users users = (Users) session.getAttribute("users");
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            u.setId(users.getId());
            u.setUsername(users.getUsername());
            ServerResponse sr = usersService.updateInformation(u);
            session.setAttribute("users",u);
            return sr;
        }
    }

    //忘记密码
    @RequestMapping("forget_get_question.do")
    public ServerResponse<Users> forgetGetQuestion(String username){
        return usersService.forgetGetQuestion(username);
    }

    //提交问题答案
    @RequestMapping("forget_check_answer.do")
    public ServerResponse<Users> forgetCheckAnswer(String username,String question,String answer){
        return usersService.forgetCheckAnswer(username,question,answer);
    }

    //忘记密码的重设密码
    @RequestMapping("forget_reset_password.do")
    public ServerResponse<Users> forgetResetPassword(String username,String passwordNew,String forgetToken){
        return usersService.forgetResetPassword(username,passwordNew,forgetToken);
    }

    //登录中状态重置密码
    @RequestMapping("reset_password.do")
    public ServerResponse<Users> resetPassword(String passwordOld,String passwordNew,HttpSession session){
        Users users = (Users) session.getAttribute("users");
        if (users == null){
            return ServerResponse.defeatedRs("用户未登录");
        }else {
            return usersService.resetPassword(users,passwordOld,passwordNew);
        }
    }
}
