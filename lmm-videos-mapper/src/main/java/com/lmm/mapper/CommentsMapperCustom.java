package com.lmm.mapper;

import com.lmm.pojo.Comments;
import com.lmm.pojo.vo.CommentsVO;
import com.lmm.utils.MyMapper;

import java.util.List;

public interface CommentsMapperCustom extends MyMapper<Comments> {
    public List<CommentsVO> queryComments(String videoId);
}
