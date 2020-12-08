package com.changsan.settings.dao;

import com.changsan.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getDicValueByCode(String code);
}
