package com.itdr.controllers;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Product;
import com.itdr.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    ProductService productService;

    //获取商品分类信息
    @RequestMapping("topcategory.do")
    public ServerResponse<Product> topCategory(Integer categoryId) {
        ServerResponse sr = productService.topCategory(categoryId);
        return sr;
    }

    //获取商品详情
    @RequestMapping("detail.do")
    public ServerResponse<Product> detail(Integer pid,
                                          @RequestParam(value = "is_new", required = false, defaultValue = "0") Integer is_new,
                                          @RequestParam(value = "is_hot", required = false, defaultValue = "0") Integer is_hot,
                                          @RequestParam(value = "is_banner", required = false, defaultValue = "0") Integer is_banner) {
        ServerResponse sr = productService.detail(pid,is_new,is_hot,is_banner);
        return sr;
    }

    @RequestMapping("list.do")
    public ServerResponse<Product> listProduct(Integer pid, String keyword,
                                               @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                               @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize,
                                               @RequestParam(value = "orderBy",required = false,defaultValue = "")String orderBy) {
        ServerResponse sr = productService.listProduct(pid,keyword,pageNum,pageNum,orderBy);
        return sr;
    }
}
