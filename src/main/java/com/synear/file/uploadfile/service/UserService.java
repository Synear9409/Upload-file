package com.synear.file.uploadfile.service;

import com.synear.file.uploadfile.pojo.User;

public interface UserService {

    User findUserByName(String name);
}
