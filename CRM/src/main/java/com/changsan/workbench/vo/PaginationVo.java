package com.changsan.workbench.vo;

import java.util.List;

/**
 * @author 17925
 * @date 2020/11/18 20:10
 */
public class PaginationVo<T> {
    private int total;
    private List<T> totalList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getTotalList() {
        return totalList;
    }

    public void setTotalList(List<T> totalList) {
        this.totalList = totalList;
    }

    @Override
    public String toString() {
        return "PaginationVo{" +
                "total=" + total +
                ", totalList=" + totalList +
                '}';
    }
}
