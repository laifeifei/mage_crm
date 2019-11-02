package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.CustomerServeDao;
import com.mage.crm.dto.CustomerDto;
import com.mage.crm.dto.ServeTypeDto;
import com.mage.crm.query.CustomerServeQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.util.CookieUtil;
import com.mage.crm.vo.CustomerServe;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerServeService {
    @Resource
    private CustomerServeDao customerServeDao;

    public void insert(CustomerServe customerServe) {
        cheakCustomerServeParams(customerServe.getServeType(), customerServe.getCustomer(), customerServe.getServiceRequest());
        customerServe.setState("1");
        customerServe.setCreateDate(new Date());
        customerServe.setUpdateDate(new Date());
        customerServe.setIsValid(1);
        AssertUtil.isTrue(customerServeDao.insert(customerServe) < 1, "服务创建失败");
    }

    private void cheakCustomerServeParams(String serveType, String customer, String serviceRequest) {
        AssertUtil.isTrue(StringUtils.isBlank(serveType), "服务类型非空!");
        AssertUtil.isTrue(StringUtils.isBlank(customer), "客户名称非空!");
        AssertUtil.isTrue(StringUtils.isBlank(serviceRequest), "内容非空!");
    }

    public Map<String, Object> queryCustomerServesByParams(CustomerServeQuery customerServeQuery) {
        //分页
        PageHelper.startPage(customerServeQuery.getPage(), customerServeQuery.getRows());
        List<CustomerServe> orderList = customerServeDao.queryCustomerServesByParams(customerServeQuery.getState());
        PageInfo<CustomerServe> pageInfo = new PageInfo<>(orderList);
        Map<String, Object> map = new HashMap<>();
        map.put("total", pageInfo.getTotal());
        map.put("rows", pageInfo.getList());
        return map;
    }

    public void update(CustomerServe customerServe, HttpServletRequest httpServletRequest) {
        //设置更新时间
        customerServe.setUpdateDate(new Date());
        if (customerServe.getState().equals("2")) {
            //从Cookie中获取真实姓名,并设置
            customerServe.setAssigner(CookieUtil.getCookieValue(httpServletRequest, "trueName"));
            customerServe.setAssignTime(new Date());
        } else if (customerServe.getState().equals("3")) {
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceProce()), "处理内容不能为空");
            customerServe.setServiceProceTime(new Date());
        } else if (customerServe.getState().equals("4")) {
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceProceResult()), "处理结果不能为空");
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getMyd()), "满意度不能为空");
            customerServe.setState("5");
        }
        AssertUtil.isTrue(customerServeDao.update(customerServe) < 1, "操作失败");
    }

    public Map<String, Object> queryCustomerServeType() {
        List<ServeTypeDto> serveTypeDtoList = customerServeDao.queryCustomerServeType();
        System.out.println(serveTypeDtoList.toString());
        Map<String,Object> map = new HashMap<>();
        map.put("code",300);
        String[] types;
        ServeTypeDto[] serveTypeDtos;
        if(serveTypeDtoList!=null&&serveTypeDtoList.size()>0){
            //初始化数据
            types = new String[serveTypeDtoList.size()];
            serveTypeDtos = new ServeTypeDto[serveTypeDtoList.size()];
            for(int i = 1;i<serveTypeDtoList.size();i++){
                types[i] = serveTypeDtoList.get(i).getName();
                serveTypeDtos[i] = serveTypeDtoList.get(i);
            }
            map.put("code",200);
            map.put("types",types);
            map.put("datas",serveTypeDtos);
        }
        return map;
    }
}

