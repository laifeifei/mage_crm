package com.mage.crm.query;

import com.mage.crm.base.BaseQuery;

public class OrderDetailsQuery extends BaseQuery {
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
