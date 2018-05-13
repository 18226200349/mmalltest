package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.pojo.Cart;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;
    public ServerResponse add(Integer userId,Integer productId,Integer count){
         Cart cart=cartMapper.selectCartByUserIdProductId(userId,productId);
         if(cart==null){
             //这个产品不存在购物车里，需要新增一个产品记录
             Cart cartItem=new Cart();
             cartItem.setQuantity(count);
             cartItem.setChecked(Const.Cart.CHECKED);
             cartItem.setProductId(productId);
             cartItem.setUserId(userId);
             cartMapper.insert(cartItem);


         }
         else {
             //产品已经存在
             //如果产品已存在，数量相加
             count=cart.getQuantity()+count;
             cart.setQuantity(count);
             cartMapper.updateByPrimaryKeySelective(cart);
         }
         return null;

    }
    private CartVo getCartVoLimit(Integer userId){
        CartVo cartVo=new CartVo();
        List<Cart> cartList=cartMapper.selectCartByUserId(userId);
        return null;
    }
}
