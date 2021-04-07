package com.zoo.ninestar.controllers;

import com.zoo.ninestar.config.zoo.ZooClientConfig;
import com.zoo.ninestar.domains.NSEventData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * 处理任务回调相关
 */
@Slf4j
@RestController
public class NSNotifyTaskController {
    @Qualifier("taskExecutor")
    @Autowired
    private Executor taskExecutor;

    @PostMapping(ZooClientConfig.NOTIFY_TASK_ACT)
    public Object notifyTaskResolve(@RequestBody NSEventData data, @RequestParam("assign")String assign){
        Assert.notNull(data, "body data cannot be null");
        Assert.isTrue(!StringUtils.isNotBlank(assign), "assgin");
        Assert.isTrue(assign.trim().equals(ZooClientConfig.NOTIFY_TASK_ASSIGN), "verify the notify assign failure");

        // 调用线程池，执行 对应的业务逻辑

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("isSuccess", true);
        return resultMap;
    }
}
