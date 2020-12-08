package com.changsan.workbench.service.impl;

import com.changsan.settings.dao.UserDao;
import com.changsan.settings.domain.User;
import com.changsan.utils.DateTimeUtil;
import com.changsan.utils.UUIDUtil;
import com.changsan.workbench.dao.*;
import com.changsan.workbench.domain.*;
import com.changsan.workbench.service.ClueService;
import com.changsan.workbench.vo.PaginationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 17925
 * @date 2020/11/21 15:55
 */
@Service("clueService")
public class ClueServiceImpl implements ClueService {
    //线索相关表
    @Autowired
    private ClueDao clueDao;
    @Autowired
    private ClueRemarkDao clueRemarkDao;
    @Autowired
    private ClueActivityRelationDao clueActivityRelationDao;
    //活动相关表
    @Autowired
    private ActivityDao activityDao;
    //联系人相关表
    @Autowired
    private ContactsDao contactsDao;
    @Autowired
    private ContactsRemarkDao contactsRemarkDao;
    @Autowired
    private ContactsActivityRelationDao contactsActivityRelationDao;
    //客户相关表
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private CustomerRemarkDao customerRemarkDao;
    //交易相关表
    @Autowired
    private TranDao tranDao;
    @Autowired
    private TranHistoryDao tranHistoryDao;
@Autowired
    private UserDao userDao;

    @Override
    public boolean addClue(Clue clue) {
        boolean flag = true;
      int result =   clueDao.addClue(clue);
      if (result != 1){
          flag = false;
      }
        return flag;
    }

    @Override
    public PaginationVo<Clue> pageList(Map<String, Object> map) {
        PaginationVo<Clue>  paginationVo =  new PaginationVo<>();
        int total =  clueDao.totalClue(map);
        List<Clue> clueList =  clueDao.selectAllClue(map);
        paginationVo.setTotal(total);
        paginationVo.setTotalList(clueList);
        return paginationVo;
    }

    @Override
    public Clue getClueById(String id) {
      return   clueDao.getClueById(id);
    }

    @Override
    public List<Activity> getActivityByClueId(String id) {
       return activityDao.getActivityByClueId(id);
    }

