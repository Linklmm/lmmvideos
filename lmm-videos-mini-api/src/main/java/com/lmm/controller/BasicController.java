package com.lmm.controller;

import com.lmm.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {
	
	@Autowired
	public RedisOperator redis;
	// 文件保存的命名空间
	public static final String FILE_SPACE = "C:/lmm_videos";

	//ffmpeg所在目录
	public static final String FFMPEG_EXE="C:\\ffmpeg-20181003-ebc3d04-win64-static\\bin\\\\ffmpeg.exe";

	public static final String USER_REDIS_SESSION="user-redis-session";

	//每页分页的记录数
	public static final Integer PAGE_SIZE=5;
}
