package com.lmm.pojo.vo;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class VideosVo {
    private String id;
    private String userId;
    private String audioId;
    private String videoDesc;
    private String videoPath;
    private Float videoSeconds;
    private Integer videoWidth;
    private Integer videoHeight;
    private String coverPath;
    private Long likeCounts;
    private Integer status;
    private Date createTime;

    private String faceImage;
    private String nickname;

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return audio_id
     */
    public String getAudioId() {
        return audioId;
    }

    /**
     * @param audioId
     */
    public void setAudioId(String audioId) {
        this.audioId = audioId;
    }

    /**
     * @return video_desc
     */
    public String getVideoDesc() {
        return videoDesc;
    }

    /**
     * @param videoDesc
     */
    public void setVideoDesc(String videoDesc) {
        this.videoDesc = videoDesc;
    }

    /**
     * @return video_path
     */
    public String getVideoPath() {
        return videoPath;
    }

    /**
     * @param videoPath
     */
    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    /**
     * @return video_seconds
     */
    public Float getVideoSeconds() {
        return videoSeconds;
    }

    /**
     * @param videoSeconds
     */
    public void setVideoSeconds(Float videoSeconds) {
        this.videoSeconds = videoSeconds;
    }

    /**
     * @return video_width
     */
    public Integer getVideoWidth() {
        return videoWidth;
    }

    /**
     * @param videoWidth
     */
    public void setVideoWidth(Integer videoWidth) {
        this.videoWidth = videoWidth;
    }

    /**
     * @return video_height
     */
    public Integer getVideoHeight() {
        return videoHeight;
    }

    /**
     * @param videoHeight
     */
    public void setVideoHeight(Integer videoHeight) {
        this.videoHeight = videoHeight;
    }

    /**
     * @return cover_path
     */
    public String getCoverPath() {
        return coverPath;
    }

    /**
     * @param coverPath
     */
    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    /**
     * @return like_counts
     */
    public Long getLikeCounts() {
        return likeCounts;
    }

    /**
     * @param likeCounts
     */
    public void setLikeCounts(Long likeCounts) {
        this.likeCounts = likeCounts;
    }

    /**
     * 获取视频状态：1、发布成功、2、禁止播放，管理员操作
     *
     * @return status - 视频状态：1、发布成功、2、禁止播放，管理员操作
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置视频状态：1、发布成功、2、禁止播放，管理员操作
     *
     * @param status 视频状态：1、发布成功、2、禁止播放，管理员操作
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}