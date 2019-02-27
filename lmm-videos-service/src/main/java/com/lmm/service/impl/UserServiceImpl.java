/**
 * @program: lmmvideos
 * @description: UserService实现类
 * @author: minmin.liu
 * @create: 2018-09-28 15:34
 **/
package com.lmm.service.impl;

import com.lmm.mapper.UsersFansMapper;
import com.lmm.mapper.UsersLikeVideosMapper;
import com.lmm.mapper.UsersMapper;
import com.lmm.mapper.UsersReportMapper;
import com.lmm.pojo.Users;
import com.lmm.pojo.UsersFans;
import com.lmm.pojo.UsersLikeVideos;
import com.lmm.pojo.UsersReport;
import com.lmm.service.UserService;
import com.lmm.utils.IMoocJSONResult;
import org.apache.commons.lang3.StringUtils;
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
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;
    @Autowired
    private UsersFansMapper usersFansMapper;
    @Autowired
    private UsersReportMapper usersReportMapper;
    @Autowired
    private Sid sid;
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUserFanRelation(String userId, String fanId) {
        String relId = sid.nextShort();
        logger.info("进入service增加粉丝数入参:userId:{}，fanId:{}",
                userId, fanId);

        UsersFans usersFans = new UsersFans();
        usersFans.setId(relId);
        usersFans.setUserId(userId);
        usersFans.setFanId(fanId);

        usersFansMapper.insert(usersFans);

        usersMapper.addFansCount(userId);
        usersMapper.addFollowerCount(fanId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryIsFollow(String userId, String fanId) {
        logger.info("进入service查询用户是否已被关注入参:userId:{}，fanId:{}",
                userId, fanId);
        Example example = new Example(UsersFans.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("fanId", fanId);
        List<UsersFans> list = usersFansMapper.selectByExample(example);
        if (list != null && !list.isEmpty() && list.size() > 0) {
            return true;
        }
        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delUserFanRelation(String userId, String fanId) {
        logger.info("进入service减少粉丝数入参:userId:{}，fanId:{}",
                userId, fanId);
        Example example = new Example(UsersFans.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("fanId", fanId);
        usersFansMapper.deleteByExample(example);

        usersMapper.reduceFansCount(userId);
        usersMapper.reduceFollowerCount(fanId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        logger.info("进入service断用户名是否存在入参:username:{}", username);
        Users users = new Users();
        users.setUsername(username);

        Users result = usersMapper.selectOne(users);
        return result == null ? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUser(Users users) {
        logger.info("进入service保存用户入参:users:{}", users);
        //创建id
        String userId = sid.nextShort();
        users.setId(userId);
        usersMapper.insert(users);
    }

    @Override
    public Users loginUser(String username, String password) {
        logger.info("进入service用户登录入参:username:{},password:{}",
                username, password);
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password", password);

        Users result = usersMapper.selectOneByExample(userExample);
        logger.info("进入service用户登录出参:result:{}", result);
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void updateUserInfo(Users user) {
        logger.info("进入service用户修改信息入参:user:{}", user);
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id", user.getId());
        usersMapper.updateByExampleSelective(user, userExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        logger.info("进入service查询用户信息入参:userId:{}", userId);
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id", userId);
        Users users = usersMapper.selectOneByExample(userExample);
        logger.info("进入service查询用户信息出参:users:{}", users);
        return users;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean isUserLikeVideo(String userId, String videoId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(videoId)) {
            return false;
        }
        logger.info("进入service查询用户是否已经喜欢该该视频入参:userId:{},videoId:{}",
                userId, videoId);

        Example example = new Example(UsersLikeVideos.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("videoId", videoId);
        List<UsersLikeVideos> list = usersLikeVideosMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void reportUser(UsersReport usersReport) {
        logger.info("进入service举报用户入参:usersReport:{}", usersReport);
        String urId = sid.nextShort();
        usersReport.setId(urId);
        usersReport.setCreateDate(new Date());

        usersReportMapper.insert(usersReport);
    }
}
