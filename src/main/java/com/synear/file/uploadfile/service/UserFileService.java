package com.synear.file.uploadfile.service;

import com.synear.file.uploadfile.pojo.UserFile;

import java.util.List;

public interface UserFileService {

    List<UserFile> findByUserId(Integer id);

    void save(UserFile userFile);

    UserFile findById(Integer id);

    void updateDownCounts(UserFile userFile);

    void deleteById(Integer id);
}
