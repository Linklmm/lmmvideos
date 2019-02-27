package com.lmm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lmm.mapper.*;
import com.lmm.pojo.Comments;
import com.lmm.pojo.SearchRecords;
import com.lmm.pojo.UsersLikeVideos;
import com.lmm.pojo.Videos;
import com.lmm.pojo.vo.CommentsVO;
import com.lmm.pojo.vo.VideosVo;
import com.lmm.service.VideoService;
import com.lmm.utils.PagedResult;
import com.lmm.utils.TimeAgoUtils;
import org.n3r.idworker.Sid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {
    private static Logger logger = LoggerFactory.getLogger(VideoService.class);

    @Autowired
    private VideosMapper videosMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private VideosMapperCustom videosMapperCustom;
    @Autowired
    private CommentsMapperCustom commentsMapperCustom;
    @Autowired
    private SearchRecordsMapper searchRecordsMapper;
    @Autowired
    private CommentsMapper commentsMapper;
    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;
    @Autowired
    private UsersMapper usersMapper;


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveVideo(Videos video) {
        String id = sid.nextShort();
        video.setId(id);
        videosMapper.insertSelective(video);

        return id;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateVideo(String videoId, String coverPath) {

        Videos video = new Videos();
        video.setId(videoId);
        video.setCoverPath(coverPath);
        videosMapper.updateByPrimaryKeySelective(video);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedResult getAllVideos(Videos video, Integer isSaveRecord,
                                    Integer page, Integer pageSize) {
        logger.info("进入service，video:{},isSaveRecord:{},page:{},pageSize:{}",
                video,isSaveRecord,page,pageSize);
        String desc =null;
        String userId =null;
        if (video!=null){
        //保存热搜词
        desc = video.getVideoDesc();
        userId = video.getUserId();
        }
        if (isSaveRecord != null && isSaveRecord == 1) {
            SearchRecords record = new SearchRecords();
            String recordId = sid.nextShort();
            record.setId(recordId);
            record.setContent(desc);
            searchRecordsMapper.insert(record);
        }

        PageHelper.startPage(page, pageSize);
        List<VideosVo> list = videosMapperCustom.queryAllVideos(desc,userId);

        PageInfo<VideosVo> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<String> getHotWords() {
        return searchRecordsMapper.getHotwords();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userLikeVideo(String userId, String videoId, String videoCreaterId) {
        String likeId = sid.nextShort();

        //1.保存用户和视频的喜欢点赞关联关系表
        UsersLikeVideos ulv = new UsersLikeVideos();
        ulv.setId(likeId);
        ulv.setUserId(userId);
        ulv.setVideoId(videoId);

        usersLikeVideosMapper.insert(ulv);

        //2.视频增加喜欢数

        videosMapperCustom.addVideoLikeCount(videoId);
        //3.用户作品受喜欢数增加
        usersMapper.addReceiveLikeCount(userId);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userUnLikeVideo(String userId, String videoId, String videoCreaterId) {
        String likeId = sid.nextShort();

        //1.删除用户和视频的喜欢点赞关联关系表
        Example example = new Example(UsersLikeVideos.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("videoId",videoId);
        usersLikeVideosMapper.deleteByExample(example);

        //2.视频减少喜欢数

        videosMapperCustom.reduceVideoLikeCount(videoId);
        //3.用户作品受喜欢数减少
        usersMapper.reduceReceiveLikeCount(userId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult queryMyFollowVideos(String userId,Integer page,Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<VideosVo> list = videosMapperCustom.queryMyFollowVideos(userId);

        PageInfo<VideosVo> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setPage(page);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult queryMyLikeVideos(String userId,Integer page,Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<VideosVo> list = videosMapperCustom.queryMyLikeVideos(userId);

        PageInfo<VideosVo> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setPage(page);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComment(Comments comments) {
        String id = sid.nextShort();
        comments.setId(id);
        comments.setCreateTime(new Date());
        commentsMapper.insert(comments);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult getAllComments(String videoId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);

        List<CommentsVO> list = commentsMapperCustom.queryComments(videoId);

        for (CommentsVO c:list){
            String timeAgo = TimeAgoUtils.format(c.getCreateTime());
            c.setTimeAgoStr(timeAgo);
        }

        PageInfo<CommentsVO> pageList = new PageInfo<>(list);

        PagedResult grid = new PagedResult();
        grid.setTotal(pageList.getPages());
        grid.setRows(list);
        grid.setPage(page);
        grid.setRecords(pageList.getTotal());

        return grid;
    }
}
