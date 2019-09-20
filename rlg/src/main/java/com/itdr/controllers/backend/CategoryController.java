package com.itdr.controllers.backend;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Category;
import com.itdr.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@ResponseBody
@RequestMapping("/manage/category/")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @RequestMapping("get_deep_category.do")
    public ServerResponse getDeepCategoryDo(Integer categoryId) {
        ServerResponse sr = categoryService.getDeepCategoryDo(categoryId);
        return sr;
    }

    @RequestMapping("set_category_name.do")
    public ServerResponse<Category> setCategoryName(Integer cid,String name) {
        ServerResponse<Category> sr = categoryService.setCategoryName(cid,name);
        return sr;
    }

    @RequestMapping("add_category.do")
    public ServerResponse<Category> addCategory(Category category) {
        ServerResponse<Category> sr = categoryService.addCategory(category);
        return sr;
    }

    @RequestMapping("get_category.do")
    public ServerResponse<Category> getCategory(Integer cid) {
        ServerResponse<Category> sr = categoryService.getCategory(cid);
        return sr;
    }
}
