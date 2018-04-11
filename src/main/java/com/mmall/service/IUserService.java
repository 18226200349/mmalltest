package com.mmall.service;

import org.springframework.stereotype.Service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

@Service
public interface IUserService {
 ServerResponse<User> login(String username,String password);
}
