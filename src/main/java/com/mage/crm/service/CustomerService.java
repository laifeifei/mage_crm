package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.CustomerDao;
import com.mage.crm.dao.CustomerLossDao;
import com.mage.crm.dto.CustomerDto;
import com.mage.crm.query.CustomerGCQuery;
import com.mage.crm.query.CustomerQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.Customer;
import com.mage.crm.vo.CustomerLoss;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {
    @Resource
    private CustomerDao customerDao;
    @Resource
    private CustomerLossDao customerLossDao;

    /**
     * 查询所有客户信息
     * @return
     */
    public List<Customer> queryAllCustomers(){
        return customerDao.queryAllCustomers();
    }

    /**
     * 通过条件查询客户信息
     * @param customerQuery
     * @return
     */
    public Map<String, Object> queryCustomersByParams(CustomerQuery customerQuery) {
        //分页
        //设置分页开始页和每页的数据数
        PageHelper.startPage(customerQuery.getPage(),customerQuery.getRows());
        //查询数据
        List<Customer> customerList = customerDao.queryCustomersByParams(customerQuery);
        PageInfo<Customer> pageInfo = new PageInfo<>(customerList);
        //声明一个map集合
        Map<String,Object> map = new HashMap<>();
        map.put("total",pageInfo.getTotal());
        map.put("rows",pageInfo.getList());
        return map;
    }

    /**
     * 添加客户信息
     * @param customer
     */
    public void insert(Customer customer) {
        //非空判断
        checkParams(customer.getName(),customer.getFr(),customer.getPhone());
        //设置为有效
        customer.setIsValid(1);
        //设置创建时间
        customer.setCreateDate(new Date());
        //设置更新时间
        customer.setUpdateDate(new Date());
        //设置状态
        customer.setState(0);
        //设置订单编号
        customer.setKhno("KH"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        //插入数据
        AssertUtil.isTrue(customerDao.insert(customer)<1,"客户插入失败");
    }

    /**
     * 非空判断
     * @param customerName
     * @param fr
     * @param phone
     */
    public void checkParams(String customerName,String fr,String phone){
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(fr),"法人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"联系电话不能为空");
    }

    public void update(Customer customer) {
        //非空判断
        checkParams(customer.getName(),customer.getFr(),customer.getPhone());
        //设置更新时间
        customer.setUpdateDate(new Date());
        //修改数据
        AssertUtil.isTrue(customerDao.update(customer)<1,"客户信息修改失败");
    }

    public void delete(Integer[] id) {
        AssertUtil.isTrue(customerDao.delete(id)<id.length,"客户信息删除失败");
    }

    public Customer queryCustomerById(Integer id) {
        return customerDao.queryCustomerById(id);
    }

    /**
     * 更新客户为流失状态
     */
    public void updateCustomerLossState() {
        //查询 客户创建日期 超过6个月 1.没有产生任何订单 2.产生订单 最后一次下单日期距离现今超过六个月
        List<CustomerLoss> customerLossList = customerDao.queryCustomerLoss();
        if(customerLossList!=null){
            for(CustomerLoss customerLoss:customerLossList){
                //设置为有效状态
                customerLoss.setIsValid(1);
                //设置创建时间
                customerLoss.setCreateDate(new Date());
                //设置更新时间
                customerLoss.setUpdateDate(new Date());
                //设置为暂缓流失
                customerLoss.setState(0);
            }
            //将暂缓流失的客户添加到客户流失表中
            AssertUtil.isTrue(customerLossDao.insertBatch(customerLossList)<1,"客户流失数据添加失败");
        }
    }

    public Map<String, Object> queryCustomersContribution(CustomerGCQuery customerGCQuery) {
        PageHelper.startPage(customerGCQuery.getPage(),customerGCQuery.getRows());
        List<CustomerDto> customerDtoList = customerDao.queryCustomersContribution(customerGCQuery);
        PageInfo<CustomerDto> customerDtoPageInfo = new PageInfo<>(customerDtoList);
        Map<String,Object> map = new HashMap<>();
        map.put("total",customerDtoPageInfo.getTotal());
        map.put("rows",customerDtoPageInfo.getList());
        return map;
    }

    public Map<String, Object> queryCustomerGC() {
        //查询数据
        List<CustomerDto> customerDtoList = customerDao.queryCustomerGC();
        //准备x,y轴坐标的数组
        String[] levels = null;
        Integer[] counts = null;
        //创建map集合
        Map<String,Object> map = new HashMap<>();
        //设置默认状态码
        map.put("code",300);
        //对数据进行非空判断，防止空指针异常
        if(customerDtoList!=null&&customerDtoList.size()>0){
            //初始化坐标数组
            levels = new String[customerDtoList.size()];
            counts = new Integer[customerDtoList.size()];
            //循环获取坐标数据
            for(int i=0;i<customerDtoList.size();i++){
                CustomerDto customerDto = customerDtoList.get(i);
                levels[i] = customerDto.getLevel();
                counts[i] = customerDto.getCount();
            }
            map.put("code",200);
        }
        //将获取到的值添加到集合中
        map.put("levels",levels);
        map.put("counts",counts);
        return map;
    }
}
