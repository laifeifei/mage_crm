package com.mage.crm.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.CustomerReprieveDao;
import com.mage.crm.query.CustomerRepriQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.CustomerLoss;
import com.mage.crm.vo.CustomerReprieve;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerReprieveService {
    @Resource
    private CustomerReprieveDao customerReprieveDao;
    @Resource
    private CustomerLossService customerLossService;
    public Map<String, Object> customerReprieveByLossId(CustomerRepriQuery customerRepriQuery) {
        PageHelper.startPage(customerRepriQuery.getPage(),customerRepriQuery.getRows());
        List<CustomerReprieve> customerReprieveList = customerReprieveDao.customerReprieveByLossId(customerRepriQuery);
        PageInfo<CustomerReprieve> customerReprievePageInfo = new PageInfo<>(customerReprieveList);
        Map<String,Object> map = new HashMap<>();
        map.put("total",customerReprievePageInfo.getTotal());
        map.put("rows",customerReprievePageInfo.getList());
        return map;
    }

    public void insertReprive(CustomerReprieve customerReprieve) {
        //非空判断
        checkParams(customerReprieve.getLossId(),customerReprieve.getMeasure());
        customerReprieve.setIsValid(1);
        customerReprieve.setCreateDate(new Date());
        customerReprieve.setUpdateDate(new Date());
        AssertUtil.isTrue(customerReprieveDao.insertReprive(customerReprieve)<1,"暂缓措施添加失败");
    }
    public void checkParams(Integer lossId ,String measure){
        AssertUtil.isTrue(StringUtils.isBlank(measure),"暂缓措施不能为空");
        //查询流失记录是否存在
        CustomerLoss customerLoss = customerLossService.queryCustomerLossesById(lossId);
        AssertUtil.isTrue(lossId==null||customerLoss==null,"流失记录不存在");
    }

    public void updateReprive(CustomerReprieve customerReprieve) {
        //非空判断
        checkParams(customerReprieve.getLossId(),customerReprieve.getMeasure());
        customerReprieve.setUpdateDate(new Date());
        AssertUtil.isTrue(customerReprieveDao.updateReprive(customerReprieve)<1,"暂缓措施更新失败");
    }

    public void delete(Integer id) {
        AssertUtil.isTrue(customerReprieveDao.delete(id)<1,"暂缓措施删除失败");
    }
}
