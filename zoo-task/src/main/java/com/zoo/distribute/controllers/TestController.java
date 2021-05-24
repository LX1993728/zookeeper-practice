package com.zoo.distribute.controllers;

import com.zoo.distribute.components.NSDelayProducer;
import com.zoo.distribute.config.zoo.ZooClientConfig;
import com.zoo.distribute.config.zoo.ZooPkPathConfig;
import com.zoo.distribute.domains.NSPathInfo;
import com.zoo.distribute.enums.NSHurtType;
import com.zoo.distribute.enums.NSMasterType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequestMapping("/test")
@RestController
public class TestController {

    @GetMapping("/getInfoFromPath")
    public NSPathInfo getInfoFromPath(){
//        String path = "/nine/pks/100/initiator/1000/accumulate/hurt_a";
//        String path = "/nine/pks/100/initiator/1000/buffs/hurt_a/100232323";
        String path = "/nine/pks/100/initiator/1000/buffs/hurt_b/100232323";
        return ZooPkPathConfig.getInfoFromPath(path);
    }

    @GetMapping
    public Object testDelayQueue() throws Exception {
        String path = ZooPkPathConfig.generateOneBuffPath(NSMasterType.INITIATOR, NSHurtType.A,"200", "201", false);
        log.info("------{}-------", path);
        NSDelayProducer producer = new NSDelayProducer(path, 1);
        for (int i = 1; i<= 15; i++){
            producer.produce("" + i, i, TimeUnit.SECONDS);
            if (i == 15){
                producer.produce(ZooClientConfig.DELAY_END_FLAG, i+1, TimeUnit.SECONDS);
            }
        }
        return "success";
    }

}
