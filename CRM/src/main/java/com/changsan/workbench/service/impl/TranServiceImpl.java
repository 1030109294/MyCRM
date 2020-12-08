package com.changsan.workbench.service.impl;

import com.changsan.utils.DateTimeUtil;
import com.changsan.utils.UUIDUtil;
import com.changsan.workbench.dao.ContactsDao;
import com.changsan.workbench.dao.CustomerDao;
import com.changsan.workbench.dao.TranDao;
import com.changsan.workbench.dao.TranHistoryDao;
import com.changsan.workbench.domain.Contacts;
import com.changsan.workbench.domain.Customer;
import com.changsan.workbench.domain.Tran;
import com.changsan.workbench.domain.TranHistory;
import com.changsan.workbench.service.TranService;
import com.changsan.workbench.vo.PaginationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 17925
 * @date 2020/11/21 16:10
 */
@Service("tranService")
public class TranServiceImpl implements TranService {
    @Autowired
    private TranDao tranDao;
    @Autowired
    private TranHistoryDao tranHistoryDao;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private ContactsDao contactsDao;
    @Override
    public boolean save(Tran tran , String customerName , String createBy) {
        boolean flag = true;
        String createTime = DateTimeUtil.getSysTime();
        Customer customer = customerDao.getCustomerByName(customerName);
        //但用户不存在时，新建用户
        if (customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateTime(createTime);
            customer.setCreateBy(createBy);
            customer.setContactSummary(tran.getContactSummary());
            customer.setDescription(tran.getDescription());
            customer.setName(customerName);
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setOwner(tran.getOwner());
            int result1 =  customerDao.save(customer);
            if (result1 != 1){
                flag = false;
            }
        }
        String cid = customer.getId();
        //创建交易
        tran.setCustomerId(cid);
        int result = tranDao.save(tran);
        if (result != 1){
            flag = false;
        }
        //创建交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setTranId(tran.getId());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateTime(createTime);
        tranHistory.setCreateBy(createBy);
        int result3 = tranHistoryDao.save(tranHistory);
        if (result3 != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVo<Tran> pageList(Map<String, Object> map) {
        PaginationVo<Tran> paginationVo = new PaginationVo<>();
        //根据用户名获取对应的id值
        int total = tranDao.totalTran(map);
        List<Tran> tranList = tranDao.getAllTran(map);
        paginationVo.setTotal(total);
        paginationVo.setTotalList(tranList);
        return paginationVo;
    }

    @Override
    public Tran getTranById(String id) {
        return tranDao.getTranById(id);
    }

    @Override
    public List<TranHistory> getTranHistoryByTranId(String id) {
        List<TranHistory> tranHistoryList = tranHistoryDao.getTranHistoryByTranId(id);
        return tranHistoryList;
    }

    @Override
    public Map<String, Object> changeStage(Tran tran) {
        Map<String, Object> map = new HashMap<>();
        boolean flag = true;
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setStage(tran.getStage());
        tranHistory.setTranId(tran.getId());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateTime(tran.getEditTime());
        tranHistory.setCreateBy(tran.getEditBy());
        int result = tranHistoryDao.save(tranHistory);
        if (result != 1){
            flag = false;
        }
        int result2 = tranDao.changeStage(tran);
        if (result2 != 1){
            flag = false;
        }
       String id =  tran.getId();
        Tran t = tranDao.getTranById(id);
        map.put("success" , flag);
        map.put("t" , t);
        return map;
    }

    @Override
    public Map<String , Object> getIcon() {
        Map<String , Object> map = new HashMap<>();
        int total = tranDao.getTotal();
        List<Map> dataList = tranDao.getIcon();
        map.put("total" , total);
        map.put("dataList" , dataList);
        return map;
    }
}
