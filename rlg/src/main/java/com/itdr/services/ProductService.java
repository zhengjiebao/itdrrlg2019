package com.itdr.services;

import com.itdr.common.ServerResponse;


public interface ProductService {
    ServerResponse topCategory(Integer categoryId);

    ServerResponse detail(Integer pid, Integer is_new, Integer is_hot, Integer is_banner);

    ServerResponse listProduct(Integer pid, String keyword, Integer pageNum, Integer pageSize, String orderBy);
}
