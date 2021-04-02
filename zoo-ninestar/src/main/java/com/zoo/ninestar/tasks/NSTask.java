package com.zoo.ninestar.tasks;

import com.zoo.ninestar.config.beanAutowire.SpringBootBeanAutowiringSupport;
import com.zoo.ninestar.services.NSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class NSTask extends SpringBootBeanAutowiringSupport implements Runnable {
    @Autowired
    private NSService nsService;

    @Override
    public void run() {

    }
}
