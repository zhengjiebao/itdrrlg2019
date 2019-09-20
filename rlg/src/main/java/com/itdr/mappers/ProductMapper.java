package com.itdr.mappers;

import com.itdr.pojo.Category;
import com.itdr.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKeyWithBLOBs(Product record);

    int updateByPrimaryKey(Product record);

    List<Category> selectByCategoryId(Integer categoryId);

    Product selectByPid(@Param("pid") Integer pid,
                        @Param("is_new") Integer is_new,
                        @Param("is_hot") Integer is_hot,
                        @Param("is_banner") Integer is_banner);

    //根据商品ID或商品名称查询数据
    List<Product> selectByPidOrName(@Param("pid")Integer pid,
                                    @Param("keyword")String keyword,
                                    @Param("col")String col,
                                    @Param("order")String orderBy);
}