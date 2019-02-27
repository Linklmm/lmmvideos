/**
 * @program: lmmvideos
 * @description: 登录注册
 * @author: minmin.liu
 * @create: 2018-09-28 15:13
 **/
package com.lmm.controller;

import com.lmm.pojo.Users;
import com.lmm.pojo.UsersReport;
import com.lmm.pojo.vo.PublisherVideo;
import com.lmm.pojo.vo.UsersVo;
import com.lmm.service.UserService;
import com.lmm.utils.IMoocJSONResult;
import com.lmm.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


@RestController
@Api(value = "用户相关业务的接口", tags = {"用户相关业务的controller"})
@RequestMapping("/user")
public class UserController extends BasicController{

    @Autowired
    private UserService userService;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    /**
     * 用户上传头像
     * */
    @ApiOperation(value = "用户上传头像",notes ="用户上传头像的接口" )
    @ApiImplicitParam(name = "userId",value = "用户id",required = true,
            dataType = "String",paramType = "query")
    @PostMapping("/uploadFace")
    public IMoocJSONResult uploadFace(String userId, @RequestParam("file") MultipartFile[] files) throws Exception {
        if (StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("用户id不能为空...");
        }
        logger.info("controller用户上传头像入参：userId:{}",userId);
        //文件保存的命名空间
//        String fileSpace="F:/lmm_videos";
        //保存到数据库中的相对路径
        String uploadPathDB="/"+userId+"/face";

        FileOutputStream fileOutputStream=null;
        InputStream inputStream=null;

        try {
            if (files!=null&&files.length>0){
                String fileName=files[0].getOriginalFilename();

                if (StringUtils.isNoneBlank(fileName)){
                    //文件上传的最终保存路径
                    String finalFacePath=FILE_SPACE+uploadPathDB+"/"+fileName;
                    logger.info("controller用户上传头像的最终路径：finalFacePath:{}",finalFacePath);
                    //设置数据库保存的路径
                    uploadPathDB+=("/"+fileName);
                    logger.info("controller用户上传头像的数据库最终路径：uploadPathDB:{}",uploadPathDB);
                    File outFile=new File(finalFacePath);
                    if (outFile.getParentFile()!=null||!outFile.getParentFile().isDirectory()){
                        //创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream=new FileOutputStream(outFile);
                    inputStream=files[0].getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
                }

            }else {
                return IMoocJSONResult.errorMsg("上传出错...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("上传出错...");
        }finally {
            if (fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        Users user=new Users();
        user.setId(userId);
        user.setFaceImage(uploadPathDB);
        userService.updateUserInfo(user);
        logger.info("controller用户上传头像的出参：uploadPathDB:{}",uploadPathDB);
        return IMoocJSONResult.ok(uploadPathDB);
    }

    @ApiOperation(value = "查询用户信息",notes ="查询用户信息的接口" )
    @ApiImplicitParam(name = "userId",value = "用户id",required = true,
            dataType = "String",paramType = "query")
    @PostMapping("/query")
    public IMoocJSONResult query(String userId,String fanId)throws Exception{
        if (StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("用户id不能为空...");
        }
        logger.info("controller查询用户信息入参：userId:{},fanId:{}",userId,fanId);
        Users userInfo=userService.queryUserInfo(userId);
        UsersVo usersVo=new UsersVo();
        BeanUtils.copyProperties(userInfo,usersVo);

        usersVo.setFollow(userService.queryIsFollow(userId,fanId));
        logger.info("controller查询用户信息出参：usersVo:{}",usersVo);
        return IMoocJSONResult.ok(usersVo);
    }

    /**
     * @description: 查询视频发布者的信息
     * @param loginUserId
     * @param videoId
     * @param publisherUserId
     * @return
     * @throws Exception
     */
    @PostMapping("/queryPublisher")
    public IMoocJSONResult queryPublisher(String loginUserId,String videoId,String publisherUserId)throws Exception{
        if (StringUtils.isBlank(publisherUserId)){
            return IMoocJSONResult.errorMsg("视频提供者id不能为空！");
        }
        logger.info("controller查询视频发布者的信息入参：loginUserId:{},videoId:{},publisherUserId:{}"
                ,loginUserId,videoId,publisherUserId);
        //1.查询视频发布者的信息
        Users userInfo=userService.queryUserInfo(publisherUserId);
        UsersVo publisher=new UsersVo();
        BeanUtils.copyProperties(userInfo,publisher);
        //2.查询当前登录者和视频的关系
        boolean userLikeVideo = userService.isUserLikeVideo(loginUserId,videoId);

        PublisherVideo publisherVideo = new PublisherVideo();
        publisherVideo.setPublisher(publisher);
        publisherVideo.setUserLikeVideo(userLikeVideo);
        logger.info("controller查询视频发布者的信息出参：publisherVideo:{}",publisherVideo);
        return IMoocJSONResult.ok(publisherVideo);
    }

    /**
     * 用户关注
     * @param userId
     * @param fanId
     * @return
     * @throws Exception
     */
    @PostMapping("/beYourFans")
    public IMoocJSONResult beYourFans(String userId,String fanId)throws Exception{
        logger.info("controller用户关注入参：userId:{},fanId:{}"
                ,userId,fanId);
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)){
            return IMoocJSONResult.errorMsg("用户id不能为空...");
        }
        userService.saveUserFanRelation(userId,fanId);
        return IMoocJSONResult.ok("关注成功！");
    }

    /**
     * 用户取消关注
     * @param userId
     * @param fanId
     * @return
     * @throws Exception
     */
    @PostMapping("/notBeYourFans")
    public IMoocJSONResult notBeYourFans(String userId,String fanId)throws Exception{
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)){
            return IMoocJSONResult.errorMsg("用户id不能为空...");
        }
        logger.info("controller用户取消关注入参：userId:{},fanId:{}"
                ,userId,fanId);
        userService.delUserFanRelation(userId,fanId);
        return IMoocJSONResult.ok("取消关注成功！");
    }
    //举报用户
    @PostMapping("/reportUser")
    public IMoocJSONResult reportUser(@RequestBody UsersReport usersReport) throws Exception {
        logger.info("controller用户举报入参：usersReport:{}",usersReport);
        // 保存举报信息
        userService.reportUser(usersReport);

        return IMoocJSONResult.errorMsg("举报成功...有你平台变得更美好...");
    }
}
