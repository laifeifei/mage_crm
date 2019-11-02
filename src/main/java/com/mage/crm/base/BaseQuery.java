package com.mage.crm.base;

/**
 * 分页相关属性
 */
public class BaseQuery {
    private Integer page;//页码
    private Integer rows;//每页的数据条数

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
