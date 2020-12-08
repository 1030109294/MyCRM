package com.changsan.workbench.dao;

import com.changsan.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getClueRemarkByClueId(String clueId);

    int deleteRemark(String clueId);

    int getCountByAid(String[] id);

    int deleteRemarkByAid(String[] id);
}
