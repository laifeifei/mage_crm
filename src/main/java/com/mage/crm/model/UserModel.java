package com.mage.crm.model;

/**
 * 返回数据的实体类
 */
public class UserModel {
    private String userName;    //用户名
    private String trueName;    //用户真实姓名
    private String userId;  //用户主键id

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
