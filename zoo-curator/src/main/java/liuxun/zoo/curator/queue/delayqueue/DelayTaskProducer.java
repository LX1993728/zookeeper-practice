package liuxun.zoo.curator.queue.delayqueue;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.queue.DistributedDelayQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.IOException;

@Slf4j
public class DelayTaskProducer {
    private static final String CONNECT_ADDRESS="localhost:2181";
    private static final int SESSION_OUTTIME = 5000;
    private static final String QUEUE_PATH = "/queue";
    private static final String NAMESPACE = "delayTask";
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
    private CuratorFramework curatorFramework;
    private DistributedDelayQueue<String> delayQueue;
    {
        curatorFramework= CuratorFrameworkFactory.builder()
                .connectString(CONNECT_ADDRESS)
                .namespace(NAMESPACE)
                .sessionTimeoutMs(SESSION_OUTTIME)
                .retryPolicy(retryPolicy)
                .build();
        curatorFramework.start();
        delayQueue= QueueBuilder.builder(curatorFramework,
                new DelayTaskConsumer(),
                new DelayTaskSerializer(),
                QUEUE_PATH)
//                .lockPath(LOCK_PATH)
                .buildDelayQueue();
        try {
            delayQueue.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void produce(String id,long timeStamp) throws Exception {
        delayQueue.put(id,timeStamp);
        log.info("put data: {}", id + "_" + timeStamp );
    }

    public void close() throws IOException {
        delayQueue.close();
    }
}
