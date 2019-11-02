package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.CustomerDao;
import com.mage.crm.dao.CustomerLossDao;
import com.mage.crm.query.CustomerLossQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.CustomerLoss;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerLossService {
    @Resource
    private CustomerLossDao customerLossDao;
    @Resource
    private CustomerDao customerDao;
    public Map<String, Object> queryCustomerLossesByParams(CustomerLossQuery customerLossQuery) {
        //分页
        PageHelper.startPage(customerLossQuery.getPage(),customerLossQuery.getRows());
        List<CustomerLoss> customerLossList = customerLossDao.queryCustomerLossesByParams(customerLossQuery);
        PageInfo<CustomerLoss> customerLossPageInfo = new PageInfo<>(customerLossList);
        Map<String,Object> map = new HashMap<>();
        map.put("total",customerLossPageInfo.getTotal());
        map.put("rows",customerLossPageInfo.getList());
        return map;
    }

    public CustomerLoss queryCustomerLossesById(Integer lossId) {
        return customerLossDao.queryCustomerLossesById(lossId);
    }

    public void updateCustomerLossState(Integer lossId, String lossReason) {
        AssertUtil.isTrue(StringUtils.isBlank(lossReason),"流失原因不能为空");
        CustomerLoss customerLoss = queryCustomerLossesById(lossId);
        AssertUtil.isTrue(customerLoss==null,"流失记录不存在");
        AssertUtil.isTrue(customerLossDao.updateCustomerLossState(lossId,lossReason) <1,"操作失败");
        AssertUtil.isTrue(customerDao.updateCustomerState(customerLoss.getCusNo()) <1,"操作失败");
    }
}
