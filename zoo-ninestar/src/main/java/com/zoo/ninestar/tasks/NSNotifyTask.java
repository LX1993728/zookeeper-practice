package com.zoo.ninestar.tasks;

import com.zoo.ninestar.config.beanAutowire.SpringBootBeanAutowiringSupport;
import com.zoo.ninestar.config.zoo.ZooClientConfig;
import com.zoo.ninestar.domains.NSEventData;
import com.zoo.ninestar.services.NSCommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 处理通知任务派发的逻辑
 */
@Slf4j
public class NSNotifyTask extends SpringBootBeanAutowiringSupport implements Runnable {
    @Autowired
    private NSCommonService nsCommonService;

    public static final AtomicInteger notifyTaskCount = new AtomicInteger(0);

    public NSNotifyTask(NSEventData eventData) throws InterruptedException {
        ZooClientConfig.NOTIFY_QUEUE.put(eventData);
        notifyTaskCount.incrementAndGet();
    }

    private String getUrl(NSEventData data){
        return String.format("http://%s/%s/",data.getAddress(), ZooClientConfig.NOTIFY_TASK_ACT);
    }

    @Override
    public void run() {
        try {
            while (!ZooClientConfig.NOTIFY_QUEUE.isEmpty()){
                final NSEventData eventData = ZooClientConfig.NOTIFY_QUEUE.poll(10, TimeUnit.SECONDS);
                if (eventData == null){
                    return;
                }
                String url = getUrl(eventData);
                eventData.setAddress(url);
                // 调用Http--post方法 分配任务到指定进程
                nsCommonService.postNotifyTaskEventDataToSlave(eventData);
            }
            log.info("Notify Task\t{} is completed", Thread.currentThread().getId());
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            notifyTaskCount.decrementAndGet();
        }
    }


}
