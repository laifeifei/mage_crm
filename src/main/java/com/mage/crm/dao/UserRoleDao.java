package com.mage.crm.dao;

import com.mage.crm.vo.UserRole;

import java.util.List;

public interface UserRoleDao {
    int insertBacth(List<UserRole> list);

    int queryRoleCountsByUserId(Integer id);

    int deleteRolesByUserId(Integer id);
}
