package com.mage.crm.query;

import com.mage.crm.base.BaseController;
import com.mage.crm.base.BaseQuery;

/**
 * 查询条件的实体类
 */
public class SaleChanceQuery extends BaseQuery {
    private String createMan;//创建人
    private String createDate;//创建时间
    private String customerName;//客户名称
    private String state;//分配状态

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
