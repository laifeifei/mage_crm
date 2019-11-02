package com.mage.crm.dao;

import com.mage.crm.query.SaleChanceQuery;
import com.mage.crm.vo.SaleChance;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SaleChanceDao {
    /**
     * 通过条件查询
     * @param saleChanceQuery
     * @return
     */
    List<SaleChance> querySaleChancesByParams(SaleChanceQuery saleChanceQuery);

    /**
     * 添加营销机会
     * @param saleChance
     * @return
     */
    int insert(SaleChance saleChance);

    /**
     * 修改营销机会
     * @param saleChance
     * @return
     */
    int update(SaleChance saleChance);

    /**
     * 删除营销机会
     * @param id
     * @return
     */
    int delete(Integer[] id);

    /**
     * 通过id查询营销机会信息
     * @param id
     * @return
     */
    SaleChance querySaleChancesById(Integer id);

    void updataDevResult(@Param("saleChanceId") Integer saleChanceId,@Param("dev") Integer dev);

}
