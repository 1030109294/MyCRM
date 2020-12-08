package com.changsan.workbench.dao;


import com.changsan.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {
    int addClue(Clue clue);

    int totalClue(Map<String, Object> map);

    List<Clue> selectAllClue(Map<String, Object> map);

    Clue getClueById(String id);

    int deleteById(String clueId);

    int deleteClueByCid(String[] id);

    int updateClue(Clue clue);
}
