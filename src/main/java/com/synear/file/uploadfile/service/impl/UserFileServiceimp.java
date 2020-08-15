package com.synear.file.uploadfile.service.impl;

import com.synear.file.uploadfile.dao.UserFileDao;
import com.synear.file.uploadfile.pojo.UserFile;
import com.synear.file.uploadfile.service.UserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserFileServiceimp implements UserFileService {

    @Autowired
    private UserFileDao userFileDao;

    @Override
    public List<UserFile> findByUserId(Integer id) {
        return userFileDao.findByUserId(id);
    }

    @Override
    public void save(UserFile userFile) {
        /*userFile.setIsImg();  ？是否为图片   直接从type类型的内容去判断*/
        String isImg = userFile.getType().startsWith("image") ? "是" : "否";
        userFile.setIsImg(isImg);
        userFile.setDownCounts(0);
        userFile.setUploadTime(new Date());
        userFileDao.save(userFile);
    }

    @Override
    public UserFile findById(Integer id) {
        return userFileDao.findById(id);
    }

    @Override
    public void updateDownCounts(UserFile userFile) {
        userFileDao.updateDownCounts(userFile);
    }

    @Override
    public void deleteById(Integer id) {
        userFileDao.deleteById(id);
    }
}
