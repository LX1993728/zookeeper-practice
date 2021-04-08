package com.zoo.ninestar.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartRunner implements ApplicationRunner {
    @Autowired
    private NSNotifyTaskDispatcher taskDispatcher;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("---------------程序执行完毕，开始执行初始化操作-------------------");
        taskDispatcher.openListener();
        taskDispatcher.leaderSelect();
        log.info("---------------执行初始化操作完毕...   -------------------");
    }
}
