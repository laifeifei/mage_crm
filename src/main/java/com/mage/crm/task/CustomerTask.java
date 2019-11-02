package com.mage.crm.task;

import com.mage.crm.service.CustomerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 客户流失管理定时器
 */
@Component
public class CustomerTask {
    @Resource
    private CustomerService customerService;
    //@Scheduled(cron = "0 55 19 * * ?")
    /*public void lossTask(){
        customerService.updateCustomerLossState();
        System.out.println("我被触发了");
    }*/
}
