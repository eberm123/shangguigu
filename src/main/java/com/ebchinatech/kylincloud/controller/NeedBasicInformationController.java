package com.ebchinatech.kylincloud.controller;

import com.alibaba.fastjson.JSON;
import com.ebchinatech.kylincloud.common.annotation.ApiPermi;
import com.ebchinatech.kylincloud.common.annotation.DataScope;
import com.ebchinatech.kylincloud.common.annotation.Log;
import com.ebchinatech.kylincloud.common.domain.AjaxResult;
import com.ebchinatech.kylincloud.common.domain.LoginUser;
import com.ebchinatech.kylincloud.common.domain.SysDept;
import com.ebchinatech.kylincloud.common.domain.SysUser;
import com.ebchinatech.kylincloud.common.domain.page.TableDataInfo;
import com.ebchinatech.kylincloud.common.enums.BusinessType;
import com.ebchinatech.kylincloud.common.utils.ServletUtils;
import com.ebchinatech.kylincloud.domain.NeedBasicInformation;
import com.ebchinatech.kylincloud.domain.dto.NeedBasicInformationDto;
import com.ebchinatech.kylincloud.domain.dto.NeedMeetingDto;
import com.ebchinatech.kylincloud.domain.vo.NeedBasicInformationVo;
import com.ebchinatech.kylincloud.service.INeedBasicInformationService;
import com.ebchinatech.kylincloud.starter.api.SysRoleFeignService;
import com.ebchinatech.kylincloud.starter.api.SysUserFeignService;
import com.ebchinatech.kylincloud.starter.framework.annotation.ReSubmitForbid;
import com.ebchinatech.kylincloud.starter.framework.utils.BaseController;
import com.ebchinatech.kylincloud.starter.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 需求Controller
 */
@Slf4j
@RestController
@RequestMapping("/need/basicInformation")
public class NeedBasicInformationController extends BaseController {

    @Autowired
    private INeedBasicInformationService needBasicInformationService;

    @Autowired
    private TokenService tokenService;

    private SysRoleFeignService sysRoleFeignService;

    /**
     * 查询需求列表
     */
    @GetMapping("/list")
    @ApiPermi(authorize = "need:basicInformation:list")
    @DataScope(menuName = "需求申请")
    public TableDataInfo list(NeedBasicInformation needBasicInformation) {
        startPage();
        List<NeedBasicInformation> list = needBasicInformationService.selectNeedBasicInformationList(needBasicInformation);
        return getDataTable(list);
    }

    @GetMapping("/getLoginUser")
    public AjaxResult getLoginUser() {
        LoginUser loginUser = this.tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        List<SysDept> deptList = user.getDept();
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("depts", deptList);
        return ajax;
    }

    /**
     * 获取需求详细信息
     */
    @ApiPermi(authorize = "need:basicInformation:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id) {
        return AjaxResult.success(needBasicInformationService.selectNeedBasicInformationById(id));
    }

    /**
     * 获取需求详细信息
     */
    @ApiPermi(authorize = "need:basicInformation:query")
    @PostMapping(value = "getInfoByMeetingId")
    public AjaxResult getInfoByMeetingId(NeedMeetingDto needMeetingDto) {
        return AjaxResult.success(needBasicInformationService.selectNeedBasicInformationByMeetingId(needMeetingDto));
    }

    /**
     * 新增需求-保存并提交流程-新版
     */
    @ApiPermi(authorize = "need:basicInformation:add")
    @Log(title = "需求", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NeedBasicInformationDto needBasicInformationDto) {
        log.info("needBasicInformationDto：{}", needBasicInformationDto);
        return needBasicInformationService.insertNeedBasicInformation(needBasicInformationDto);
    }

    /**
     * 修改需求
     */
    @ReSubmitForbid
    @ApiPermi(authorize = "need:basicInformation:edit")
    @Log(title = "修改需求", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NeedBasicInformationDto needBasicInformationDto) {
        log.info("needBasicInformationDto：{}", needBasicInformationDto);
        return needBasicInformationService.updateNeedBasicInformation(needBasicInformationDto);
    }

    /**
     * 删除需求
     */
    @ApiPermi(authorize = "need:basicInformation:remove")
    @Log(title = "需求", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable("id") Integer id) {
        log.info("ids:", id);
        return needBasicInformationService.deleteNeedBasicInformationByIds(id);
    }

    /**
     * 查询未作废的需求列表
     */
    @Log(title = "需求")
    @GetMapping("/getNotVoidList")
    public AjaxResult selectNeedBasicInformationIsVoidList(Integer[] ids) {
        return AjaxResult.success(needBasicInformationService.selectNeedBasicInformationIsVoidList(ids));
    }

    /**
     * 作废需求
     */
    @ApiPermi(authorize = "need:basicInformation:close")
    @Log(title = "需求", businessType = BusinessType.DELETE)
    @GetMapping("/close/{ids}")
    public AjaxResult close(@PathVariable Integer ids) {
        return needBasicInformationService.closeNeedBasicInformationByIds(ids);
    }

    /**
     * 获取需求详细信息
     */
    @ApiPermi(authorize = "need:basicInformation:query")
    @GetMapping(value = "/getNeedInfo/{needId}")
    public AjaxResult getNeedInfo(@PathVariable("needId") Integer needId) {
        NeedBasicInformation needBasicInformation = needBasicInformationService.selectNeedBasicInformationById(needId);
        return AjaxResult.success(needBasicInformation);
    }

    /**
     * 修改需求详细信息
     */
    @ApiPermi(authorize = "need:basicInformation:query")
    @PostMapping(value = "/updateNeedInfo")
    public AjaxResult updateNeedInfo(@RequestBody NeedBasicInformationVo needBasicInformationVo) {
        //log.info("通过流程修改需求详细信息{}", needBasicInformationVo.toString());
        //NeedBasicInformationDto needBasicInformationDto = JSON.parseObject(needBasicInformationVo.getBaseFormData(), NeedBasicInformationDto.class);
        NeedBasicInformationDto needBasicInformationDto = JSON.parseObject(needBasicInformationVo.getBaseFormData().toString(), NeedBasicInformationDto.class);
        return needBasicInformationService.updateNeedBasicInformation(needBasicInformationDto);
    }

}
