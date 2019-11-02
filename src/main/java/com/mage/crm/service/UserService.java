package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.mage.crm.dao.PermissionDao;
import com.mage.crm.dao.UserDao;
import com.mage.crm.dao.UserRoleDao;
import com.mage.crm.dto.UserDto;
import com.mage.crm.model.UserModel;
import com.mage.crm.query.UserQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.util.Base64Util;
import com.mage.crm.util.Md5Util;
import com.mage.crm.vo.User;
import com.mage.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private UserRoleDao userRoleDao;

    @Resource
    private PermissionDao permissionDao;
    @Resource
    private HttpSession httpSession;
    /**
     * 用户合法性检测
     * @return
     */
    public UserModel userLogin(String userName,String userPwd){
        //查询操作
        User user = userDao.queryUserByName(userName);
        //进行相关判断
        //判断用户是否为空
        AssertUtil.isTrue(null==user,"用户不存在");
        //判断用户是否被注销
        AssertUtil.isTrue("0".equals(user.getIsValid()),"用户已经被注销");
        //判断用户密码是否正确
        //将密码用Md5进行加密
        String userPwd1 = Md5Util.encode(userPwd);

        AssertUtil.isTrue(!userPwd1.equals(user.getUserPwd()),"密码不正确");
        //获取用户拥有的权限，根据用户拥有的角色显示对应的选择框
        List<String> permissions = permissionDao.queryPermissionsByUserId(user.getId()+"");
        if(null!=permissions&&permissions.size()>0){
            httpSession.setAttribute("userPermission",permissions);
        }
        //将信息存入返回信息的类中，并返回
        return createUserModel(user);
    }

    /**
     * 将信息存入userModel中
     * @param user
     * @return
     */
    private UserModel createUserModel(User user) {
        UserModel userMode = new UserModel();
        //将id进行加密
        String userId = Base64Util.enCode(user.getId());
        userMode.setUserId(userId);
        userMode.setUserName(user.getUserName());
        userMode.setTrueName(user.getTrueName());
        return userMode;
    }

    /**
     * 更新密码
     * @param
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    public void updatePwd(String id, String oldPassword, String newPassword, String confirmPassword) {
        AssertUtil.isTrue(StringUtils.isBlank(id),"id不存在");
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"新密码不能为空");
        AssertUtil.isTrue(oldPassword.equals(newPassword),"新密码与原来密码不能相等");
        AssertUtil.isTrue(!newPassword.equals(confirmPassword),"新密码与确认密码不一致");
        //将id进行解密后再查询
        User user = userDao.queryUserById(Base64Util.deCode(id));
        AssertUtil.isTrue(null==user,"用户不存在了");
        AssertUtil.isTrue("0".equals(user.getIsValid()),"用户已经被注销了");
        //将密码加密后进行比较
        AssertUtil.isTrue(!Md5Util.encode(oldPassword).equals(user.getUserPwd()),"原始密码错误");
        AssertUtil.isTrue(userDao.updatePwd(user.getId(),Md5Util.encode(newPassword))<1,"用户密码更新失败");
    }

    /**
     * 通过id查询用户是否存在
     * @param id
     */
    public User queryUserById(String id) {
        return userDao.queryUserById(id);
    }

    /**
     * 查询分配人
     * @return
     */
    public List<User> queryAllCustomerManager() {
        return userDao.queryAllCustomerManager();
    }

    public Map<String, Object> queryUsersByParams(UserQuery userQuery) {
        PageHelper.startPage(userQuery.getPage(),userQuery.getRows());
        List<UserDto> userList = userDao.queryUsersByParams(userQuery);
        //给roleIds赋值
        if (userList != null && userList.size() > 0) {
            for (UserDto userDto : userList) {
                String roleIdstr = userDto.getRoleIdsStr();
                if (roleIdstr != null) {
                    String[] roleIdArray =  roleIdstr.split(",");
                    for (int i = 0; i <roleIdArray.length ; i++) {
                        List<Integer> roleIds = userDto.getRoleIds();
                        roleIds.add(Integer.parseInt(roleIdArray[i]));
                    }
                }
            }
        }
        PageInfo<UserDto> userPageInfo = new PageInfo<>(userList);
        Map<String,Object> map = new HashMap<>();
        map.put("total",userPageInfo.getTotal());
        map.put("rows",userPageInfo.getList());
        return map;
    }

    public void insert(User user) {
        //非空判断
        checkParams(user.getUserName(),user.getTrueName(),user.getPhone());
        //判断userName的唯一性
        AssertUtil.isTrue(userDao.queryUserByName(user.getUserName())!=null,"用户名已经存在");
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        //设置默认密码
        user.setUserPwd(Md5Util.encode("123456"));
        //更新用户表
        AssertUtil.isTrue(userDao.insert(user)<1,"插入用户失败");
        List<Integer> roleIds = user.getRoleIds();
        if(roleIds!=null&&roleIds.size()>0){
            relateRoles(roleIds,Integer.parseInt(user.getId()));
        }
    }
    private void relateRoles(List<Integer> roleIds, int userId) {
        List<UserRole> roleList=new ArrayList<UserRole>();
        for (Integer roleId:roleIds){
            UserRole userRole = new UserRole();
            userRole.setIsValid(1);
            userRole.setCreateDate(new Date());
            userRole.setUpdateDate(new Date());
            userRole.setRoleId(roleId);
            userRole.setUserId(userId);
            roleList.add(userRole);
        }
        AssertUtil.isTrue(userRoleDao.insertBacth(roleList)<1,"用户角色添加失败");
    }

    public void update(User user) {
        //非空判断
        checkParams(user.getUserName(),user.getTrueName(),user.getPhone());
        User tmpUser = userDao.queryUserByName(user.getUserName());
        //判断userName的唯一性
        AssertUtil.isTrue(tmpUser!=null&&!user.getId().equals(tmpUser.getId()),"用户名已经存在");
        user.setUpdateDate(new Date());
        //修改用户表
        AssertUtil.isTrue(userDao.update(user)<1,"用户修改失败");
        //修改用户角色表
        List<Integer> roleIds = user.getRoleIds();
        if(roleIds!=null&&roleIds.size()>0){
            //先查询用户角色是否存在
            int count = userRoleDao.queryRoleCountsByUserId(Integer.parseInt(user.getId()));
            //存在，先删除，再插入
            if(count>0){
                AssertUtil.isTrue(userRoleDao.deleteRolesByUserId(Integer.parseInt(user.getId()))<1,"用户更新失败");
            }
            //插入
            relateRoles(roleIds,Integer.parseInt(user.getId()));
        }
    }

    /**
     * 非空判断方法
     */
    public void checkParams(String userName,String trueName,String phone){
        AssertUtil.isTrue(StringUtil.isEmpty(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(trueName),"真实姓名不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(phone),"手机号码不能为空");
    }

    public void delete(Integer id) {
        AssertUtil.isTrue(userDao.delete(id)<1,"用户删除失败");
        int counts = userRoleDao.queryRoleCountsByUserId(id);
        if(counts>0){
            AssertUtil.isTrue(userRoleDao.deleteRolesByUserId(id)<counts,"用户角色删除失败");
        }
    }
}
