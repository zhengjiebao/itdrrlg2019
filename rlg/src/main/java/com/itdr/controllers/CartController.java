package com.itdr.controllers;

import com.itdr.common.Const;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;
import com.itdr.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@ResponseBody
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    CartService cartService;

    @PostMapping("add.do")
    public ServerResponse addOne(Integer productId, Integer count, HttpSession session) {
        //购物车添加商品
       Users users = (Users) session.getAttribute("users");
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return cartService.addOne(productId,count,users.getId());
        }
    }

    //获取登录用户购物车列表
    @RequestMapping("list.do")
    public ServerResponse listCart(HttpSession session) {
        Users users = (Users) session.getAttribute("users");
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return cartService.listCart(users.getId());
        }
    }

    //购物车更新商品
    @RequestMapping("update.do")
    public ServerResponse updateCart(Integer productId, Integer count,HttpSession session) {
        Users users = (Users) session.getAttribute("users");
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return cartService.updateCart(productId,count,users.getId());
        }
    }

    //购物车删除商品
    @RequestMapping("delete_product.do")
    public ServerResponse deleteCart(String productIds,HttpSession session) {
        Users users = (Users) session.getAttribute("users");
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return cartService.deleteCart(productIds,users.getId());
        }
    }

    //查询在购物车里的商品信息条数
    @RequestMapping("get_cart_product_count.do")
    public ServerResponse getCartProductCount(HttpSession session) {
        Users users = (Users) session.getAttribute("users");
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return cartService.getCartProductCount(users.getId());
        }
    }

    //购物车全选
    @RequestMapping("select_all.do")
    public ServerResponse selectAll(HttpSession session,Integer check) {
        Users users = (Users) session.getAttribute("users");
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return cartService.selectOrUnSelect(users.getId(),check,null);
        }
    }

    //购物车取消全选
    @RequestMapping("un_select_all.do")
    public ServerResponse unSelectAll(HttpSession session,Integer check) {
        Users users = (Users) session.getAttribute("users");
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return cartService.selectOrUnSelect(users.getId(),check,null);
        }
    }

    //购物车选中某个商品
    @RequestMapping("select.do")
    public ServerResponse select(HttpSession session,Integer check,Integer productId) {
        Users users = (Users) session.getAttribute("users");
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return cartService.selectOrUnSelect(users.getId(),check,null);
        }
    }

    //购物车取消选中某个商品
    @RequestMapping("un_select.do")
    public ServerResponse unSelect(HttpSession session,Integer check,Integer productId) {
        Users users = (Users) session.getAttribute("users");
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return cartService.selectOrUnSelect(users.getId(),check,null);
        }
    }
}
