package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.model.MessageModel;
import com.mage.crm.query.CustomerRepriQuery;
import com.mage.crm.service.CustomerReprieveService;
import com.mage.crm.vo.CustomerReprieve;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("customer_repri")
public class CustomerReprieveController extends BaseController {
    @Resource
    private CustomerReprieveService customerReprieveService;
    @RequestMapping("customerReprieveByLossId")
    @ResponseBody
    public Map<String,Object> customerReprieveByLossId(CustomerRepriQuery customerRepriQuery){
        return customerReprieveService.customerReprieveByLossId(customerRepriQuery);
    }

    @RequestMapping("insertReprive")
    @ResponseBody
    public MessageModel insertReprive(CustomerReprieve customerReprieve){
        customerReprieveService.insertReprive(customerReprieve);
        return createMessageModel("添加暂缓措施成功");
    }
    @RequestMapping("updateReprive")
    @ResponseBody
    public MessageModel updateReprive(CustomerReprieve customerReprieve){
        customerReprieveService.updateReprive(customerReprieve);
        return createMessageModel("更新暂缓措施成功");
    }
    @RequestMapping("delete")
    @ResponseBody
    public MessageModel delete(Integer id){
        customerReprieveService.delete(id);
        return createMessageModel("删除缓存措施成功");
    }
}
