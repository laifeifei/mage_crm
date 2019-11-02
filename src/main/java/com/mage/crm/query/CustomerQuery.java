package com.mage.crm.query;

import com.mage.crm.base.BaseQuery;

public class CustomerQuery extends BaseQuery {
    private String khno; //客户编号
    private String name; //客户名称

    public String getKhno() {
        return khno;
    }

    public void setKhno(String khno) {
        this.khno = khno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
