package com.itdr.services.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itdr.common.ServerResponse;
import com.itdr.mappers.CategoryMapper;
import com.itdr.mappers.ProductMapper;
import com.itdr.pojo.Category;
import com.itdr.pojo.Product;
import com.itdr.pojo.vo.ProductVo;
import com.itdr.services.ProductService;
import com.itdr.utils.PoToVoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ServerResponse<Product> topCategory(Integer categoryId) {
        if (categoryId == null || categoryId < 0){
            return ServerResponse.defeatedRs(100,"非法的参数");
        }

        List<Category> product =  categoryMapper.seleteByParentId(categoryId);

        if (product == null){
            return ServerResponse.defeatedRs(100,"查询的ID不存在");
        }
        if (product.size() == 0){
            return ServerResponse.defeatedRs(100,"没有该子分类");
        }

        return ServerResponse.successRs(product);
    }


    @Override
    public ServerResponse detail(Integer pid, Integer is_new, Integer is_hot, Integer is_banner) {
        if (pid == null || pid < 0){
            return ServerResponse.defeatedRs(100,"非法的参数");
        }

        Product p = productMapper.selectByPid(pid,is_new,is_hot,is_banner);

        if (p == null){
            return ServerResponse.defeatedRs("商品不存在");
        }

        ProductVo productVo = null;
        try {
             productVo = PoToVoUtil.productToProductVo(p);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ServerResponse.successRs(productVo);
    }

    @Override
    public ServerResponse listProduct(Integer pid, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        if ((pid == null || pid < 0) && (keyword == null || keyword.equals(""))){
            return ServerResponse.defeatedRs("非法参数");
        }

        String[] split = new String[2];
        //分割排序参数
        if (!orderBy.equals("")){
            split = orderBy.split("_");
        }

        String keys = "%"+keyword+"%";
        PageHelper.startPage(pageNum,pageSize);
        List<Product> li = productMapper.selectByPidOrName(pid,keys,split[0],split[1]);
        PageInfo pf = new PageInfo(li);

        return ServerResponse.successRs(pf);
    }
}
