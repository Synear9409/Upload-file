package com.synear.file.uploadfile.dao;

import com.synear.file.uploadfile.pojo.UserFile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFileDao {

    ////根据登录用户id获取该用户的文件列表
    List<UserFile> findByUserId(Integer id);
    ///保存上传文件信息
    void save(UserFile userFile);

    UserFile findById(Integer id);

    void updateDownCounts(UserFile userFile);

    void deleteById(Integer id);
}
