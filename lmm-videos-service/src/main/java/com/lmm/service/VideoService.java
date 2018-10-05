package com.lmm.service;

import com.lmm.pojo.Videos;
import com.lmm.utils.PagedResult;

public interface VideoService {
    /**
     * @Description: 保存视频
     *
     */
    public String saveVideo(Videos video);

    /**
     * @description: 修改视频的封面
     *
     */

    public void updateVideo(String videoId,String coverPath);

    /**
     * @description: 分页查询视频列表
     * */
    public PagedResult getAllVideos(Integer page,Integer pageSize);
}
