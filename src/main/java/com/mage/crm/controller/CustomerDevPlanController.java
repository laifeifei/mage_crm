package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.model.MessageModel;
import com.mage.crm.query.CustomerDevPlanQuery;
import com.mage.crm.service.CustomerDevPlanService;
import com.mage.crm.service.SaleChanceService;
import com.mage.crm.vo.CustomerDevPlan;
import com.mage.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("cus_dev_plan")
public class CustomerDevPlanController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private CustomerDevPlanService customerDevPlanService;
    @RequestMapping("index")
    public String index(Integer id, Model model){
        //通过id查询营销机会
        SaleChance saleChance = saleChanceService.querySaleChancesById(id);
        //将数据存入作用域中
        model.addAttribute("saleChance",saleChance);
        //返回详情页面
        return "cus_dev_plan_detail";
    }

    /**
     * 查询开发计划项
     * @param customerDevPlanQuery
     * @return
     */
    @RequestMapping("queryCusDevPlans")
    @ResponseBody
    public Map<String,Object> queryCusDevPlans(CustomerDevPlanQuery customerDevPlanQuery){
        return customerDevPlanService.queryCusDevPlans(customerDevPlanQuery);
    }

    /**
     * 保存开发计划项
     * @param customerDevPlan
     */
    @RequestMapping("insert")
    @ResponseBody
    public MessageModel insert(CustomerDevPlan customerDevPlan){
        customerDevPlanService.insert(customerDevPlan);
        return createMessageModel("客户开发计划添加成功");
    }

    /**
     * 更新客户开发计划
     * @param customerDevPlan
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public MessageModel update(CustomerDevPlan customerDevPlan){
        customerDevPlanService.update(customerDevPlan);
        return createMessageModel("客户开发计划更新成功");
    }

    /**
     * 删除客户开发计划
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public MessageModel delete(Integer id){
        customerDevPlanService.delete(id);
        return createMessageModel("客户开发计划删除成功");
    }


}
