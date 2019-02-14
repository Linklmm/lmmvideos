package com.lmm.mapper;

import com.lmm.pojo.Videos;
import com.lmm.pojo.vo.VideosVo;
import com.lmm.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
//@Repository
public interface VideosMapperCustom extends MyMapper<Videos> {

    public List<VideosVo> queryAllVideos(@Param("videoDesc") String videoDesc);

    /**
     * @description: 对喜欢的视频的数量进行累加
     * @param videoId
     */
    public void addVideoLikeCount(String videoId);

    /**
     * @description： 对喜欢的视频的数量进行累减
     * @param videoId
     */
    public void reduceVideoLikeCount(String videoId);
}