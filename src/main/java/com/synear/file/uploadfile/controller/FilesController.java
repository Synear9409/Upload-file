package com.synear.file.uploadfile.controller;

import com.synear.file.uploadfile.pojo.User;
import com.synear.file.uploadfile.pojo.UserFile;
import com.synear.file.uploadfile.service.UserFileService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("file")
public class FilesController {

    @Autowired
    private UserFileService userFileService;

    /**
     * 展示所有文件信息
     */
    @GetMapping("index")
    public String findAll(Model model,HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user != null){
            List<UserFile> listFile = userFileService.findByUserId(user.getId());
            model.addAttribute("listFile",listFile);
        }
        return "fileShow";
    }

    /**
     * 返回json格式的所有文件信息
     */
    @GetMapping("indexJson")
    @ResponseBody
    public List<UserFile> findAllJson(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user != null){
            return userFileService.findByUserId(user.getId());
        }
        return null;
    }

    /**
     * 文件下载和在线打开
     */
    @GetMapping("download")
    public void download(String openStyle,Integer id, HttpServletResponse response){
        //获取打开方式
        openStyle = openStyle == null ? "attachment":openStyle;
        ////获取文件信息
        UserFile userFile = userFileService.findById(id);
        ///下载才更新下载次数  在线打开不更新
        if("attachment".equals(openStyle)){
            userFile.setDownCounts(userFile.getDownCounts()+1);
            userFileService.updateDownCounts(userFile);
        }
        try {
            ///根据文件的名字和文件存储路径获取文件输出流
            String realPath = ResourceUtils.getURL("classpath:").getPath()+"static"+userFile.getPath();
            ///获取输入流
            FileInputStream is = new FileInputStream(new File(realPath,userFile.getNewFileName()));
            //附件下载
            response.setHeader("content-disposition",openStyle+";filename="+ URLEncoder.encode(userFile.getOldFileName(),"UTF-8"));
            //获取响应输出流
            ServletOutputStream os = response.getOutputStream();
            ///文件拷贝
            IOUtils.copy(is,os);
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     */
    @PostMapping("upload")
    public String upload(MultipartFile file,HttpSession session) throws IOException {
        ///获取用户id
        User user = (User) session.getAttribute("user");
        ///获取文件原始名称
        String oldFileName = file.getOriginalFilename();
        //获取后缀
        String ext = "." + FilenameUtils.getExtension(oldFileName);
        //生成新的文件名称  (UUID格式：xxx-xxx-xxx-xxx-xxx)
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                + UUID.randomUUID().toString().replace("-","")+ext;
        ///文件大小
        Long size = file.getSize();
        ///文件类型
        String type = file.getContentType();

        //根据日期生成目录
        String realPath = ResourceUtils.getURL("classpath:").getPath()+"static/files";
//        System.out.println(realPath);
        String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dateDirPath = realPath + "/" + dateFormat;
//        System.out.println(dateDirPath);
        File dateDir = new File(dateDirPath);
        //若不存在该目录则创建
        if(!dateDir.exists())dateDir.mkdirs();

        //处理文件上传
        file.transferTo(new File(dateDir,newFileName));

        UserFile userFile = new UserFile();
        userFile.setOldFileName(oldFileName);
        userFile.setNewFileName(newFileName);
        userFile.setExt(ext);
        userFile.setSize(String.valueOf(size));
        userFile.setType(type);
        userFile.setPath("/files/"+dateFormat);
        userFile.setUserId(user.getId());
        userFileService.save(userFile);
        return "redirect:/file/index";
    }

    /**
     * 文件删除
     */
    @GetMapping("delete")
    public String delete(Integer id){
        ///查到对应的文件信息
        UserFile userFile = userFileService.findById(id);

        try {
            ///删除文件
            String realPath = ResourceUtils.getURL("classpath:").getPath()+"static" + userFile.getPath();
            File oldFile = new File(realPath,userFile.getNewFileName());
            if(oldFile.exists())oldFile.delete();
            //删除数据库信息
            userFileService.deleteById(id);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return "redirect:/file/index";
    }
}
