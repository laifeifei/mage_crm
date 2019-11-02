package com.mage.crm.controller;

import com.mage.crm.RequestPermission;
import com.mage.crm.base.BaseController;
import com.mage.crm.model.MessageModel;
import com.mage.crm.query.SaleChanceQuery;
import com.mage.crm.service.SaleChanceService;
import com.mage.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;
    /**
     * openTab跳转页面
     */
    @RequestMapping("index/{id}")
    public String index(@PathVariable("id") String id){
        //判断将要跳转得页面
        if("1".equals(id)){
            //跳转到营销机会管理页面
            return "sale_chance";
        }else if("2".equals(id)){
            //跳转到客户开发计划页面
            return "cus_dev_plan";
        }else{
            //跳转到错误页面
            return "error";
        }
    }

    /**
     * 根据条件查询出信息
     * @param saleChanceQuery
     * @return
     */
    @RequestMapping("querySaleChancesByParams")
    @ResponseBody
    public Map<String,Object> querySaleChancesByParams(SaleChanceQuery saleChanceQuery){
        return saleChanceService.querySaleChancesByParams(saleChanceQuery);
    }

    /**
     * 添加数据
     * @param saleChance
     * @return
     */
    @RequestMapping("insert")
    @ResponseBody
    public MessageModel insert(SaleChance saleChance){
        saleChanceService.insert(saleChance);
        return createMessageModel("营销机会添加成功");
    }

    /**
     * 修改数据
     * @param saleChance
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public MessageModel update(SaleChance saleChance){
        saleChanceService.update(saleChance);
        return createMessageModel("营销机会修改成功");
    }

    /**
     * 删除营销机会
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    //添加自定义注解，过滤掉无权限的删除请求
    @RequestPermission(aclVal = "101003")
    public MessageModel delete(Integer[] id){
        saleChanceService.delete(id);
        return createMessageModel("营销机会删除成功");
    }

    /**
     * 更新开发状态
     * @param devResult
     * @param saleChanceId
     * @return
     */
    @RequestMapping("updateSaleChanceDevResult")
    @ResponseBody
    public MessageModel updateSaleChanceDevResult(Integer saleChanceId,Integer devResult){
        saleChanceService.updateSaleChanceDevResult(saleChanceId,devResult);
        return createMessageModel("营销机会状态修改成功");
    }

}
