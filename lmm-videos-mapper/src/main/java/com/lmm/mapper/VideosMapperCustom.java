package com.lmm.mapper;

import com.lmm.pojo.Videos;
import com.lmm.pojo.vo.VideosVo;
import com.lmm.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosMapperCustom extends MyMapper<Videos> {

    public List<VideosVo> queryAllVideos(@Param("videoDesc") String videoDesc);
}