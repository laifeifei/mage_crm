package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.query.OrderDetailsQuery;
import com.mage.crm.service.OrderDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("order_detail")
public class OrderDetailsController extends BaseController {
    @Resource
    private OrderDetailsService orderDetailsService;
    @RequestMapping("queryOrderDetailsByOrderId")
    @ResponseBody
    public Map<String,Object> queryOrderDetailsByOrderId(OrderDetailsQuery orderDetailsQuery){
        return orderDetailsService.queryOrderDetailsByOrderId(orderDetailsQuery);
    }
}
