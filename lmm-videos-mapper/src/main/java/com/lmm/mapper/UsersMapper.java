package com.lmm.mapper;

import com.lmm.pojo.Users;
import com.lmm.utils.MyMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersMapper extends MyMapper<Users> {

    /**
     * @Description：用户作品喜欢书增加
     * @param userId
     */
    public void addReceiveLikeCount(String userId);

    /**
     * @description： 用户作品受喜欢数减少
     * @param userId
     */
    public void reduceReceiveLikeCount(String userId);

    /**
     * 增加粉丝数
     * @param userId
     */
    public void addFansCount(String userId);

    /**
     * 减少粉丝数
     * @param userId
     */
    public void reduceFansCount(String userId);

    /**
     * 增加关注数
     * @param userId
     */
    public void addFollowerCount(String userId);

    /**
     * 减少关注数
     * @param userId
     */
    public void reduceFollowerCount(String userId);
}