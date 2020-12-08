package com.changsan.workbench.service;

import com.changsan.workbench.domain.Activity;
import com.changsan.workbench.domain.Clue;
import com.changsan.workbench.domain.Customer;
import com.changsan.workbench.domain.Tran;
import com.changsan.workbench.vo.PaginationVo;

import java.util.List;
import java.util.Map;

public interface ClueService {
    boolean addClue(Clue clue);

    PaginationVo<Clue> pageList(Map<String, Object> map);

    Clue getClueById(String id);

    List<Activity> getActivityByClueId(String id);

    boolean deleteRelatedById(String id);

    boolean addClueActivityRelation(String cid, String[] aid);

    boolean conversion(String flag , String clueId, Tran tran , String createBy);

    boolean delete(String[] id);

    Map<String, Object> getClueAndUserList(String id);

    int updateClue(Clue clue);
}
