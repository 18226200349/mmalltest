package com.mmall.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
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
	* 
	* @Description: TODO登录模块 
	* @param @param username
	* @param @param password
	* @param @param session
	* @param @return    
	* @return Object      
	* @throws
	 */

   @ResponseBody
   @RequestMapping(value="login.do",method=RequestMethod.POST)
	public ServerResponse<User> login(String username,String password,HttpSession session){
		 ServerResponse<User> response=iUserService.login(username, password);
		 if(response.isSuccess()){
			 session.setAttribute(Const.CURRENT_USER, response.getData());
		 }
		 return response;
	}
	/**
	 * 
	* @Title: logout  
	* @Description: TODO 登出模块
	* @param @param session
	* @param @return    
	* @return ServerResponse<String>      
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value="logout.do",method=RequestMethod.POST)
	public ServerResponse<String> logout(HttpSession session){
		session.removeAttribute(Const.CURRENT_USER);
		return ServerResponse.createBySuccess();
		
	}
	/**
	 * 
	* @Title: register  
	* @Description: TODO 用户注册
	* @param @param user
	* @param @return    
	* @return ServerResponse<String>      
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value="register.do",method=RequestMethod.POST)
	public ServerResponse<String> register(User user){
		return iUserService.register(user);
		
	}
	/**
	 * 
	* @Title: checkValid  
	* @Description: TODO 用户名和密码校验
	* @param @param str
	* @param @param type
	* @param @return    
	* @return ServerResponse<String>      
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value="checkvalid.do",method=RequestMethod.POST)
	public ServerResponse<String> checkValid( String str,String type){
		return iUserService.checkValid(str, type);
	}
	/**
	 * 登录获取用户信息
	* @Title: getUserInfo  
	* @Description: TODO 
	* @param @param session
	* @param @return    
	* @return ServerResponse<User>      
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value="get_user_info.do",method=RequestMethod.POST)
	public ServerResponse<User> getUserInfo(HttpSession session){
		User user=(User) session.getAttribute(Const.CURRENT_USER);
		if(user!=null){
			return ServerResponse.createBySuccess(user);
		}
		return ServerResponse.createByErrorMessage("用户未登录无法获取用户信息");
	}
	/**
	 * 
	* @Title: forgetGetQuestion  
	* @Description: TODO通过用户名查询验证用户问题 
	* @param @param usrename
	* @param @return    
	* @return ServerResponse<String>      
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value="forget_get_question.do",method=RequestMethod.POST)
	public ServerResponse<String> forgetGetQuestion(String username){
		
		return iUserService.selectQuestion(username);
		
	}
	/**
	 * 
	* @Title: forgetCheckAnswer  
	* @Description: TODO验证用户的问题和答案是否正确
	* @param @param username
	* @param @param question
	* @param @param answer
	* @param @return    
	* @return ServerResponse<String>      
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value="forget_check_answer.do",method=RequestMethod.POST)
	public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
		return iUserService.checkAnswer(username, question, answer);
		
	}
	/**
	 * 
	* @Title: forgetResetPassword  
	* @Description: TODO 用户重置密码
	* @param @param username
	* @param @param passwordNew
	* @param @param forgetToken
	* @param @return    
	* @return ServerResponse<String>      
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value="forget_reset_password.do",method=RequestMethod.POST)
	public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
		return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
		
	}
	@ResponseBody
	@RequestMapping(value="reset_password.do",method=RequestMethod.POST)
	public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
		User user=(User) session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorMessage("用户未登录");
		}
		else{
			return iUserService.resetPassword(passwordOld, passwordNew, user);
		}
		
	}
	/**
	 * 更新用户信息
	* @Title: update_informaton  
	* @Description: TODO 
	* @param @param session
	* @param @param user
	* @param @return    
	* @return ServerResponse<User>      
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value="update_information.do",method=RequestMethod.POST)
	public ServerResponse<User> update_informaton(HttpSession session,User user){
		User currentUser=(User) session.getAttribute(Const.CURRENT_USER);
		if(currentUser==null){
			return ServerResponse.createByErrorMessage("用户未登录");
		}
		user.setId(currentUser.getId());
		user.setUsername(currentUser.getUsername());
		ServerResponse<User> response=iUserService.updateInformation(user);
		if(response.isSuccess()){
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		return response;
	}
	/**
	 * 获取当前用户信息
	* @Title: get_information  
	* @Description: TODO 
	* @param @param session
	* @param @return    
	* @return ServerResponse<User>      
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value="get_infomation.do",method=RequestMethod.POST)
	public ServerResponse<User> get_information(HttpSession session){
		User currentUser=(User) session.getAttribute(Const.CURRENT_USER);
		if(currentUser==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,用户需要强制登录status=10");
		}
		return iUserService.getInfomation(currentUser.getId());
	}

}
