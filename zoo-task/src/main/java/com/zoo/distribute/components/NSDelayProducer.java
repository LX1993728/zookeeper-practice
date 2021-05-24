package com.zoo.distribute.components;

import com.zoo.distribute.config.beanAutowire.SpringBootBeanAutowiringSupport;
import com.zoo.distribute.config.zoo.ZooClientConfig;
import com.zoo.distribute.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.DistributedDelayQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * 血条生产者
 */
@Slf4j
public class NSDelayProducer extends SpringBootBeanAutowiringSupport {
    // 队列容器
    private static final ConcurrentMap<String, DistributedDelayQueue<String>> QUEUE_MAP = new ConcurrentHashMap<>();

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
        }finally {
            QUEUE_MAP.put(path.trim(), delayQueue);
        }
    }
    public void produce(String content,long timeMilis) throws Exception {
        delayQueue.put(content,System.currentTimeMillis() + timeMilis);
    }

    public void produce(String content,long keepalive, TimeUnit timeUnit) throws Exception {
        this.produce(content, timeUnit.toMillis(keepalive));
    }

    /**
     * this static method is used for auto close queue  when consume empty or  force close
     * @param path
     * @throws Exception
     */
    public static void closeDelayQueue(String path) throws Exception {
        final CuratorFramework curatorFramework = SpringContextUtil.getBean(CuratorFramework.class);
        curatorFramework.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
        DistributedDelayQueue<String> queue = QUEUE_MAP.get(path.trim());
        if (queue == null){
            log.warn("the delay queue for path {} cannot be found", path);
            return;
        }
        queue.close();
        QUEUE_MAP.remove(path.trim());

        log.info("------ queue for path {} destroy queue={} ----", path, queue);
    }

    public void close() throws IOException {
        delayQueue.close();
    }
}
