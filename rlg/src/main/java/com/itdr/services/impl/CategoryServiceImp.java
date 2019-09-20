package com.itdr.services.impl;

import com.itdr.common.ServerResponse;
import com.itdr.mappers.CategoryMapper;
import com.itdr.pojo.Category;
import com.itdr.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ServerResponse getDeepCategoryDo(Integer categoryId) {
        if (categoryId == null || categoryId < 0){
            return ServerResponse.defeatedRs("非法的参数");
        }
        List<Integer> li = new ArrayList<>();
        li.add(categoryId);
        getAll(categoryId,li);
        ServerResponse sr = ServerResponse.successRs(li);
        return sr;
    }

    @Override
    public ServerResponse<Category> setCategoryName(Integer cid, String name) {
        if (cid == null){
            return ServerResponse.defeatedRs(100,"ID不能为空");
        }
        if (name == null || name.equals("")){
            return ServerResponse.defeatedRs(100,"名称不能为空");
        }

        int i = categoryMapper.updateByName(cid,name);
        if (i <= 0){
            return ServerResponse.defeatedRs(100,"更新失败");
        }
        return ServerResponse.successRs(200,"更新成功");
    }

    @Override
    public ServerResponse<Category> addCategory(Category category) {
        if (category.getName() == null || category.getName().equals("")){
            return ServerResponse.defeatedRs(100,"品类名称不能为空");
        }
/*        if (category.getParentId() == null){
            return ServerResponse.defeatedRs(100,"品类父ID不能为空");
        }*/

        int i = categoryMapper.insert(category);
        if (i <= 0){
            return ServerResponse.defeatedRs(100,"增加品类失败");
        }
        return ServerResponse.successRs(200,"增加品类成功");
    }

    @Override
    public ServerResponse<Category> getCategory(Integer cid) {
        Category category = categoryMapper.selectByPrimaryKey(cid);
        if (category == null){
            return ServerResponse.defeatedRs(100,"该品类不存在");
        }
        return ServerResponse.successRs(200,category);
    }


    private void getAll(Integer pid, List<Integer> list){
        List<Category> li = categoryMapper.seleteByParentId(pid);

        if (li != null && li.size() != 0){
            for (Category category : li) {
                list.add(category.getId());
                getAll(category.getId(),list);
            }
        }
    }
}
