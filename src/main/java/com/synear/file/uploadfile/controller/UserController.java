package com.synear.file.uploadfile.controller;

import com.synear.file.uploadfile.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("user")
@Transactional
public class UserController {

    @GetMapping
    public String toLogin(){
        return "login";
    }


    @PostMapping("login")
    public String login(User user, RedirectAttributes attributes){
        //获取当前的用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登入数据
        UsernamePasswordToken token = new UsernamePasswordToken(
                user.getUsername(), DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

        try {
            subject.login(token);
            return "redirect:/file/index";
        } catch (UnknownAccountException | IncorrectCredentialsException e) {
            //表示用户名不存在
            attributes.addFlashAttribute("message","用户名或者密码错误");
            return "redirect:/user";
        }
    }
    @GetMapping("logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }
}
