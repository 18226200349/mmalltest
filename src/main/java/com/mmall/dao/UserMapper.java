package com.mmall.dao;

import org.apache.ibatis.annotations.Param;

import com.mmall.pojo.User;
/**
 * usermapper实体类
* @ClassName: UserMapper  
* @Description: TODO  
* @author Administrator  
* @date 2018年4月11日  
*
 */
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    int checkUsername(String username);
    
    User selectLogin(@Param("username")String username,@Param("password")String password);
    
    int checkEmail(@Param("email") String email);
    
    String findQuestionByUsername(String username);
    
    int checkAnswer(@Param("username")String username,@Param("question")String question,@Param("answer")String answer);
    
    int updatePasswordByUsername(@Param("username") String username,@Param("passwordNew") String passwordNew);
    
    int checkPassword(@Param("password")String password,@Param("userId")Integer userId);
    
    int checkEmailByUserId(@Param("email")String email,@Param("userId")Integer userId);
}
