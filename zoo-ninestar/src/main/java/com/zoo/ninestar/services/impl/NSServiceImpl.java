package com.zoo.ninestar.services.impl;

import com.zoo.ninestar.config.zoo.ZooClientConfig;
import com.zoo.ninestar.config.zoo.ZooPkPathConfig;
import com.zoo.ninestar.domains.NSEventData;
import com.zoo.ninestar.enums.NSEventType;
import com.zoo.ninestar.services.NSService;
import com.zoo.ninestar.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NSServiceImpl implements NSService {


    @Override
    public void postNotifyTaskEventDataToSlave(NSEventData eventData) {
        Assert.notNull(eventData, "eventData cannot be null!!!");
        final String data = eventData.getData();
        final String path = eventData.getPath();
        final NSEventType type = eventData.getType();
        final String address = eventData.getAddress();
        Assert.isTrue(StringUtils.isNotBlank(address), "target address cannot be blank !!!");
        String wrappUrl = String.format("%s?%s=%s", address, "assign", ZooClientConfig.NOTIFY_TASK_ASSIGN);
        final String result = HttpUtils.postJson(wrappUrl,eventData);
        if (result == null){
            log.error("dispatch task fail, eventData={}", eventData.toString());
        }
        log.info("dispatch task result= {}", result);
        log.info("dispatch task success, eventData={}", eventData.toString());
    }
}
