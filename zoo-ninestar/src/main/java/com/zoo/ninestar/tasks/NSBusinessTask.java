package com.zoo.ninestar.tasks;

import com.zoo.ninestar.config.beanAutowire.SpringBootBeanAutowiringSupport;
import com.zoo.ninestar.config.zoo.ZooClientConfig;
import com.zoo.ninestar.domains.NSEventData;
import com.zoo.ninestar.services.NSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 处理真实的业务逻辑
 */
@Slf4j
public class NSBusinessTask extends SpringBootBeanAutowiringSupport implements Runnable {
    @Autowired
    private NSService nsService;

    private static final AtomicInteger businessTaskCount = new AtomicInteger(0);

    public NSBusinessTask(NSEventData eventData) throws InterruptedException {
        ZooClientConfig.BUSINESS_QUEUE.put(eventData);
        businessTaskCount.addAndGet(1);
    }

    @Override
    public void run() {
        try {
            // 数据队列 + 线程池执行 高速执行
            while (!ZooClientConfig.BUSINESS_QUEUE.isEmpty()){
                final NSEventData eventData = ZooClientConfig.BUSINESS_QUEUE.poll(10, TimeUnit.SECONDS);
                if (eventData == null){
                    return;
                }
                // TODO: 根据队列中取出eventdata内容 调用nsService 处理与业务相关的逻辑
                // ...
            }
            businessTaskCount.decrementAndGet();
            log.info("Business Task\t{} is completed", Thread.currentThread().getId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
