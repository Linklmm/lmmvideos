package com.lmm.controller;

import com.lmm.service.BgmService;
import com.lmm.utils.IMoocJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "BGM业务的接口", tags = {"BGM业务的controller"})
@RequestMapping("/bgm")
public class BgmController {

	@Autowired
	private BgmService bgmService;

	@ApiOperation(value = "获取bgm列表",notes ="获取bgm列表的接口" )
	@PostMapping("/list")
	public IMoocJSONResult bgmList() {

		return IMoocJSONResult.ok(bgmService.queryBgmList());
	}
	
}
