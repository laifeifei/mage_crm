package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.base.CrmConstant;
import com.mage.crm.model.MessageModel;
import com.mage.crm.model.UserModel;
import com.mage.crm.query.UserQuery;
import com.mage.crm.service.UserService;
import com.mage.crm.util.CookieUtil;
import com.mage.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;
    /**
     * 登录
     * @param userName
     * @param userPwd
     * @return
     */
    @RequestMapping("/userLogin")
    @ResponseBody
    public MessageModel userLogin(String userName, String userPwd){
        MessageModel messageModel = new MessageModel();
        messageModel.setCode(CrmConstant.OPS_SUCCESS_CODE);
        messageModel.setMsg(CrmConstant.OPS_SUCCESS_MSG);
        //进行登录操作
        UserModel userModel = userService.userLogin(userName, userPwd);
        //登录成功
        //设置返回用户信息到返回信息中
        messageModel.setResult(userModel);
        return messageModel;
    }

    /**
     * 修改密码
     * @param request
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @return
     */
    @ResponseBody
    @RequestMapping("updatePwd")
    public MessageModel updatePwd(HttpServletRequest request, String oldPassword, String newPassword, String confirmPassword){
        MessageModel messageModel = new MessageModel();
        String id = CookieUtil.getCookieValue(request, "id");
        userService.updatePwd(id,oldPassword,newPassword,confirmPassword);
        return createMessageModel("用户密码修改成功");
    }

    /**
     * 查询分配人
     * @return
     */
    @RequestMapping("queryAllCustomerManager")
    @ResponseBody
    public List<User> queryAllCustomerManager(){
        List<User> users = userService.queryAllCustomerManager();
        return users;
    }
    @RequestMapping("index")
    public String index(){
        return "user";
    }
    @RequestMapping("queryUsersByParams")
    @ResponseBody
    public Map<String,Object> queryUsersByParams(UserQuery userQuery){
        return userService.queryUsersByParams(userQuery);
    }
    @ResponseBody
    @RequestMapping("insert")
    public MessageModel insert(User user){
        userService.insert(user);
        return createMessageModel("插入用户成功");
    }
    @ResponseBody
    @RequestMapping("update")
    public MessageModel update(User user){
        userService.update(user);
        return createMessageModel("用户修改成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public MessageModel delete(Integer id){
        userService.delete(id);
        return createMessageModel("用户删除成功");
    }
}
