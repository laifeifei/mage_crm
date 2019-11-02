package com.mage.crm.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.ModuleDao;
import com.mage.crm.dao.PermissionDao;
import com.mage.crm.dao.RoleDao;
import com.mage.crm.dao.UserRoleDao;
import com.mage.crm.query.RoleQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.Module;
import com.mage.crm.vo.Permission;
import com.mage.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleService {
    @Resource
    private RoleDao roleDao;
    @Resource
    private UserRoleDao userRoleDao;
    @Resource
    private PermissionDao permissionDao;
    @Resource
    private ModuleDao moduleDao;

    public Map<String, Object> queryRolesByParams(RoleQuery roleQuery) {
        PageHelper.startPage(roleQuery.getPage(), roleQuery.getRows());
        List<Role> roles= roleDao.queryRolesByParams(roleQuery.getRoleName());
        PageInfo<Role> pageInfo=new PageInfo<Role>(roles);
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("total", pageInfo.getTotal());
        map.put("rows", pageInfo.getList());
        return map;
    }

    public List<Role> queryAllRoles() {
        return roleDao.queryAllRoles();
    }

    public void insert(Role role) {
        //非空判断
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名不能为空");
        //判断角色是否已经存在
        AssertUtil.isTrue(null!=roleDao.queryRoleByRoleName(role.getRoleName()),"角色已经存在");
        //设置创建时间和更新时间
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        role.setIsValid(1);
        AssertUtil.isTrue(roleDao.insert(role)<1, "角色添加成功");
    }

    /**
     * 更新角色信息
     * @param role
     */
    public void update(Role role) {
        //参数校验
        //角色名非空
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名不能为空");
        //角色id必须存在
        AssertUtil.isTrue(null==role.getId()&&null==roleDao.queryRoleById(role.getId()),"要更新的角色不存在");
        //查询角色名
        Role roleName = roleDao.queryRoleByRoleName(role.getRoleName());
        //要更新的角色名不能已经存在
        AssertUtil.isTrue(null!=roleName&&!roleName.getRoleName().equals(role.getRoleName()),"角色名已经存在");
        //设置更新时间
        role.setUpdateDate(new Date());
        //更新角色
        AssertUtil.isTrue(roleDao.update(role)<1,"角色更新成功");
    }

    /**
     * 删除角色
     * @param id
     */
    public void delete(Integer id) {
        //验证要删除的角色是否存在
        AssertUtil.isTrue(null==id||null==roleDao.queryRoleById(id+""),"要删除的角色不存在");
        //级联删除用户角色记录
        int counts = userRoleDao.queryRoleCountsByUserId(id);
        if(counts>0){
            //删除用户角色关联表中的记录
            AssertUtil.isTrue(userRoleDao.deleteRolesByUserId(id)<1,"用户角色关联表删除失败");
        }
        //删除角色表中的记录
        AssertUtil.isTrue(roleDao.delete(id)<1,"角色删除失败");
    }

    /**
     * 添加角色关联权限
     * @param rid
     * @param moduleIds
     */
    public void addPermission(Integer rid, Integer[] moduleIds) {
        //参数合法性校验
        //rid的角色记录必须存在
        AssertUtil.isTrue(null==rid||null==roleDao.queryRoleById(rid+""),"要授权角色不存在");
        //查询角色原来的权限是否存在
        int count = permissionDao.queryPermissionCountByRid(rid);
        //存在，删除角色原来的权限
        if(count>0){
            AssertUtil.isTrue(permissionDao.deletePermissionByRid(rid)<count,"角色权限删除失败");
        }
        List<Permission> permissions = null;
        //判断moduleIds是否为空
        if(null!=moduleIds&&moduleIds.length>0){
            //执行批量添加，将权限添加给对应的角色
            //声明一个对象集合，用来存储permission对象
            permissions = new ArrayList<>();
            Module module = null;
            for(Integer moduleId:moduleIds){
                //通过moduleId查询module对象
                module = moduleDao.queryModuleById(moduleId);
                //创建permission对象
                Permission permission = new Permission();
                if(module!=null){
                    //将module的optvalue赋值给permission
                    permission.setAclValue(module.getOptValue());
                }
                //设置相关信息
                permission.setRoleId(rid);
                permission.setModuleId(moduleId);
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                //放到list集合中
                permissions.add(permission);
            }
            //将授权信息添加到数据中
            AssertUtil.isTrue(permissionDao.insertBatch(permissions)<moduleIds.length, "角色授权成功");
        }
    }
}
