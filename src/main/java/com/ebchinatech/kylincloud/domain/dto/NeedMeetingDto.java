package com.ebchinatech.kylincloud.domain.dto;

import com.ebchinatech.kylincloud.domain.NeedBasicInformation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author xuboshen
 * @date 2021-09-03
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class NeedMeetingDto extends NeedBasicInformation {

    /**
     * 需求id
     */
    private Integer[] idList;

    /**
     * 会议id
     */
    private Long meetingId;
}
