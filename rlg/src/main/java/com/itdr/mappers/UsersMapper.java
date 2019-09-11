package com.itdr.mappers;

import com.itdr.pojo.Users;
import org.apache.ibatis.annotations.Param;

public interface UsersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);

    Users selectByUsernameAndPassword(@Param("username") String username,@Param("password") String password);

    int selectByUsernameOrEmail(@Param("str") String str,@Param("type") String type);

    int selectByEmailAndId(@Param("email") String email,@Param("id") Integer id);

    String selectByUsername(String username);

    int selectByUsernameAndQuestionAndAnswer(@Param("username") String username,@Param("question") String question,@Param("answer") String answer);

    int updateByUsernameAndPassword(@Param("username") String username,@Param("passwordNew") String passwordNew);

    int selectByIdAndPassword(@Param("id") Integer id,@Param("passwordOld") String passwordOld);
}