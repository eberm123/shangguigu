package com.ebchinatech.kylincloud.controller;

import com.alibaba.fastjson.JSONObject;
import com.ebchinatech.kylincloud.common.domain.AjaxResult;
import com.ebchinatech.kylincloud.common.domain.SysUser;
import com.ebchinatech.kylincloud.service.feign.KylinFlowFeignService;
import com.ebchinatech.kylincloud.starter.api.KylinProcessFeignService;
import com.ebchinatech.kylincloud.starter.api.SysUserFeignService;
import com.ebchinatech.kylincloud.starter.framework.utils.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Program: KylinCloud-Demo
 * @Class: KylinFlowController
 * @Description:
 * @Author: Eastascend <EastascendWang@gmail.com>
 * @Date: 2021-12-29 14:36
 **/
@RestController
@RequestMapping("/flow")
public class KylinFlowController extends BaseController {

    @Autowired
    private SysUserFeignService sysUserFeignService;

    @Autowired
    private KylinFlowFeignService kylinFlowFeignService;
    @Autowired
    private KylinProcessFeignService kylinProcessFeignService;

    @GetMapping("/testFlow")
    public AjaxResult testFlow() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("businessKey","flow24060ded557b44cd88a2ace357eeeb2d");
        dataMap.put("bizKey","bizKey");
        return kylinFlowFeignService.startProcess(JSONObject.toJSONString(dataMap));
    }

    @GetMapping("/testUser")
    public AjaxResult testUser() {
        AjaxResult ajaxResult= sysUserFeignService.getInfo("admin");
        SysUser user = JSONObject.parseObject(JSONObject.toJSONString(ajaxResult.get("data")), SysUser.class);
        return AjaxResult.success(user);
    }

    @GetMapping("/testFlowBack")
    public AjaxResult testFlowBack() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("businessKey","flow111111111");
        dataMap.put("bizKey","bizKey");
        dataMap.put("userId","01006991");
        return kylinProcessFeignService.startProcess(JSONObject.toJSONString(dataMap));
    }

}
