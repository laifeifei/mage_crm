package com.mage.crm.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.CustomerDevPlanDao;
import com.mage.crm.dao.SaleChanceDao;
import com.mage.crm.query.CustomerDevPlanQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.CustomerDevPlan;
import com.mage.crm.vo.SaleChance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerDevPlanService {
    @Resource
    private CustomerDevPlanDao customerDevPlanDao;
    @Resource
    private SaleChanceDao saleChanceDao;//创建营销机会对象

    /**
     * 查询开发计划项
     * @param customerDevPlanQuery
     * @return
     */
    public Map<String, Object> queryCusDevPlans(CustomerDevPlanQuery customerDevPlanQuery) {
        //分页操作
        //设置开始页和每页的数据条数
        PageHelper.startPage(customerDevPlanQuery.getPage(),customerDevPlanQuery.getRows());
        //查询数据返回List集合,通过营销机会的id查询
        List<CustomerDevPlan> customerDevPlanList = customerDevPlanDao.queryCusDevPlansBySaleChanceId(customerDevPlanQuery.getSaleChanceId());
        //设置分页对象
        PageInfo<CustomerDevPlan> customerDevPlanPageInfo = new PageInfo<>(customerDevPlanList);
        //创建map集合，存储页数和数据
        Map<String,Object> map = new HashMap<>();
        //存储页数
        map.put("total",customerDevPlanPageInfo.getTotal());
        //存储每页的数据
        map.put("rows",customerDevPlanPageInfo.getList());
        //放回map集合
        return map;
    }

    /**
     * 保存开发计划项
     * @param customerDevPlan
     */
    public void insert(CustomerDevPlan customerDevPlan){
        //非空判断，判断营销机会是否还存在
        //根据id查询营销机会是否还存在
        SaleChance saleChance = saleChanceDao.querySaleChancesById(customerDevPlan.getSaleChanceId());
        AssertUtil.isTrue(saleChance==null,"营销机会不存在");
        AssertUtil.isTrue(saleChance.getDevResult()==2,"营销机会已经完成");
        AssertUtil.isTrue(saleChance.getDevResult()==3,"营销机会已经失败");
        //设置开发计划项为有效
        customerDevPlan.setIsValid(1);
        //设置创建时间
        customerDevPlan.setCreateDate(new Date());
        //设置更新时间
        customerDevPlan.setUpdateDate(new Date());
        //添加开发计划，并判断是否成功
        AssertUtil.isTrue(customerDevPlanDao.insert(customerDevPlan)<1,"开发计划添加失败");
        //将营销机会设置为已分配
        if(saleChance.getDevResult()==0){
            saleChanceDao.updataDevResult(customerDevPlan.getSaleChanceId(),1);
        }
    }

    public void update(CustomerDevPlan customerDevPlan) {
        //非空判断，判断营销机会是否还存在
        //根据id查询营销机会是否还存在
        SaleChance saleChance = saleChanceDao.querySaleChancesById(customerDevPlan.getSaleChanceId());
        AssertUtil.isTrue(saleChance==null,"营销机会不存在");
        AssertUtil.isTrue(saleChance.getDevResult()==2,"营销机会已经完成");
        AssertUtil.isTrue(saleChance.getDevResult()==3,"营销机会已经失败");
        //设置更新时间
        customerDevPlan.setUpdateDate(new Date());
        //更新开发计划，并判断是否成功
        AssertUtil.isTrue(customerDevPlanDao.update(customerDevPlan)<1,"开发计划添加失败");
    }

    /**
     * 删除客户开发计划
     * @param id
     */
    public void delete(Integer id){
        AssertUtil.isTrue(customerDevPlanDao.delete(id)<1,"客户开发计划删除失败");
    }
}
