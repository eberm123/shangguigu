package com.ebchinatech.kylincloud.domain.vo;

import com.alibaba.fastjson2.JSONObject;
import com.ebchinatech.kylincloud.common.domain.SysDept;
import com.ebchinatech.kylincloud.domain.NeedBasicInformation;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class NeedBasicInformationVo extends NeedBasicInformation {


    private String bizKey;

    //private String baseFormData;
    private JSONObject baseFormData;
    private List<SysDept> depts;
}
