package com.changsan.workbench.dao;

import com.changsan.workbench.domain.ClueActivityRelation;
import com.changsan.workbench.domain.ContactsActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    int deleteRelatedById(String id);

    int addClueActivityRelation(ClueActivityRelation clueActivityRelation);

    List<ClueActivityRelation> getActivityIdByClueId(String clueId);

    int deleteByClueId(String clueId);
}
