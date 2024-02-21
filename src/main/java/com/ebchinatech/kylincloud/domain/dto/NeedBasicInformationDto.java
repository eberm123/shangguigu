package com.ebchinatech.kylincloud.domain.dto;

import com.ebchinatech.kylincloud.common.domain.SysDoc;
import com.ebchinatech.kylincloud.domain.NeedBasicInformation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author xuboshen
 * @date 2021-07-26
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class NeedBasicInformationDto extends NeedBasicInformation {
    
    /** 文件集合 */
    private List<SysDoc> fileObjLists;
}
