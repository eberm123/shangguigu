package com.ebchinatech.kylincloud.mapper;

import com.ebchinatech.kylincloud.domain.NeedBasicInformation;
import com.ebchinatech.kylincloud.domain.dto.NeedMeetingDto;
import com.ebchinatech.kylincloud.domain.vo.NeedStatusVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 需求Mapper接口
 * 
 * @author Xbs
 * @date 2021-07-16
 */
@Repository
public interface NeedBasicInformationMapper {

    /**
     * 需求最大编号
     *
     * @return 需求最大编号
     */
    public String selectNeedBasicInformationCount(String param);

    /**
     * 查询需求
     * 
     * @param id 需求ID
     * @return 需求
     */
    public NeedBasicInformation selectNeedBasicInformationById(Integer id);

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
     * 查询未作废的需求列表
     *
     * @return 未作废的需求列表
     */
    public List<NeedBasicInformation> selectNeedBasicInformationIsVoidList(Integer[] ids);


    /**
     * 新增需求
     * 
     * @param needBasicInformation 需求
     * @return 结果
     */
    public int insertNeedBasicInformation(NeedBasicInformation needBasicInformation);

    /**
     * 修改需求
     * 
     * @param needBasicInformation 需求
     * @return 结果
     */
    public int updateNeedBasicInformation(NeedBasicInformation needBasicInformation);

    /**
     * 删除需求
     * 
     * @param id 需求ID
     * @return 结果
     */
    public int deleteNeedBasicInformationById(Integer id);

    /**
     * 作废需求
     *
     * @param ids 需要作废的数据ID
     * @return 结果
     */
    public int closeNeedBasicInformationByIds(Integer ids);


    /**
     * 修改会议相关信息
     *
     * @param id
     * @return 结果
     */
    public int updateNeedBasicInformationMeetingById(Integer id);


    /**
     * 修改业务状态
     *
     * @param needStatusVo
     * @return 结果
     */
    public int updateNeedBasicInformationbusinessStatusById(NeedStatusVo needStatusVo);

    /**
     * 立项反显需求
     * @param ids
     * @return
     */
    List<NeedBasicInformation> selectNeedListByIds(Integer[] ids);
}
