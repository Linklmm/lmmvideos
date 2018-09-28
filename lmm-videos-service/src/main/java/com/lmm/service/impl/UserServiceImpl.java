/**
 * @program: lmmvideos
 * @description: UserService实现类
 * @author: minmin.liu
 * @create: 2018-09-28 15:34
 **/
package com.lmm.service.impl;

import com.lmm.mapper.UsersMapper;
import com.lmm.pojo.Users;
import com.lmm.service.UserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users users=new Users();
        users.setUsername(username);

        Users result=usersMapper.selectOne(users);
        return result==null ?false:true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUser(Users users) {
        String userId=sid.nextShort();
        users.setId(userId);
        usersMapper.insert(users);
    }
}
