package com.ebchinatech.kylincloud.task;

import com.ebchinatech.kylincloud.common.utils.string.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 *
 * @author kylin
 */
@Component("kylinTask")
public class KylinTask {
    public void kylinMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void kylinParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    public void kylinNoParams() {
        System.out.println("###执行无参方法###");
    }
}
