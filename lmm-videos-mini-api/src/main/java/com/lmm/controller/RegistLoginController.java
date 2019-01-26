/**
 * @program: lmmvideos
 * @description: 登录注册
 * @author: minmin.liu
 * @create: 2018-09-28 15:13
 **/
package com.lmm.controller;

import com.lmm.pojo.Users;
import com.lmm.pojo.vo.UsersVo;
import com.lmm.service.UserService;
import com.lmm.utils.IMoocJSONResult;
import com.lmm.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@Api(value = "用户注册登录的接口", tags = {"注册和登录的controller"})
public class RegistLoginController extends BasicController{

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
            user.setnickname(user.getUsername());
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

//        String uniqueToken= UUID.randomUUID().toString();
//        redis.set(USER_REDIS_SESSION+":"+user.getId(),uniqueToken,1000*60*30);
//
//        UsersVo usersVo=new UsersVo();
//        BeanUtils.copyProperties(user,usersVo);
//        usersVo.setUserToken(uniqueToken);

        UsersVo usersVo=setUserRedisSessionToken(user);

        return IMoocJSONResult.ok(usersVo);

    }

    public UsersVo setUserRedisSessionToken(Users userModel){
        String uniqueToken= UUID.randomUUID().toString();
        redis.set(USER_REDIS_SESSION+":"+userModel.getId(),uniqueToken,1000*60*30);

        UsersVo usersVo=new UsersVo();
        BeanUtils.copyProperties(userModel,usersVo);
        usersVo.setUserToken(uniqueToken);
        return usersVo;
    }

    @ApiOperation(value = "用户登录的接口",notes ="用户登录的接口" )
    @PostMapping("/login")
    public IMoocJSONResult login(@RequestBody Users user) throws Exception {
        String password=MD5Utils.getMD5Str(user.getPassword());
        //1.判断用户名和密码不为空
        if (StringUtils.isBlank(user.getUsername())||StringUtils.isBlank(user.getPassword())){
            return IMoocJSONResult.errorMsg("用户名和密码不能为空");
        }
        //2.判断用户名是否存在
        boolean usernameIsExist=userService.queryUsernameIsExist(user.getUsername());

        if(!usernameIsExist){
            return IMoocJSONResult.errorMsg("用户不存在！");
        }else {
            //3.判断用户名和密码是否正确
            Users result = userService.loginUser(user.getUsername(),password);
            if (result!=null) {
                result.setPassword("");
                UsersVo usersVo=setUserRedisSessionToken(result);
                return IMoocJSONResult.ok(usersVo);
            } else {
                return IMoocJSONResult.errorMsg("用户名或密码错误！请重试.....");
            }
        }
    }

    @ApiOperation(value = "用户注销",notes ="用户注销的接口" )
    @ApiImplicitParam(name = "userId",value = "用户id",required = true,
            dataType = "String",paramType = "query")
    @PostMapping("/logout")
    public IMoocJSONResult logout(String userId) throws Exception {
        redis.del(USER_REDIS_SESSION+":"+userId);
        return IMoocJSONResult.ok();
    }
}
