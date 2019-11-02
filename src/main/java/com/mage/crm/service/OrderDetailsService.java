package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.OrderDetailsDao;
import com.mage.crm.query.OrderDetailsQuery;
import com.mage.crm.vo.OrderDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderDetailsService {
    @Resource
    private OrderDetailsDao orderDetailsDao;
    public Map<String, Object> queryOrderDetailsByOrderId(OrderDetailsQuery orderDetailsQuery) {
        //分页
        PageHelper.startPage(orderDetailsQuery.getPage(), orderDetailsQuery.getRows());
        List<OrderDetails> customerOrderList = orderDetailsDao.queryOrderDetailsByOrderId(orderDetailsQuery);
        PageInfo<OrderDetails> pageInfo = new PageInfo<>(customerOrderList);
        Map<String,Object> map = new HashMap<>();
        map.put("total",pageInfo.getTotal());
        map.put("rows",pageInfo.getList());
        return map;
    }
}
