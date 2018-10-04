package com.lmm.service.impl;

import com.lmm.mapper.VideosMapper;
import com.lmm.pojo.Videos;
import com.lmm.service.VideoService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideosMapper videosMapper;

    @Autowired
    private Sid sid;


    @Override
    public String saveVideo(Videos video) {
        String id=sid.nextShort();
        video.setId(id);
        videosMapper.insertSelective(video);

        return id;
    }
}
