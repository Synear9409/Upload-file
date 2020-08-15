package com.synear.file.uploadfile.service.impl;

import com.synear.file.uploadfile.dao.UserDao;
import com.synear.file.uploadfile.pojo.User;
import com.synear.file.uploadfile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImp implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User findUserByName(String name) {
        return userDao.findUserByName(name);
    }
}
