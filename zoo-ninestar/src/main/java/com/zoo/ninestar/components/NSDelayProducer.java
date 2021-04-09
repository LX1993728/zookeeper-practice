package com.zoo.ninestar.components;

import com.zoo.ninestar.config.beanAutowire.SpringBootBeanAutowiringSupport;
import com.zoo.ninestar.config.zoo.ZooClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.DistributedDelayQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 血条生产者
 */
@Slf4j
public class NSDelayProducer extends SpringBootBeanAutowiringSupport {
    @Autowired
    private CuratorFramework curatorFramework;

    @Autowired
    private QueueSerializer<String> queueSerializer;

    private String path;
    // 消费的消息类型 1: 处理buff相关 2: 处理PK超时相关
    private int type = 1;

    public NSDelayProducer(String path, int type) {
        this.path = path;
        this.type = type;
        initAndStart();
    }

    private DistributedDelayQueue<String> delayQueue;

    private void initAndStart(){
        delayQueue= QueueBuilder.builder(curatorFramework,
                new NSDelayConsumer(delayQueue, ZooClientConfig.DELAY_END_FLAG, path, type),
                queueSerializer,
                path)
//                .lockPath(LOCK_PATH)
                .buildDelayQueue();
        try {
            delayQueue.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void produce(String content,long timeMilis) throws Exception {
        delayQueue.put(content,System.currentTimeMillis() + timeMilis);
    }

    public void produce(String content,long keepalive, TimeUnit timeUnit) throws Exception {
        this.produce(content, timeUnit.toMillis(keepalive));
    }

    public void close() throws IOException {
        delayQueue.close();
    }
}
