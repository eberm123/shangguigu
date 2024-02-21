package com.ebchinatech.kylincloud.service.feign;

import com.ebchinatech.kylincloud.common.domain.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created with IntelliJ IDEA.
 * Package: com.ebchinatech.kylincloud.starter.api
 * User: Tuzki
 * Date: 2021/11/26
 * Time: 16:02
 * Description:
 */
@FeignClient(name = "kylincloud-flow", path = "/kylin/flowInfo")
public interface KylinFlowFeignService {

    /**
     * 发起流程
     *
     * @param msg
     * @return
     */
    @PostMapping(value = "/startProcess")
    AjaxResult startProcess(@RequestBody String msg);
}
