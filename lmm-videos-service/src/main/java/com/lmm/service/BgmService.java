package com.lmm.service;

import com.lmm.pojo.Bgm;
import com.lmm.pojo.Users;

import java.util.List;

public interface BgmService {
    /**
     * @description:查询bgm列表
     * @return 用户存在返回true
     * */
    public List<Bgm> queryBgmList();

    /**
     * @description: 根据ID查询bgm信息
     */
    public Bgm queryBgmById(String bgmId);

}
