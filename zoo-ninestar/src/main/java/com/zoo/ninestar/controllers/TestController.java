package com.zoo.ninestar.controllers;

import com.zoo.ninestar.config.zoo.ZooPkPathConfig;
import com.zoo.ninestar.domains.NSPathInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
