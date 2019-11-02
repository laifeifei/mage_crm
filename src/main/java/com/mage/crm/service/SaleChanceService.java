package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.SaleChanceDao;
import com.mage.crm.query.SaleChanceQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService {
    @Resource
    private SaleChanceDao saleChanceDao;
    public Map<String,Object> querySaleChancesByParams(SaleChanceQuery saleChanceQuery){
        //分页
        //获取分页的页面值和每页显示的数
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getRows());
        //查询出数据
        List<SaleChance> saleChances = saleChanceDao.querySaleChancesByParams(saleChanceQuery);
        //创建分页类
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChances);
        //获取页面显示的数据
        List<SaleChance> list = pageInfo.getList();
        //创建map对象
        Map<String,Object> map = new HashMap<String, Object>();
        //将数据存入map中
        map.put("rows",list);
        map.put("total",pageInfo.getTotal());
        //返回map
        return map;
    }

    public void insert(SaleChance saleChance) {
        //非空校验
        checkParams(saleChance.getLinkMan(),saleChance.getLinkPhone(),saleChance.getCgjl());
        //设置添加的营销机会为有效
        saleChance.setIsValid(1);
        //设置创建时间
        saleChance.setCreateDate(new Date());
        //设置更新时间
        saleChance.setUpdateDate(new Date());
        //设置为未开发的状态，初始状态
        saleChance.setDevResult(0);
        //设置分配状态
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            //未分配
            saleChance.setState(0);
        }else{
            //已分配
            saleChance.setState(1);
            //更新分配时间
            saleChance.setAssignTime(new Date());
        }
        //添加营销机会
        AssertUtil.isTrue(saleChanceDao.insert(saleChance)<1,"营销机会添加失败");
    }

    @RequestMapping("update")
    @ResponseBody
    public void update(SaleChance saleChance) {
        //非空校验
        checkParams(saleChance.getLinkMan(),saleChance.getLinkPhone(),saleChance.getCgjl());
        //设置更新时间
        saleChance.setUpdateDate(new Date());
        //设置分配状态
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            //未分配
            saleChance.setState(0);
        }else{
            //已分配
            saleChance.setState(1);
            //更新分配时间
            saleChance.setAssignTime(new Date());
        }
        //修改营销机会
        AssertUtil.isTrue(saleChanceDao.update(saleChance)<1,"营销机会修改失败");
    }

    /**
     * 非空校验的方法
     * @param linkMan
     * @param linkPhone
     * @param cgjl
     */
    public void checkParams(String linkMan,String linkPhone,String cgjl){
        //非空校验
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系电话不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(cgjl),"成功几率不能为空");
    }

    /**
     * 营销机会删除
     * @param id
     */
    public void delete(Integer[] id) {
        //受影响行数小于数组长度删除失败
        AssertUtil.isTrue(saleChanceDao.delete(id) < id.length,"营销机会删除失败");
    }

    /**
     * 根据id查找营销机会信息
     * @param id
     * @return
     */
    public SaleChance querySaleChancesById(Integer id) {
        return saleChanceDao.querySaleChancesById(id);
    }

    /**
     * 更新营销机会的开发状态
     * @param devResult
     * @param saleChanceId
     */
    public void updateSaleChanceDevResult( Integer saleChanceId,Integer devResult) {
        saleChanceDao.updataDevResult(saleChanceId,devResult);
    }
}