    @Override
    public boolean deleteRelatedById(String id) {
        boolean flag = true;
        int result = clueActivityRelationDao.deleteRelatedById(id);
        if (result != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean addClueActivityRelation(String cid, String[] aid) {
        boolean flag = true;
        for (String aaid : aid){
            String id = UUIDUtil.getUUID();
            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
           clueActivityRelation.setActivityId(aaid);
           clueActivityRelation.setClueId(cid);
           clueActivityRelation.setId(id);
            int result = clueActivityRelationDao.addClueActivityRelation(clueActivityRelation);
            if (result != 1){
                flag = false;
            }
        }
        return flag;
        }

    @Override
    public boolean conversion(String flag , String clueId, Tran tran , String createBy) {
        String createTime = DateTimeUtil.getSysTime();
        boolean result = true;
        //根据clueId获取线索
        Clue clue = clueDao.getClueById(clueId);
        String company = clue.getCompany();
        //根据公司名获取客户信息
        Customer customer1 = customerDao.getCustomerByName(company);
        //判断是否存在此客户，不存在就新建一个
        if (customer1 == null){
             customer1 = new Customer();
             customer1.setWebsite(clue.getWebsite());
             customer1.setPhone(clue.getPhone());
             customer1.setOwner(clue.getOwner());
             customer1.setNextContactTime(clue.getNextContactTime());
             customer1.setName(company);
             customer1.setId(UUIDUtil.getUUID());
             customer1.setDescription(clue.getDescription());
             customer1.setCreateTime(createTime);
             customer1.setCreateBy(createBy);
             customer1.setContactSummary(clue.getContactSummary());
             customer1.setAddress(clue.getAddress());
             int result1 = customerDao.save(customer1);
            System.out.println(result1);
             if (result1 != 1){
                 result = false;
             }
        }
     //线索删除前将其中信息传给联系人中
        Contacts contacts = new Contacts();
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setId(UUIDUtil.getUUID());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(customer1.getId());
        contacts.setCreateTime(createTime);
        contacts.setCreateBy(createBy);
        contacts.setBirth(null);
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAppellation(clue.getAppellation());
        contacts.setAddress(clue.getAddress());
        int result2 = contactsDao.save(contacts);
        System.out.println(result2);
        if (result2 != 1){
            result = false;
        }
        //线索转换 将线索备注中的信息发送到客户和联系人中
        List<ClueRemark> clueRemarkList =  clueRemarkDao.getClueRemarkByClueId(clueId);
        for (ClueRemark clueRemark : clueRemarkList){
            String noteContent = clueRemark.getNoteContent();
            //信息传入到客户中
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setNoteContent(noteContent);
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setEditFlag("0");
            customerRemark.setCustomerId(customer1.getId());
            customerRemark.setCreateTime(createTime);
            customerRemark.setCreateBy(createBy);
            int result3 = customerRemarkDao.save(customerRemark);
            System.out.println(result3);
            if (result3 != 1){
                result = false;
            }
            //信息传入到联系人中
           ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setEditFlag("0");
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setCreateBy(createBy);
           int result4 = contactsRemarkDao.save(contactsRemark);
            System.out.println(result4);
            if (result4 != 1){
                result = false;
            }
        }
        //"线索和市场活动"的关系转换到"联系人和市场活动"的关系表中
        List<ClueActivityRelation> contactsActivityRelations = clueActivityRelationDao.getActivityIdByClueId(clueId);
        for (ClueActivityRelation clueActivityRelation : contactsActivityRelations){
            String activityId = clueActivityRelation.getActivityId();
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(contacts.getId());
            int result5 = contactsActivityRelationDao.save(contactsActivityRelation);
            System.out.println(result5);
            if (result5 != 1){
                result = false;
            }
        }
        if ("1".equals(flag)) {
            //创建交易
            tran.setSource(contacts.getSource());
            tran.setDescription(contacts.getDescription());
            tran.setContactsId(contacts.getId());
            tran.setContactSummary(contacts.getContactSummary());
            tran.setCreateBy(createBy);
            tran.setCreateTime(createTime);
            tran.setCustomerId(customer1.getId());
            tran.setId(UUIDUtil.getUUID());
            tran.setOwner(clue.getOwner());
            tran.setNextContactTime(contacts.getNextContactTime());
            int result6 = tranDao.saveTran(tran);
            System.out.println(result6);
            if (result6 == 1) {
                TranHistory tranHistory = new TranHistory();
                tranHistory.setTranId(tran.getId());
                tranHistory.setStage(tran.getStage());
                tranHistory.setMoney(tran.getMoney());
                tranHistory.setId(UUIDUtil.getUUID());
                tranHistory.setExpectedDate(tran.getExpectedDate());
                tranHistory.setCreateTime(createTime);
                tranHistory.setCreateBy(createBy);
                int result7 = tranHistoryDao.save(tranHistory);
                System.out.println(result7);
                if (result7 != 1) {
                    result = false;
                }
            } else {
                result = false;
            }
        }
         //删除线索备注
        int result8 = clueRemarkDao.deleteRemark(clueId);
        System.out.println(result8);
        if (result8 != 1){
            result = false;
        }
        //删除线索和活动的关联
        int result9 =  clueActivityRelationDao.deleteByClueId(clueId);
        System.out.println(result9);
        if (result9 != 1){
            result = false;
        }
        //删除线索
        int result10 =    clueDao.deleteById(clueId);
        System.out.println(result10);
        if (result10 != 1){
            result = false;
        }
        return result;
    }

    @Override
    public boolean delete(String[] id) {

        boolean flag = true;
        //查询remark中需要删除的id有多少记录
        int count1 = clueRemarkDao.getCountByAid(id);
        //删除了remark中多少条记录
        int count2 = clueRemarkDao.deleteRemarkByAid(id);
        //判断存在的记录跟删除的数量是否相等
        if (count1 != count2){
            flag = false;
        }
        //删除activity中所对应id的记录
        int count3 = clueDao.deleteClueByCid(id);
        //判断删除的记录于需要删除的数量是否相同
        if (count3 != id.length){
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getClueAndUserList(String id) {
        Map<String , Object> map = new HashMap<>();
        List<User> uList =   userDao.selectAllUser();
        Clue a = clueDao.getClueById(id);
        map.put("uList" , uList);
        map.put("a" , a);
        return map;
    }

    @Override
    public int updateClue(Clue clue) {
        return clueDao.updateClue(clue);
    }
}
