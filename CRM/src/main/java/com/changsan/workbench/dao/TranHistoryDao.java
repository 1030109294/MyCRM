package com.changsan.workbench.dao;

import com.changsan.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory tranHistory);

    List<TranHistory> getTranHistoryByTranId(String id);
}
