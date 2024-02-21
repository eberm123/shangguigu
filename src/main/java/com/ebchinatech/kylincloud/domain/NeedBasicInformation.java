package com.ebchinatech.kylincloud.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ebchinatech.kylincloud.common.annotation.Excel;
import com.ebchinatech.kylincloud.common.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 需求对象 need_basic_information
 * 
 * @author Xbs
 * @date 2021-07-16
 */
@TableName("need_basic_information")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class NeedBasicInformation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Integer id;

    /** 业务经理 */
    @Excel(name = "业务经理")
    private String businessManager;

    /** 业务经理 */
    @Excel(name = "业务经理id")
    private String businessManagerId;

    /** 需求名称 */
    @Excel(name = "需求名称")
    private String demandName;

    /** 需求类型 */
    @Excel(name = "需求类型", readConverterExp = "1=业务类,2=技术类" )
    private Integer demandType;

    /** 需求背景 */
    @Excel(name = "需求背景")
    private String demandBackground;

    /** 需求内容详细描述 */
    @Excel(name = "需求内容详细描述")
    private String demandContent;

    /** 主管业务部门 */
    @Excel(name = "主管业务部门")
    private String businessDepartment;

    /** 主管业务部门 */
    @Excel(name = "主管业务部门id")
    private String businessDepartmentId;

    /** 需求编号 */
    @Excel(name = "需求编号")
    private String demandNumber;

    /** 业务状态 */
    @Excel(name = "业务状态对应流程审批状态", readConverterExp = "当前审批节点")
    private String businessStatus;

    /** 评审状态 */
    @Excel(name = "评审状态", readConverterExp = "0=通过,1=不通过")
    private String attendanceStatus;

    /** 是否作废 */
    @Excel(name = "是否作废")

    private String isVoid;

    /** 需求小组会期数 */
    @Excel(name = "需求小组会期数")
    private String demandGroupNumber;

    /** 会议id */
    @Excel(name = "会议id")
    private Long meetingId;

    /** 会议名称 */
    @Excel(name = "会议名称")
    private String meetingName;

    /** 会议时间 */
    @Excel(name = "会议时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date demandTeamApproveDate;

    /** 发起流程日期 */
    @Excel(name = "申请流程日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime;

    /** 流程id */
    private String procInstId;

    /** 删除标志 */
    private Integer isDel;

    /** 创建标志 */
    private String isCreate;

    private String sqlString;
}
