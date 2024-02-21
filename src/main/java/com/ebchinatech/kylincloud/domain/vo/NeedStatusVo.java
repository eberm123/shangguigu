package com.ebchinatech.kylincloud.domain.vo;

import lombok.Data;

@Data
public class NeedStatusVo extends BaseStatusVo {
    //业务状态
    private String businessStatus;

    //评审状态
    private String attendanceStatus;

    //维护结果表中的状态
    private String recordStatus;

    private Long meetingId;
}
