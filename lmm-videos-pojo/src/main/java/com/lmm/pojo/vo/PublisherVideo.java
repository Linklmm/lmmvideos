package com.lmm.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "用户对象",description = "这是用户对象")
public class PublisherVideo {
    public UsersVo publisher;
    public boolean userLikeVideo;

    public UsersVo getPublisher() {
        return publisher;
    }

    public void setPublisher(UsersVo publisher) {
        this.publisher = publisher;
    }

    public boolean isUserLikeVideo() {
        return userLikeVideo;
    }

    public void setUserLikeVideo(boolean userLikeVideo) {
        this.userLikeVideo = userLikeVideo;
    }
}