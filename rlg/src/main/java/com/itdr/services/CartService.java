package com.itdr.services;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.vo.CartVo;

public interface CartService {
    //购物车添加商品
    ServerResponse addOne(Integer productId, Integer count, Integer uid);

    //获取登录用户购物车列表
    ServerResponse<CartVo> listCart(Integer id);

    //购物车更新商品
    ServerResponse<CartVo> updateCart(Integer productId, Integer count, Integer id);

    //购物车删除商品
    ServerResponse<CartVo> deleteCart(String productIds, Integer id);

    //查询在购物车里的商品信息条数
    ServerResponse<Integer> getCartProductCount(Integer id);

    //改变购物车商品选中状态
    ServerResponse<CartVo> selectOrUnSelect(Integer id,Integer check,Integer productId);
}
