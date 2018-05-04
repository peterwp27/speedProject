package com.nriet.project.service.impl;

import com.nriet.project.dao.UserMapper;
import com.nriet.project.model.User;
import com.nriet.project.service.UserService;
import com.nriet.framework.myBatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/05/03.
 */
@Service
@Transactional
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Resource
    private UserMapper userMapper;

}
