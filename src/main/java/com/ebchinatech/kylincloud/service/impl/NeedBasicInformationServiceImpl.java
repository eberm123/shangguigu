package com.ebchinatech.kylincloud.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ebchinatech.kylincloud.common.domain.AjaxResult;
import com.ebchinatech.kylincloud.common.domain.KylinFlowCallBack;
import com.ebchinatech.kylincloud.common.domain.KylinTaskTodoInfo;
import com.ebchinatech.kylincloud.common.domain.SysUser;
import com.ebchinatech.kylincloud.common.domain.page.TableDataInfo;
import com.ebchinatech.kylincloud.common.exception.BaseException;
import com.ebchinatech.kylincloud.common.utils.DateUtils;
import com.ebchinatech.kylincloud.common.utils.string.StringUtils;
import com.ebchinatech.kylincloud.domain.NeedBasicInformation;
import com.ebchinatech.kylincloud.domain.dto.NeedBasicInformationDto;
import com.ebchinatech.kylincloud.domain.dto.NeedMeetingDto;
import com.ebchinatech.kylincloud.domain.vo.NeedBasicInformationVo;
import com.ebchinatech.kylincloud.domain.vo.NeedStatusVo;
import com.ebchinatech.kylincloud.mapper.NeedBasicInformationMapper;
import com.ebchinatech.kylincloud.need.constant.NeedConstant;
import com.ebchinatech.kylincloud.service.INeedBasicInformationService;
import com.ebchinatech.kylincloud.service.feign.KylinFlowFeignService;
import com.ebchinatech.kylincloud.starter.api.*;
import com.ebchinatech.kylincloud.starter.framework.utils.SecurityUtils;
import com.ebchinatech.kylincloud.starter.framework.utils.TaskUtils;
import com.ebchinatech.kylincloud.starter.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 需求Service业务层处理
 *
 * @author Xbs
 * @date 2021-07-16
 */

@Service
public class NeedBasicInformationServiceImpl implements INeedBasicInformationService {
    private static final Logger log = LoggerFactory.getLogger(NeedBasicInformationServiceImpl.class);

    @Autowired
    private NeedBasicInformationMapper needBasicInformationMapper;

    @Autowired
    private KylinFlowFeignService kylinFlowFeignService;

    @Autowired
    private KylinTaskFeignService kylinTaskFeignService;

    @Autowired
    private SysUserFeignService sysUserFeignService;

    @Autowired
    private SysDocFeignService sysDocFeignService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private KylinProcessFeignService kylinProcessFeignService;

    private SysRoleFeignService sysRoleFeignService;
    /**
     * 查询需求
     *
     * @param id 需求ID
     * @return 需求
     */
    @Override
    public NeedBasicInformationVo selectNeedBasicInformationById(Integer id) {

        sysRoleFeignService.getUser("");
        NeedBasicInformation info = needBasicInformationMapper.selectNeedBasicInformationById(id);
        NeedBasicInformationVo needBasicInformationVo = new NeedBasicInformationVo();
        BeanUtils.copyProperties(info, needBasicInformationVo);
        AjaxResult ajaxResult = sysUserFeignService.getInfo(info.getBusinessManagerId());
        SysUser sysUser = JSONObject.parseObject(JSONObject.toJSONString(ajaxResult.get("data")), SysUser.class);
        //SysUser sysUser = sysUserMapper.selectUserById(info.getBusinessManagerId());
        needBasicInformationVo.setDepts(sysUser.getDept());

        return needBasicInformationVo;


    }

    /**
     * 查询需求
     *
     * @param needMeetingDto
     * @return 需求
     */
    @Override
    public List<NeedBasicInformation> selectNeedBasicInformationByMeetingId(NeedMeetingDto needMeetingDto) {
        return needBasicInformationMapper.selectNeedBasicInformationByMeetingId(needMeetingDto);
    }

    /**
     * 查询需求列表
     *
     * @param needBasicInformation 需求
     * @return 需求
     */
    @Override
    public List<NeedBasicInformation> selectNeedBasicInformationList(NeedBasicInformation needBasicInformation) {
        return needBasicInformationMapper.selectNeedBasicInformationList(needBasicInformation);
    }

    /**
     * 查询未作废的需求列表
     *
     * @return 未作废的需求列表
     */
    @Override
    public List<NeedBasicInformation> selectNeedBasicInformationIsVoidList(Integer[] ids) {
        return needBasicInformationMapper.selectNeedBasicInformationIsVoidList(ids);
    }

