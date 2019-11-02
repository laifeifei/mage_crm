package com.mage.crm.dao;

import com.mage.crm.query.CustomerRepriQuery;
import com.mage.crm.vo.CustomerReprieve;

import java.util.List;

public interface CustomerReprieveDao {
    List<CustomerReprieve> customerReprieveByLossId(CustomerRepriQuery customerRepriQuery);

    int  insertReprive(CustomerReprieve customerReprieve);

    int updateReprive(CustomerReprieve customerReprieve);

    int delete(Integer id);
}
