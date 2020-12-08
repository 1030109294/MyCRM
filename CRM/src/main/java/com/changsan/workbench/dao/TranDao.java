package com.changsan.workbench.dao;

import com.changsan.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int saveTran(Tran tran);

    int save(Tran tran);

    int totalTran(Map<String, Object> map);

    List<Tran> getAllTran(Map<String, Object> map);

    Tran getTranById(String id);

    int changeStage(Tran tran);

    List<Map> getIcon();

    int getTotal();
}
