package com.ebchinatech.kylincloud.service;


import com.ebchinatech.kylincloud.common.domain.AjaxResult;
import com.ebchinatech.kylincloud.domain.NeedBasicInformation;
import com.ebchinatech.kylincloud.domain.dto.NeedBasicInformationDto;
import com.ebchinatech.kylincloud.domain.dto.NeedMeetingDto;
import com.ebchinatech.kylincloud.domain.vo.NeedBasicInformationVo;
import com.ebchinatech.kylincloud.domain.vo.NeedStatusVo;

import java.util.List;

/**
 * 需求Service接口
 * 
 * @author Xbs
 * @date 2021-07-16
 */
public interface INeedBasicInformationService {

    /**
     * 查询需求
     * 
     * @param id 需求ID
     * @return 需求
     */
    public NeedBasicInformationVo selectNeedBasicInformationById(Integer id);

    /**
     * 查询需求
     *
     * @param needMeetingDto
     * @return 需求
     */
    public List<NeedBasicInformation> selectNeedBasicInformationByMeetingId(NeedMeetingDto needMeetingDto);

    /**
     * 查询需求列表
     *
     * @param needBasicInformation 需求
     * @return 需求集合
     */
    public List<NeedBasicInformation> selectNeedBasicInformationList(NeedBasicInformation needBasicInformation);

    /**
     * 查询需求列表
     *
     * @return 需求集合
     */
    public List<NeedBasicInformation> selectNeedBasicInformationIsVoidList(Integer[] ids);


    /**
     * 新增需求-保存不提交流程
     *
     * @param needBasicInformationDto
     * @return 结果
     */
    public AjaxResult saveNeedBasicInformation(NeedBasicInformationDto needBasicInformationDto);


    /**
     * 新增需求-提交流程
     *
     * @param needBasicInformationDto
     * @return 结果
     */
    public AjaxResult insertNeedBasicInformation(NeedBasicInformationDto needBasicInformationDto);

    /**
     * 修改需求
     *
     * @param needBasicInformationDto
     * @return 结果
     */
    public AjaxResult updateNeedBasicInformation(NeedBasicInformationDto needBasicInformationDto);

    /**
     * 修改需求
     *
     * @param needBasicInformation
     * @return 结果
     */
    public AjaxResult updateNeedBasicInformation(NeedBasicInformation needBasicInformation);

    /**
     * 批量删除需求
     * 
     * @param id 需要删除的需求ID
     * @return 结果
     */
    public AjaxResult deleteNeedBasicInformationByIds(Integer id);

    /**
     * 作废需求
     *
     * @param ids 需要作废的需求ID
     * @return 结果
     */
    public AjaxResult closeNeedBasicInformationByIds(Integer ids);

//    /**
//     * 修改评审状态
//     *
//     * @param needStatusVoList
//     * @return 结果
//     */
//    public AjaxResult updateNeedBasicInformationAttendanceStatusById(List<NeedStatusVo> needStatusVoList);

    /**
     * 清除会议相关信息
     *
     * @return 结果
     */
    public AjaxResult updateNeedBasicInformationMeetingById(Integer id);

    /**
     * 修改业务状态
     *
     * @param needStatusVo
     * @return 结果
     */
    public int updateNeedBasicInformationbusinessStatusById(NeedStatusVo needStatusVo);

//    public void checkIsProductNoDeClose(Long meetingId);

    /**
     * 项目立项反显需求接口
     * @param ids
     * @return
     */
    public List<NeedBasicInformation> selectNeedListByIds(Integer[] ids);
}
