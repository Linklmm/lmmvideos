package com.lmm.mapper;

import com.lmm.pojo.SearchRecords;
import com.lmm.utils.MyMapper;

import java.util.List;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
    public List<String> getHotwords();
}