package com.lmm.mapper;

import com.lmm.pojo.Videos;
import com.lmm.pojo.vo.VideosVo;
import com.lmm.utils.MyMapper;

import java.util.List;

public interface VideosMapperCustom extends MyMapper<Videos> {

    public List<VideosVo> queryAllVideos();
}