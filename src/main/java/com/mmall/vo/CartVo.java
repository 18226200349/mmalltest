package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

public class CartVo {
     private List<CartProductVo> cartProductVoList;
     private BigDecimal cartTotalPrice;
     private Boolean allchecked;//是否全部勾选
     private String imageGHost;
}
