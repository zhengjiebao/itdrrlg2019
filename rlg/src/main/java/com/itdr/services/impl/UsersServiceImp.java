package com.itdr.services.impl;

import com.itdr.common.Const;
import com.itdr.common.ServerResponse;
import com.itdr.common.TokenCache;
import com.itdr.mappers.UsersMapper;
import com.itdr.pojo.Users;
import com.itdr.services.UsersService;
import com.itdr.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsersServiceImp implements UsersService {

    @Autowired
    UsersMapper usersMapper;

    //用户登录
    @Override
    public ServerResponse<Users> login(String username, String password) {
        if (username == null || username.equals("")){
            return ServerResponse.defeatedRs("用户名不能为空");
        }
        if (password == null || password.equals("")){
            return ServerResponse.defeatedRs("密码不能为空");
        }

        //根据用户名查找是否存在用户
        int i = usersMapper.selectByUsernameOrEmail(username, "username");
        if (i <= 0){
            return ServerResponse.defeatedRs("用户名不存在");
        }

        //MD5加密
        String md5Password = MD5Utils.getMD5Code(password);

        //根据用户名密码查找用户是否存在
        Users u = usersMapper.selectByUsernameAndPassword(username,md5Password);

        if (u == null){
            return ServerResponse.defeatedRs("账户或密码错误");
        }

        //封装数据并返回
        ServerResponse sr = ServerResponse.successRs(Const.SUCCESS,u);
        return sr;
    }

    @Override
    public ServerResponse<Users> register(Users u) {
        if (u.getUsername() == null || u.getUsername().equals("")){
            return ServerResponse.defeatedRs(Const.ERROR,"账户名不能为空");
        }
        if (u.getPassword() == null || u.getPassword().equals("")){
            return ServerResponse.defeatedRs(Const.ERROR,"密码不能为空");
        }

        int i2 = usersMapper.selectByUsernameOrEmail(u.getUsername(), "username");
        if (i2 > 0){
            return ServerResponse.defeatedRs(Const.ERROR,"注册的用户名已经存在");
        }

        //MD5加密
        u.setPassword(MD5Utils.getMD5Code(u.getPassword()));

        //检查注册用户名是否存在
        int i = usersMapper.insert(u);
        if (i <= 0){
            return ServerResponse.defeatedRs(Const.ERROR,"用户注册失败");
        }
        return ServerResponse.successRs(Const.SUCCESS,null,"用户注册成功");
    }

    //检查用户是否有效
    @Override
    public ServerResponse<Users> checkValid(String str, String type) {
        if (str == null || str.equals("")){
            return ServerResponse.defeatedRs(Const.ERROR,"参数不能为空");
        }
        if (type == null || type.equals("")){
            return ServerResponse.defeatedRs(Const.ERROR,"参数类型不能为空");
        }

        int i = usersMapper.selectByUsernameOrEmail(str,type);
        if (i > 0 && type.equals("username")){
            return ServerResponse.defeatedRs(Const.ERROR,"用户名已经存在");
        }
        if (i > 0 && type.equals("email")){
            return ServerResponse.defeatedRs(Const.ERROR,"邮箱已经存在");
        }
        return ServerResponse.successRs(Const.SUCCESS,null,"校验成功");
    }

    @Override
    public ServerResponse getInforamtion(Users users) {
        Users users1 = usersMapper.selectByPrimaryKey(users.getId());
        if (users1 == null){
            return ServerResponse.defeatedRs(Const.ERROR,"用户不存在");
        }
        users1.setPassword("");
        return ServerResponse.successRs(Const.SUCCESS,users1);
    }

    //更新当前用户信息
    @Override
    public ServerResponse updateInformation(Users u) {
        int i2 = usersMapper.selectByEmailAndId(u.getEmail(),u.getId());
        if (i2 > 0){
            return ServerResponse.defeatedRs(Const.ERROR,"要更新的邮箱已经存在");
        }
        int i = usersMapper.updateByPrimaryKeySelective(u);
        if (i <= 0){
            return ServerResponse.defeatedRs(Const.ERROR,"更新失败");
        }
        return ServerResponse.successRs(Const.SUCCESS,"更新成功");
    }

    //忘记密码
    @Override
    public ServerResponse<Users> forgetGetQuestion(String username) {
        if (username == null || username.equals("")){
            return ServerResponse.defeatedRs(Const.ERROR,"参数不能为空");
        }
        int i = usersMapper.selectByUsernameOrEmail(username, Const.USERNAME);
        if (i <= 0){
            return ServerResponse.defeatedRs(Const.ERROR,"用户名不存在");
        }
        String question = usersMapper.selectByUsername(username);
        if (question == null || "".equals(question)){
            return ServerResponse.defeatedRs(Const.ERROR,"该用户未设置找回密码问题");
        }
        return ServerResponse.successRs(Const.SUCCESS,question);
    }

    //提交问题答案
    @Override
    public ServerResponse<Users> forgetCheckAnswer(String username, String question, String answer) {
        //参数是否为空
        if (username == null || username.equals("")){
            return ServerResponse.defeatedRs(Const.ERROR,"用户名不能为空");
        }
        if (question == null || question.equals("")){
            return ServerResponse.defeatedRs(Const.ERROR,"问题不能为空");
        }
        if (answer == null || answer.equals("")){
            return ServerResponse.defeatedRs(Const.ERROR,"答案不能为空");
        }

        int i = usersMapper.selectByUsernameAndQuestionAndAnswer(username,question,answer);
        if (i <= 0){
            return ServerResponse.defeatedRs(Const.ERROR,"问题答案错误");
        }

        //产生随机字符令牌
        String token = UUID.randomUUID().toString();
        //把令牌放入缓存中，使用Google guova缓存，后期使用Redis
        TokenCache.set("token_"+username,token);

        return ServerResponse.successRs(Const.SUCCESS,token);
    }

    @Override
    public ServerResponse<Users> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (username == null || username.equals("")){
            return ServerResponse.defeatedRs(Const.ERROR,"用户名不能为空");
        }
        if (passwordNew == null || passwordNew.equals("")){
            return ServerResponse.defeatedRs(Const.ERROR,"新密码不能为空");
        }
        if (forgetToken == null || forgetToken.equals("")){
            return ServerResponse.defeatedRs(Const.ERROR,"非法的令牌参数");
        }

        //判断缓存中的token
        String token = TokenCache.get("token_" + username);
        if (token == null || token.equals("")){
            return ServerResponse.defeatedRs(Const.ERROR,"token过期了");
        }
        if (!token.equals(forgetToken)){
            return ServerResponse.defeatedRs(Const.ERROR,"非法的token");
        }

        String md5passwordNew = MD5Utils.getMD5Code(passwordNew);

        int i = usersMapper.updateByUsernameAndPassword(username, passwordNew);
        if (i <= 0){
            return ServerResponse.defeatedRs("修改密码失败");
        }
        return ServerResponse.successRs("修改密码成功");
    }

    @Override
    public ServerResponse<Users> resetPassword(Users users, String passwordOld, String passwordNew) {
        if (passwordOld == null || passwordOld.equals("")){
            return ServerResponse.defeatedRs(Const.ERROR,"参数不能为空");
        }
        if (passwordNew == null || passwordNew.equals("")){
            return ServerResponse.defeatedRs(Const.ERROR,"参数不能为空");
        }

        String md5passwordOld = MD5Utils.getMD5Code(passwordOld);

        int i = usersMapper.selectByIdAndPassword(users.getId(),md5passwordOld);
        if (i<= 0){
            return ServerResponse.defeatedRs("旧密码输入错误");
        }

        String md5passwordNew = MD5Utils.getMD5Code(passwordNew);

        int i2 = usersMapper.updateByUsernameAndPassword(users.getUsername(), md5passwordNew);
        if (i2 <= 0){
            return ServerResponse.defeatedRs("修改密码失败");
        }
        return ServerResponse.successRs("修改密码成功");

    }
}
