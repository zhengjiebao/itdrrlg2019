package com.itdr.services;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Category;

public interface CategoryService{
    ServerResponse getDeepCategoryDo(Integer categoryId);

    ServerResponse<Category> setCategoryName(Integer cid, String name);

    ServerResponse<Category> addCategory(Category category);

    ServerResponse<Category> getCategory(Integer cid);
}
