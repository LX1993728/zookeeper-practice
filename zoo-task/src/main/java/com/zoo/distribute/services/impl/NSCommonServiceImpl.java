package com.zoo.distribute.services.impl;

import com.zoo.distribute.config.zoo.ZooClientConfig;
import com.zoo.distribute.domains.NSEventData;
import com.zoo.distribute.enums.NSEventType;
import com.zoo.distribute.services.NSCommonService;
import com.zoo.distribute.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
public class NSCommonServiceImpl implements NSCommonService {


    @Override
    public void postNotifyTaskEventDataToSlave(NSEventData eventData) {
        Assert.notNull(eventData, "eventData cannot be null!!!");
        final String data = eventData.getData();
        final String path = eventData.getPath();
        final NSEventType type = eventData.getType();
        final String address = eventData.getAddress();
        Assert.isTrue(StringUtils.isNotBlank(address), "target address cannot be blank !!!");
        String wrappUrl = String.format("%s?%s=%s", address, "assign", ZooClientConfig.NOTIFY_TASK_ASSIGN);
        log.info("notify task data= {}, url={}", eventData.toString(), wrappUrl);
        final String result = HttpUtils.postJson(wrappUrl,eventData);
        if (result == null){
            log.error("dispatch task fail, eventData={}", eventData.toString());
        }
        log.info("dispatch task result= {}", result);
        log.info("dispatch task success, eventData={}", eventData.toString());
    }
}
