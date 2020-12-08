package com.changsan.workbench.service;

import com.changsan.workbench.domain.Tran;
import com.changsan.workbench.domain.TranHistory;
import com.changsan.workbench.vo.PaginationVo;

import java.util.List;
import java.util.Map;

public interface TranService {
    boolean save(Tran tran , String customerName , String createBy);

    PaginationVo<Tran> pageList(Map<String, Object> map);

    Tran getTranById(String id);

    List<TranHistory> getTranHistoryByTranId(String id);

    Map<String, Object> changeStage(Tran tran);

    Map<String , Object> getIcon();
}
