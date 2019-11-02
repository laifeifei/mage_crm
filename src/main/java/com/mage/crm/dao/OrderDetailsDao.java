package com.mage.crm.dao;

import com.mage.crm.query.OrderDetailsQuery;
import com.mage.crm.vo.OrderDetails;

import java.util.List;

public interface OrderDetailsDao {
    List<OrderDetails> queryOrderDetailsByOrderId(OrderDetailsQuery orderDetailsQuery);
}
