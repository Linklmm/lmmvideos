/**
 * @program: lmmvideos
 * @description: 登录注册
 * @author: minmin.liu
 * @create: 2018-09-28 15:13
 **/
package com.lmm.controller;

import com.lmm.pojo.Users;
import com.lmm.service.UserService;
import com.lmm.utils.IMoocJSONResult;
import com.lmm.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "用户注册登录的接口", tags = {"注册和登录的controller"})
public class RegistLoginController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户注册的接口",notes ="用户注册的接口" )
    @PostMapping("/regist")
    public IMoocJSONResult regist(@RequestBody Users user) throws Exception {
        //1.判断用户名和密码不为空
        if (StringUtils.isBlank(user.getUsername())||StringUtils.isBlank(user.getPassword())){
            return IMoocJSONResult.errorMsg("用户名和密码不能为空");
        }
        //2.判断用户名是否存在
        boolean usernameIsExist=userService.queryUsernameIsExist(user.getUsername());
        //3.保存用户，注册信息
        if(!usernameIsExist){
            user.setRickname(user.getUsername());
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setFansCounts(0);
            user.setReceiveLikeCounts(0);
            user.setFollowCounts(0);
            userService.saveUser(user);
        }else {
            return IMoocJSONResult.errorMsg("用户名已存在，请重新输入！");
        }
        //不能将密码存到前端中
        user.setPassword("");
        return IMoocJSONResult.ok(user);
    }
}
