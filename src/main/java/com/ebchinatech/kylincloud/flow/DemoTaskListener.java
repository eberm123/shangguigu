package com.ebchinatech.kylincloud.flow;

import com.ebchinatech.kylincloud.common.domain.AjaxResult;
import com.ebchinatech.kylincloud.common.domain.KylinTaskEntity;
import com.ebchinatech.kylincloud.starter.service.IFlowListenerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 流程监听回调Demo
 *
 * @author Eastascend
 * @date 2022-02-11
 **/
@Slf4j
@Component
public class DemoTaskListener implements IFlowListenerService {
    @Override
    public KylinTaskEntity handle(KylinTaskEntity entity) {
        log.info(entity.toString());
        /*业务逻辑*/

        return entity;
    }
}
