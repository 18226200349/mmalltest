package com.mmall.controller.backend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/manage/product")
public class ProdouctManageController {
	@Autowired
	private IUserService iUserService;
	@Autowired
	private IProductService iProductService;

	/**
	 * 
	 * @Title: 新增或更新接口 @Description: 新增或更新接口 @param @param session @param @param
	 *         product @param @return @return ServerResponse @throws
	 */
	@RequestMapping(value = "save.do")
	@ResponseBody
	public ServerResponse productSave(HttpSession session, Product product) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			// 添加增加商品的业务逻辑
			return iProductService.saveOrUpdateProduct(product);
		} else {
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}

	/**
	 * 
	 * @Title: 改变商品状态 @Description: TODO @param @param session @param @param
	 *         productId @param @param status @param @return @return
	 *         ServerResponse @throws
	 */
	@RequestMapping(value = "get_sale_status.do")
	@ResponseBody
	public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			// 获取商品的状态信息
			return iProductService.setSaleStatus(productId, status);
		} else {
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}

	/**
	 * 
	 * @Title: 改变商品状态 @Description: TODO @param @param session @param @param
	 *         productId @param @param status @param @return @return
	 *         ServerResponse @throws
	 */
	@RequestMapping(value = "detail.do")
	@ResponseBody
	public ServerResponse getDetail(HttpSession session, Integer productId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			// 填充业务
			return iProductService.manageProductDetail(productId);
		} else {
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}

	@RequestMapping(value = "list.do")
	@ResponseBody
	public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			// 填充动态分页
			return iProductService.getProductList(pageNum, pageSize);
		} else {
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}

	@RequestMapping(value = "search.do")
	@ResponseBody
	public ServerResponse productSearch(HttpSession session,String productName,Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
								  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			// 填充动态分页
			return iProductService.searchProduct(productName,productId,pageNum,pageSize);
		} else {
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}
	public ServerResponse upload(MultipartFile file, HttpServletRequest request){
		String path=request.getSession().getServletContext().getRealPath("upload");
        return null;
	}
}
