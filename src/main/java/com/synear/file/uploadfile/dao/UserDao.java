package com.synear.file.uploadfile.dao;

import com.synear.file.uploadfile.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    ///查找指定用户 ---提供给Shiro
    User findUserByName(String name);
}
