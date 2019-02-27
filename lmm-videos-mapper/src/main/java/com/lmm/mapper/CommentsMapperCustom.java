package com.lmm.mapper;

import com.lmm.pojo.Comments;
import com.lmm.pojo.vo.CommentsVO;
import com.lmm.utils.MyMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentsMapperCustom extends MyMapper<Comments> {
    public List<CommentsVO> queryComments(String videoId);
}
