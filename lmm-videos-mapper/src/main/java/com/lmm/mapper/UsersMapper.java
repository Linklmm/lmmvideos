package com.lmm.mapper;

import com.lmm.pojo.Users;
import com.lmm.utils.MyMapper;
import org.springframework.stereotype.Repository;

//@Repository
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
}