    /**
     * 新增需求&提交流程-
     *
     * @param needBasicInformationDto
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult insertNeedBasicInformation(NeedBasicInformationDto needBasicInformationDto) {
        AjaxResult ajaxResult = new AjaxResult();
        if (needBasicInformationDto == null) {
            return AjaxResult.error("请求参数不能为空!");
        }
        //从参数中获取流程key
        String businessKey = "flow15c79efa59cf43d5bf8295df5eca70a3";
        if (StringUtils.isEmpty(businessKey)) {
            return AjaxResult.error("请先在参数里面设置流程key!");
        }
        //提交时判断是否已经生成过编号
        if (StringUtils.isEmpty(needBasicInformationDto.getDemandNumber())) {
            //没有编号生成编号
            needBasicInformationDto = pubNeedBasicInformation(needBasicInformationDto);
        }
        if (needBasicInformationDto.getId() == null) {
            //新增页面的提交
            ajaxResult = this.saveNeedBasicInformation(needBasicInformationDto);
        } else {
            //修改页面的提交
            ajaxResult = this.updateNeedBasicInformation(needBasicInformationDto);

        }
        Map map = new HashMap();
        //保存或者修改成功
        if (ajaxResult != null && "200".equals(ajaxResult.get("code").toString())) {
            map = JSON.parseObject(JSON.toJSONString(ajaxResult.get("data")), Map.class);
            if (!StringUtils.isEmpty(needBasicInformationDto.getProcInstId())) {
                log.info("发起人修改信息，流程实例ID为:{}", needBasicInformationDto.getProcInstId());
                //SysUser leaderByDept = deptService.getLeaderByDept(map.get("businessDepartmentId") + "");
                Map variables = new HashMap<String, Object>();
                variables.put("demandType", map.get("demandType"));
                //taskService.completeTask(needBasicInformationDto.getProcInstId(), SecurityUtils.getUsername(), variables);
                String processInstanceId = needBasicInformationDto.getProcInstId();
                String userId = SecurityUtils.getUsername();
                String taskId = null;
                TableDataInfo taskFeign = kylinTaskFeignService.list(SecurityUtils.getUsername(), "0");//查待办任务
                String taskString = JSONObject.toJSONString(taskFeign);
                List<KylinTaskTodoInfo> kylinTaskTodoInfos = JSON.parseArray(JSON.parseObject(taskString).getString("rows"), KylinTaskTodoInfo.class);
                for (KylinTaskTodoInfo taskTodoInfo : kylinTaskTodoInfos) {
                    if (processInstanceId.equals(taskTodoInfo.getProcessInstanceId())) {
                        taskId = taskTodoInfo.getId();
                        if (org.apache.commons.lang.StringUtils.isEmpty(userId) || org.apache.commons.lang.StringUtils.isBlank(userId)) {
                            JSONObject jsonObject = JSONObject.parseObject(taskTodoInfo.getBizJson());
                            userId = jsonObject.getString("assignee");
                            log.info("待办人：{}", userId);
                        }
                        break;
                    }
                }
                log.info("流程实例ID为:{}, 任务ID为:{}, 任务处理人:{}", processInstanceId, taskId, userId);
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("taskId", taskId);//!!!!!!!!!!!!!!!需要查taskId
                dataMap.put("userId", userId);
                dataMap.put("variables", variables);
                kylinTaskFeignService.completeTask(JSONObject.toJSONString(dataMap));
            } else {
                //流程发起
                log.info("需求编号:{}流程发起", needBasicInformationDto.getDemandNumber());
                Map<String, Object> bizData = TaskUtils.initVariableMap();
                //【流程名称】+需求/项目/预算/工时名称
                bizData.put("abstract", "【需求申请】" + map.get("demandName"));
                bizData.put("assignee", SecurityUtils.getUsername());
                Map variables = new HashMap<String, Object>();
                variables.put("demandType", map.get("demandType"));
                variables.put(NeedConstant.APPLY_REEDIT_GROUP_KEY, SecurityUtils.getUsername());
                variables.put(NeedConstant.ATTENDANCE_STATUS, "3");
                //动态传入审批人
                //String pmo = postMapper.selectSysUserByPostCode(SysPostConstants.PMO);
                String pmo = "admin";
                if (StringUtils.isBlank(pmo)) {
                    throw new BaseException("PMO负责人为空！");
                }
                //SysUser leaderByDept = deptService.getLeaderByDept(map.get("businessDepartmentId") + "");
                SysUser leaderByDept = SecurityUtils.getLoginUser().getUser();
                if ("1".equals(map.get("demandType") + "")) {
                    //业务类流程
                    // 二级部门负责人
                    if (leaderByDept == null || org.apache.commons.lang.StringUtils.isBlank(leaderByDept.getUserId())) {
                        throw new BaseException("业务部门负责人为空！");
                    }
                    //String productAssistant = postMapper.selectSysUserByPostCode(SysPostConstants.PRODUCT_ASSISTANT);
                    String productAssistant = SecurityUtils.getLoginUser().getUser().getUserId();
                    if (StringUtils.isBlank(productAssistant)) {
                        throw new BaseException("产品规划部助理为空！");
                    }
                    variables.put(NeedConstant.BUSINESS_MANAGER_ID_GROUP_KEY, leaderByDept.getUserId());
                    variables.put(NeedConstant.DEMAND_GROUP_GROUP_KEY, pmo);
                    variables.put(NeedConstant.PRODUCT_ASSISTANT_GROUP_KEY, productAssistant);
                    variables.put(NeedConstant.MEETING_INFO_GROUP_KEY, SecurityUtils.getUsername());

                    variables.put(NeedConstant.SECEND_DEPT_MANAGER_GROUP_KEY, leaderByDept.getUserId());
                    variables.put(NeedConstant.PMO_GROUP_KEY, pmo);
                } else {
                    //技术类流程
                    // 二级部门负责人
                    if (leaderByDept == null || org.apache.commons.lang.StringUtils.isBlank(leaderByDept.getUserId())) {
                        throw new BaseException("二级部门负责人为空！");
                    }
                    variables.put(NeedConstant.SECEND_DEPT_MANAGER_GROUP_KEY, leaderByDept.getUserId());
                    variables.put(NeedConstant.PMO_GROUP_KEY, pmo);

                    //String productAssistant = postMapper.selectSysUserByPostCode(SysPostConstants.PRODUCT_ASSISTANT);
                    String productAssistant = SecurityUtils.getLoginUser().getUser().getUserId();

                    variables.put(NeedConstant.BUSINESS_MANAGER_ID_GROUP_KEY, leaderByDept.getUserId());
                    variables.put(NeedConstant.DEMAND_GROUP_GROUP_KEY, pmo);
                    variables.put(NeedConstant.PRODUCT_ASSISTANT_GROUP_KEY, productAssistant);
                    variables.put(NeedConstant.MEETING_INFO_GROUP_KEY, SecurityUtils.getUsername());
                }
                //ProcessInstance processInstance = kylinProcessService.startProcess(businessKey, map.get("id") + "", bizData, variables);
                Map<String, Object> processDataMap = new HashMap<>();
                processDataMap.put("businessKey", businessKey);
                processDataMap.put("bizKey", map.get("id"));
                processDataMap.put("bizData", bizData);
                processDataMap.put("variable", variables);
                AjaxResult startProcessResult = kylinFlowFeignService.startProcess(JSONObject.toJSONString(processDataMap));
                JSONObject startProcessResultMap = JSON.parseObject((String) startProcessResult.get("data"));
                String processInstanceId = startProcessResultMap.getString("processInstanceId");
                //log.info("processInstance----->>>>>>>:{}", processInstance);
                if (processInstanceId == null) {
                    throw new BaseException("流程发起失败！");
                }
                //发起流程之后保存processInstanceId
                NeedBasicInformationDto needBasicInformationDtos = new NeedBasicInformationDto();
                //needBasicInformationDtos.setProcInstId(processInstance.getProcessInstanceId());
                needBasicInformationDtos.setProcInstId(processInstanceId);
                needBasicInformationDtos.setId(needBasicInformationDto.getId());
                AjaxResult result = updateNeedBasicInformation(needBasicInformationDtos);
                if (result == null || "500".equals(result.get("code"))) {
                    throw new BaseException("保存processInstanceId失败！");
                }
            }
        }
        return ajaxResult;
    }

    /**
     * 新增需求-保存
     *
     * @param needBasicInformationDto
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult saveNeedBasicInformation(NeedBasicInformationDto needBasicInformationDto) {
        //needBasicInformationDto.setCreateBy(SecurityUtils.getUsername());
        //默认当前创建时间
        needBasicInformationDto.setCreateTime(DateUtils.getNowDate());
        needBasicInformationDto.setBusinessStatus("已创建");
        needBasicInformationDto.setIsVoid("否");
        needBasicInformationDto.setIsDel(0);
        log.info("needBasicInformationDto----------{}", needBasicInformationDto);
        int needId = needBasicInformationMapper.insertNeedBasicInformation(needBasicInformationDto);
        if (needId <= 0) {
            return AjaxResult.error("保存失败!");
        }
        // 更新关联文件
//        sysDocService.updateDocBizBatchById(needBasicInformationDto.getFileObjLists(), Boolean.FALSE,
//                String.valueOf(needBasicInformationDto.getId()), "NEED_TYPE");
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("sysDocList", needBasicInformationDto.getFileObjLists());
        dataMap.put("isSave", Boolean.FALSE);
        dataMap.put("bizObjectId", String.valueOf(needBasicInformationDto.getId()));
        dataMap.put("bizObjectType", "NEED_TYPE");
        sysDocFeignService.updateDocBizBatchById(JSONObject.toJSONString(dataMap));
        return AjaxResult.success("新增成功", needBasicInformationDto);
    }

    private NeedBasicInformationDto pubNeedBasicInformation(NeedBasicInformationDto needBasicInformationDto) {
        //默认编号为0
        String num = "0";
        //获取当前最大需求编号
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String format = simpleDateFormat.format(DateUtils.getNowDate());
        String maxDemandNumber = needBasicInformationMapper.selectNeedBasicInformationCount(format);
        if (!StringUtils.isEmpty(maxDemandNumber)) {
            num = maxDemandNumber.substring(7);
        }
        String number = String.format("%03d", Integer.parseInt(num) + 1);
        //需求编码生成规则XQ****-序列号
        //目前按年度重置，最大999
        String newDemandNumber = "XQ" + simpleDateFormat.format(new Date()) + '-' + number;
        needBasicInformationDto.setDemandNumber(newDemandNumber);
        needBasicInformationDto.setApplyTime(DateUtils.getNowDate());
        return needBasicInformationDto;
    }

    /**
     * 修改需求
     *
     * @param needBasicInformation 需求
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult updateNeedBasicInformation(NeedBasicInformation needBasicInformation) {
        // 修改需求基本信息
        needBasicInformation.setUpdateBy(SecurityUtils.getUsername());
        needBasicInformation.setUpdateTime(DateUtils.getNowDate());
        int result = needBasicInformationMapper.updateNeedBasicInformation(needBasicInformation);
        if (result <= 0) {
            return AjaxResult.error("修改失败");
        }
        return AjaxResult.success("修改成功");
    }

    /**
     * 修改需求
     *
     * @param needBasicInformationDto 需求
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult updateNeedBasicInformation(NeedBasicInformationDto needBasicInformationDto) {
        if (needBasicInformationDto.getId() == null) {
            //新增页面的保存
            AjaxResult ajaxResult = this.saveNeedBasicInformation(needBasicInformationDto);
            return ajaxResult;
        }
        // 修改需求基本信息
        needBasicInformationDto.setUpdateBy(SecurityUtils.getUsername());
        needBasicInformationDto.setUpdateTime(DateUtils.getNowDate());
        int result = needBasicInformationMapper.updateNeedBasicInformation(needBasicInformationDto);
        if (result <= 0) {
            return AjaxResult.error("修改失败");
        }
        // 更新关联文件
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("sysDocList", needBasicInformationDto.getFileObjLists());
        dataMap.put("isSave", Boolean.FALSE);
        dataMap.put("bizObjectId", String.valueOf(needBasicInformationDto.getId()));
        dataMap.put("bizObjectType", "NEED_TYPE");
        sysDocFeignService.updateDocBizBatchById(JSONObject.toJSONString(dataMap));
        return AjaxResult.success("修改成功", needBasicInformationDto);
    }

    /**
     * 删除需求
     *
     * @param id 需要删除的需求ID
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult deleteNeedBasicInformationByIds(Integer id) {
        //获取要删除的列表数据
        NeedBasicInformation needBasicInformation = needBasicInformationMapper.selectNeedBasicInformationById(id);
        if (needBasicInformation == null) {
            return AjaxResult.error("要删除的数据不存在！");
        }
        //可以删除
        if ("已创建".equals(needBasicInformation.getBusinessStatus()) || "发起人修改信息".equals(needBasicInformation.getBusinessStatus())) {
            String processInstanceId = needBasicInformation.getProcInstId();
            //没有流程
            if (StringUtils.isEmpty(processInstanceId)) {
                int result = needBasicInformationMapper.deleteNeedBasicInformationById(id);
                if (result <= 0) {
                    return AjaxResult.error("删除失败");
                }
            } else {
                //有流程
                String operator = SecurityUtils.getUsername();
                String endReason = "删除数据";
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("processInstanceId", processInstanceId);
                dataMap.put("operator", operator);
                dataMap.put("endReason", endReason);
                AjaxResult ajaxResult = kylinTaskFeignService.endProcessInstance(JSONObject.toJSONString(dataMap));
                if (ajaxResult == null || "500".equals(ajaxResult.get("code"))) {
                    return AjaxResult.error("没有找到流程实例id：" + processInstanceId + "的流程:");
                }
                int result = needBasicInformationMapper.deleteNeedBasicInformationById(id);
                if (result <= 0) {
                    return AjaxResult.error("删除失败");
                }
            }
            return AjaxResult.success("删除成功");
        }
        //不可以删除
        return AjaxResult.error("只可以删除业务状态为【已创建】、【发起人修改信息】的数据！");
    }

    /**
     * 作废需求
     *
     * @param id 需要作废的需求ID
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult closeNeedBasicInformationByIds(Integer id) {
        //获取要删除的列表数据
        NeedBasicInformation needBasicInformation = needBasicInformationMapper.selectNeedBasicInformationById(id);
        if (needBasicInformation == null) {
            return AjaxResult.error("要作废的数据不存在！");
        }
        //未提交
        if (StringUtils.isEmpty(needBasicInformation.getProcInstId())) {
            int result = needBasicInformationMapper.closeNeedBasicInformationByIds(id);
            if (result <= 0) {
                return AjaxResult.error("作废失败");
            }
        } else {
            //流程中或流程已走完
            String processInstanceId = needBasicInformation.getProcInstId();
            String operator = SecurityUtils.getUsername();
            String endReason = "作废数据";
            //作废   审核通过的不再调用作废接口，只是修改作废状态
            if (!"审核通过".equals(needBasicInformation.getBusinessStatus())) {
//                int excutionCount = kylinTaskService.getProcessExecutionCount(processInstanceId);
//                if (excutionCount == 0) {
//                    return AjaxResult.error("没有找到流程实例id：" + processInstanceId + "的流程:");
//                }
                //kylinTaskService.endProcessInstance(processInstanceId, operator, endReason);
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("processInstanceId", processInstanceId);
                dataMap.put("operator", operator);
                dataMap.put("endReason", endReason);
                AjaxResult ajaxResult = kylinTaskFeignService.endProcessInstance(JSONObject.toJSONString(dataMap));
                if (ajaxResult == null || "500".equals(ajaxResult.get("code"))) {
                    return AjaxResult.error("没有找到流程实例id：" + processInstanceId + "的流程:");
                }
            }
            int result = needBasicInformationMapper.closeNeedBasicInformationByIds(id);
            if (result <= 0) {
                return AjaxResult.error("作废失败");
            }
        }
        return AjaxResult.success("作废成功");
    }

    /**
     * 清除会议相关信息
     *
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult updateNeedBasicInformationMeetingById(Integer id) {
        int i = needBasicInformationMapper.updateNeedBasicInformationMeetingById(id);
        if (i < 0) {
            return AjaxResult.error("清空会议信息失败!");
        }
        return AjaxResult.success("清空会议信息成功!");
    }

    /**
     * 修改业务状态
     *
     * @param needStatusVo
     * @return 结果
     */
    @Override
    @Transactional
    public int updateNeedBasicInformationbusinessStatusById(NeedStatusVo needStatusVo) {
        return needBasicInformationMapper.updateNeedBasicInformationbusinessStatusById(needStatusVo);
    }

//    public void checkIsProductNoDeClose(Long meetingId){
//        NeedMeetingDto dto = new NeedMeetingDto();
//        dto.setMeetingId(meetingId);
//        List<NeedBasicInformation> needBasicInformations = needBasicInformationMapper.selectNeedBasicInformationByMeetingId(dto);
//        if (null != needBasicInformations && needBasicInformations.size() > 0){
//            if ("产品规划部助理审核".equals(needBasicInformations.get(0).getBusinessStatus())){
//                log.info("助理审核处关闭会议，跳转到选择需求小组会节点");
//                updateNeedBasicInformationMeetingById(needBasicInformations.get(0).getId());
//                Map variables = new HashMap<String, Object>();
//                variables.put(NeedConstant.ATTENDANCE_STATUS,"2");
//                taskService.completeTask(needBasicInformations.get(0).getProcInstId(),NeedConstant.PRODUCT_ASSISTANT_NODE_ID,NeedConstant.PRODUCT_ASSISTANT_GROUP_KEY,variables);
//            }
//        }
//    }

    @Override
    public List<NeedBasicInformation> selectNeedListByIds(Integer[] ids) {
        return needBasicInformationMapper.selectNeedListByIds(ids);
    }
}
