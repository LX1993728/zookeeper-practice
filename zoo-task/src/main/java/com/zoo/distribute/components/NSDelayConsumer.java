package com.zoo.distribute.components;

import com.zoo.distribute.config.beanAutowire.SpringBootBeanAutowiringSupport;
import com.zoo.distribute.services.NSCommonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.DistributedDelayQueue;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.state.ConnectionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * buff consumer
 */
@Slf4j
public class NSDelayConsumer extends SpringBootBeanAutowiringSupport implements QueueConsumer<String> {
    @Autowired
    private CuratorFramework curatorFramework;

    @Autowired
    private NSCommonService nsCommonService;

    private DistributedDelayQueue<String> delayQueue;
    private String endFlag;
    private String path;
    private int type;

    public NSDelayConsumer(DistributedDelayQueue<String> delayQueue,
                           String endFlag,
                           String path,
                           int type
    ) {
        this.delayQueue = delayQueue;
        this.endFlag = endFlag;
        this.path = path;
        this.type = type;
        Assert.isTrue(StringUtils.isNotBlank(path), "path cannot be black !!!");
    }

    @Override
    public void consumeMessage(String message) throws Exception {
        if (message.equals(endFlag)){
            NSDelayProducer.closeDelayQueue(path);
        }else {
            resovleMessage(message);
        }
    }

    @Override
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {

    }

    private void  resovleMessage(String message) throws Exception{
        // TODO: 调用NSService 处理相关消费逻辑
       if (this.type == 1){
           log.info("consume buff message...");
           // ......
       }else if (this.type == 2){
           log.info("consume pk timeout message...");
           // ......
       }
    }
}
