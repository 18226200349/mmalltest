package com.mmall.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;

@Controller
@RequestMapping("/mamage/user")
public class UserManngeController {
   @Autowired
   private IUserService iUserService;
   /**
    * 后台登录
   * @Title: login  
   * @Description: TODO 
   * @param @param username
   * @param @param password
   * @param @param session
   * @param @return    
   * @return ServerResponse<User>      
   * @throws
    */
   @ResponseBody
   @RequestMapping(value="login.do",method=RequestMethod.POST)
   public ServerResponse<User> login(String username,String password,HttpSession session){
	   ServerResponse<User> response=iUserService.login(username, password);
	   if(response.isSuccess()){
		   User user=response.getData();
		   if(user.getRole()==Const.Role.ROLE_ADMIN){
			   //登录的是管理员
			   session.setAttribute(Const.CURRENT_USER, user);
			   return response;
		   }
		   else{
			   return ServerResponse.createByErrorMessage("不是管理员,无法登录");
		   }
	   }
	return response;
	   
   }
}
