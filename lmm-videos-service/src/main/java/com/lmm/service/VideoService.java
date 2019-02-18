package com.lmm.service;

import com.lmm.pojo.Videos;
import com.lmm.pojo.vo.VideosVo;
import com.lmm.utils.PagedResult;

import java.util.List;

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
    public PagedResult getAllVideos(Videos video,Integer isSaveRecord,
                                    Integer page,Integer pageSize);

    /**
     * @description: 获取热搜词
     * @return
     */
    public List<String> getHotWords();

    /**
     * 用户喜欢的视频/点赞
     * @param userId
     * @param videoId
     * @param videoCreaterId
     */
    public void userLikeVideo(String userId,String videoId,String videoCreaterId);

    /**
     * 取消点赞
     * @param userId
     * @param videoId
     * @param videoCreaterId
     */
    public void userUnLikeVideo(String userId,String videoId,String videoCreaterId);

    /**
     * 查询关注者的所有视频
     * @param userId
     * @return
     */
    public PagedResult queryMyFollowVideos(String userId,Integer page,Integer pageSize);

    /**
     * 查询用户喜欢的视频
     * @param userId
     * @return
     */
    public PagedResult queryMyLikeVideos(String userId,Integer page,Integer pageSize);
}
