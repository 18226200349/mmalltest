package com.mmall.controller;

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
/**
 * 
* @ClassName: UserController  
* @Description: TODO  
* @author Administrator  
* @date 2018年4月11日  
*
 */
@Controller
@RequestMapping("/user/")
public class UserController {
	@Autowired
	private IUserService iUserService;
	/**
	 * 
	* @Title: login  
	* @Description: TODO 
	* @param @param username
	* @param @param password
	* @param @param session
	* @param @return    
	* @return Object      
	* @throws
	 */
	@RequestMapping(value="login.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login(String username,String password,HttpSession session){
		 ServerResponse<User> response=iUserService.login(username, password);
		 if(response.isSuccess()){
			 session.setAttribute(Const.CURRENT_USER, response.getData());
		 }
		 return response;
	}

}
