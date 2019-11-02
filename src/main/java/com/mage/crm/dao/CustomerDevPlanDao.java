package com.mage.crm.dao;

import com.mage.crm.query.CustomerDevPlanQuery;
import com.mage.crm.vo.CustomerDevPlan;

import java.util.List;

public interface CustomerDevPlanDao {
    /**
     * 通过营销机会id查询开发计划项
     * @param saleChanceId
     * @return
     */
    List<CustomerDevPlan> queryCusDevPlansBySaleChanceId(Integer saleChanceId);

    /**
     * 添加营销计划
     * @param customerDevPlan
     * @return
     */
    int insert(CustomerDevPlan customerDevPlan);

    /**
     * 更新营销计划
     * @param customerDevPlan
     * @return
     */
    int update(CustomerDevPlan customerDevPlan);

    /**
     * 删除客户开发计划
     * @param id
     * @return
     */
    int delete(Integer id);
}
