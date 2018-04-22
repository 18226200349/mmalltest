package com.mmall.service.impl;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
@Service("iUserService")
public class UserServiceImpl implements IUserService{
    @Autowired
    private UserMapper userMapper;
    /**
     * 用户登录
     */
	@Override
	public ServerResponse<User> login(String username, String password) {
		int resultCount=userMapper.checkUsername(username);
		if(resultCount==0){
			return ServerResponse.createByErrorMessage("用户名不存在");
		}
		//todo 密码登录MD5
		String mad5Password=MD5Util.MD5EncodeUtf8(password);
		User user=userMapper.selectLogin(username, mad5Password);
		if(user==null){
			return ServerResponse.createByErrorMessage("密码错误");
		}
		//将密码重置为空，避免泄露
		user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
		return ServerResponse.createBySucess("登录成功", user);
	}
	/**
	 * 用户注册
	 */
	@Override
	public ServerResponse<String> register(User user) {
		//检验用户名是否存在返回一个值
		ServerResponse validResonse=this.checkValid(user.getUsername(),Const.USERNAME);
		//如果不成功检验返回一个error值
		if(!validResonse.isSuccess()){
			return validResonse;
		}
		//检验用户邮箱存在
		 validResonse=this.checkValid(user.getEmail(),Const.EMAIL);
		if(!validResonse.isSuccess()){
			return validResonse;
		}
	    user.setRole(Const.Role.ROLE_CUSTOMER);
	    //MD5加密
	    user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
	    int  resultCount=userMapper.insert(user);
	    if(resultCount==0){
	    	return ServerResponse.createByErrorMessage("注册失败");
	    }
		return ServerResponse.createBySuccessMessage("注册成功");
	}
	/**
	 * 
	* @Title: checkValid  
	* @Description: TODO 校验用户密码和邮箱给前台一个反馈数据
	* @param @param str
	* @param @param type
	* @param @return    
	* @return ServerResponse<String>      
	* @throws
	 */
	@Override
    public ServerResponse<String> checkValid(String str,String type){
	if(StringUtils.isNotBlank(type)){
		//开始校验
		if(Const.USERNAME.equals(type)){
			int resultCount=userMapper.checkUsername(str);
			if(resultCount>0){
				return ServerResponse.createByErrorMessage("用户名已存在");
			}
		}
		if(Const.EMAIL.equals(type)){
			int resultCount=userMapper.checkEmail(str);
			    if(resultCount>0){
			    	return ServerResponse.createByErrorMessage("email已存在");
			    }
		}
	}
	else{
		return ServerResponse.createByErrorMessage("参数错误");
	}
	return ServerResponse.createBySuccessMessage("校验成功");   
   }
	
   /**
    * 通过用户名查询验证问题
    */
   public ServerResponse selectQuestion(String username){
	   ServerResponse validResponse=this.checkValid(username, Const.USERNAME);
	   if(validResponse.isSuccess()){
		   //用户不存在
		   return ServerResponse.createByErrorMessage("用户不存在");
	   }
	   String question=userMapper.findQuestionByUsername(username);
	   if(StringUtils.isNotBlank(question)){
		   return ServerResponse.createBySuccess(question);
	   }
	       return ServerResponse.createByErrorMessage("找回密码的问题是空的");
   }
   /**
    * 检查用户问题答案
    */
   public ServerResponse<String> checkAnswer(String username,String question,String answer){
	   int resultCount=userMapper.checkAnswer(username,question,answer);
	   if(resultCount>0){
		   //说明问题及问题答案是正确的是这个用户的
		   String forgetToken=UUID.randomUUID().toString();
		   //将fogetToken 放到
		   TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
		   return ServerResponse.createBySuccess(forgetToken);
		   
	   }
	   return ServerResponse.createByErrorMessage("问题答案错误");
   }
   /**
    * 问题回答成功后重置密码
    */
   public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
	   if(StringUtils.isBlank(forgetToken)){
		   return ServerResponse.createByErrorMessage("参数错误，token需要传递");
	   }
	   ServerResponse validResponse=this.checkValid(username, Const.USERNAME);
	   if(validResponse.isSuccess()){
		   //用户不存在
		   return ServerResponse.createByErrorMessage("用户不存在");
	   }
	   String token=TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
	   if(StringUtils.isBlank(token)){
		   return ServerResponse.createByErrorMessage("token无效或过期");
	   }
	   if(StringUtils.equals(forgetToken, token)){
		   String md5Password=MD5Util.MD5EncodeUtf8(passwordNew);
		   int rowCount=userMapper.updatePasswordByUsername(username, md5Password);
		   if(rowCount>0){
			   return ServerResponse.createBySuccessMessage("修改密码成功");
		   }
	   }
	   else{
		   return ServerResponse.createByErrorMessage("token错误,请重新获取重置密码");
	   }
	   
	   return ServerResponse.createByErrorMessage("修改密码失败");
   }
   /**
    * 重置密码
    */
   public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
	   //防止横向越权，要校验一下这个用户的旧密码，一定要指定这个用户，因为我们会查询出一个Count(1),必须指定Id,不然使用字典也是有可能通过的查询出true
	   int resultCount=userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
	   if(resultCount==0){
		   return ServerResponse.createByErrorMessage("密码错误");
	   }
	   user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
	   int updateCount=userMapper.updateByPrimaryKeySelective(user);
	   if(updateCount>0){
		   return ServerResponse.createBySuccessMessage("密码更新成功");
	   }
	   else{
		   return ServerResponse.createByErrorMessage("密码更新失败");
	   }
   }
   /**
    * 更新个人信息
    */
   public ServerResponse<User> updateInformation(User user){
	   //username不能更新
	   //email也需要进行校验，校验新的email 是不是已经存在，并且存在的email如果相同的话，不能是我们当前的这个用户的
	   int resultCount=userMapper.checkEmailByUserId(user.getEmail(), user.getId());
	   if(resultCount>0){
		   return ServerResponse.createByErrorMessage("email已经存在，请更换email再尝试更新");
	   }
	   
	   User updateUser=new User();
	   updateUser.setId(user.getId());
	   updateUser.setEmail(user.getEmail());
	   updateUser.setPhone(user.getPhone());
	   updateUser.setQuestion(user.getQuestion());
	   updateUser.setAnswer(user.getAnswer());
	   
	   int updateCount=userMapper.updateByPrimaryKeySelective(updateUser);
	   if(updateCount>0){
		   return ServerResponse.createBySucess("更新个人信息成功", updateUser);
	   }
	   else{
		   return ServerResponse.createByErrorMessage("更新个人信息失败");
	   }
   }
   /**
    * 获取用户信息
   * @Title: getInfomation  
   * @Description: TODO 
   * @param @param userId
   * @param @return    
   * @return ServerResponse<User>      
   * @throws
    */
   public ServerResponse<User> getInfomation(Integer userId){
	   User user=userMapper.selectByPrimaryKey(userId);
	   if(user==null){
		   return ServerResponse.createByErrorMessage("找不到当前用户");
	   }
	   	user.setPassword(StringUtils.EMPTY);
	    return ServerResponse.createBySuccess(user);
   }
   //backend
   /**
    * 
   * @Title: 校验是否是管理员  
   * @Description: TODO 
   * @param @param user
   * @param @return    
   * @return ServerResponse      
   * @throws
    */
   public ServerResponse checkAdminRole(User user){
	   if(user!=null&& user.getRole().intValue()==Const.Role.ROLE_ADMIN){
		   return ServerResponse.createBySuccess();
	   }
	   return ServerResponse.createByError();
   }
}
