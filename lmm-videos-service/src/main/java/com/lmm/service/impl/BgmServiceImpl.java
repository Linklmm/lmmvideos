/**
 * @program: lmmvideos
 * @description: UserService实现类
 * @author: minmin.liu
 * @create: 2018-09-28 15:34
 **/
package com.lmm.service.impl;

import com.lmm.mapper.BgmMapper;
import com.lmm.mapper.UsersMapper;
import com.lmm.pojo.Bgm;
import com.lmm.pojo.Users;
import com.lmm.service.BgmService;
import com.lmm.service.UserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BgmServiceImpl implements BgmService {

    @Autowired
    private BgmMapper bgmMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Bgm> queryBgmList() {
        return bgmMapper.selectAll();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Bgm queryBgmById(String bgmId) {
        return bgmMapper.selectByPrimaryKey(bgmId);
    }
}
