package com.itdr.services.impl;

import com.itdr.common.Const;
import com.itdr.common.ServerResponse;
import com.itdr.mappers.CartMapper;
import com.itdr.mappers.ProductMapper;
import com.itdr.pojo.Cart;
import com.itdr.pojo.Product;
import com.itdr.pojo.vo.CartProductVo;
import com.itdr.pojo.vo.CartVo;
import com.itdr.services.CartService;
import com.itdr.utils.BigDecimalUtil;
import com.itdr.utils.PoToVoUtil;
import com.itdr.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CartServiceImp implements CartService {

    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductMapper productMapper;

    //购物车添加商品
    @Override
    public ServerResponse<CartVo> addOne(Integer productId, Integer count, Integer uid) {
        //参数非空判断
        if (productId == null || productId <= 0 || count == null || count <= 0) {
            return ServerResponse.defeatedRs("非法参数");
        }

        //如果有这条购物信息，就更新商品数量,如果没有，就添加新的购物车信息
        Cart c2 = cartMapper.selectByUidAndProductId(uid, productId);

        if (c2 != null) {
            //更新数据
            c2.setQuantity(c2.getQuantity() + count);
            int i = cartMapper.updateByPrimaryKeySelective(c2);
        } else {
            //插入新数据
            Cart c = new Cart();
            c.setUserId(uid);
            c.setProductId(productId);
            c.setQuantity(count);
            int insert = cartMapper.insert(c);
        }

        return listCart(uid);

    }

    //获取登录用户购物车列表
    @Override
    public ServerResponse<CartVo> listCart(Integer id) {
        CartVo cartVo = this.getCartVo(id);
        return ServerResponse.successRs(cartVo);
    }

    //购物车更新商品
    @Override
    public ServerResponse<CartVo> updateCart(Integer productId, Integer count, Integer id) {
        //参数非空判断
        if (productId == null || productId <= 0 || count == null || count <= 0) {
            return ServerResponse.defeatedRs("非法参数");
        }

        //如果有这条购物信息，就更新商品数量
        //如果没有，就添加新的购物车信息
        Cart c2 = cartMapper.selectByUidAndProductId(id, productId);
        //更新数据
        c2.setQuantity(c2.getQuantity() + count);
        int i = cartMapper.updateByPrimaryKeySelective(c2);

        return listCart(id);
    }

    //购物车删除商品
    @Override
    public ServerResponse<CartVo> deleteCart(String productIds, Integer id) {
        if (productIds == null || productIds.equals("")){
            return ServerResponse.defeatedRs("非法参数");
        }

        //把字符串中的数据放到集合中
        String[] split = productIds.split(",");
        List<String> strings = Arrays.asList(split);

        int i = cartMapper.deleteByProducts(strings,id);
        return listCart(id);
    }

    //查询在购物车里的商品信息条数
    @Override
    public ServerResponse<Integer> getCartProductCount(Integer id) {
        List<Cart> carts = cartMapper.selectByUid(id);
        return ServerResponse.successRs(carts.size());
    }

    //改变购物车商品选中状态
    @Override
    public ServerResponse<CartVo> selectOrUnSelect(Integer id,Integer check,Integer productId) {
        int i = cartMapper.selectOrUnSelect(id,check,productId);
        return listCart(id);
    }


    private CartVo getCartVo(Integer uid) {
        //创建CartVo对象
        CartVo cartVo = new CartVo();

        //创建变量存储购物车总价
        BigDecimal cartTotalPrice = new BigDecimal("0");

        //存放CartProductVo对象的集合
        List<CartProductVo> cartProductVoList = new ArrayList<CartProductVo>();

        //根据用户ID查询该用户的所有购物车信息
        List<Cart> liCart = cartMapper.selectByUid(uid);

        //从购物信息集合中拿出每一条数据，根据其中的商品ID查询需要的商品信息
        if (liCart.size() != 0) {
            for (Cart cart : liCart) {
                //根据购物信息中的商品ID查询商品数据
                Product p = productMapper.selectByPid(cart.getProductId(),0,0,0);
                //使用工具类进行数据的封装
                CartProductVo cartProductVo = PoToVoUtil.getOne(cart,p);

                //计算购物车总价
                if (cart.getChecked() == Const.Cart.CHECK){
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }

                //把对象放到集合中
                cartProductVoList.add(cartProductVo);
            }
        }
        //封装CartVo数据
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(this.checkAll(uid));
        cartVo.setCartTotalPrice(cartTotalPrice);

        try {
            cartVo.setImageHost(PropertiesUtil.getValue("imageHost"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cartVo;
    }

    //判断用户购物车是否全选
    private boolean checkAll(Integer uid){
        int i = cartMapper.selectByUidCheck(uid,Const.Cart.UNCHECK);
        if (i == 0){
            return true;
        }else {
            return false;
        }
    }
}
