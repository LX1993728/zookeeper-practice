package liuxun.zoo.curator.queue.examples;


import liuxun.zoo.curator.election.Client;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type;
import org.apache.curator.framework.recipes.queue.DistributedDelayQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
public class DistributedDelayQueueExample {


    private static final String PATH = "/example/queue";
    public static void main(String[] args) throws Exception {
        String connectStr = "localhost:2181";
        CuratorFramework client = null;
        DistributedDelayQueue<String> queue = null;
        try {
            client = CuratorFrameworkFactory.newClient(connectStr, new ExponentialBackoffRetry(1000, 3));
            client.getCuratorListenable().addListener(new CuratorListener() {
                @Override
                public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
                    log.info("CuratorEvent: " + event.getType().name());
                }
            });
            client.start();



            QueueConsumer<String> consumer = createQueueConsumer(queue, client, PATH);
            QueueBuilder<String> builder = QueueBuilder.builder(client, consumer, createQueueSerializer(), PATH);
            queue = builder.buildDelayQueue();
            queue.start();

            final TreeCache treeCache = new TreeCache(Client.getCf(), PATH);
            treeCache.getListenable().addListener((curatorFramework, treeCacheEvent) -> {
                Type type = treeCacheEvent.getType();
                switch (type){
                    case NODE_REMOVED:
                    case NODE_UPDATED:
                    case NODE_ADDED:
//                        log.info("path={}\ttype={}\tdata={}", treeCacheEvent.getData().getPath(),
//                                treeCacheEvent.getType(), new String(treeCacheEvent.getData().getData(), StandardCharsets.UTF_8).trim());

                }
            });
            treeCache.start();

            for (int i = 0; i < 10; i++) {
                Thread.sleep(100);
                queue.put("test-" + i, System.currentTimeMillis() + 10000);
                if (i == 9){
                    queue.put("E", System.currentTimeMillis() + 10100);
                }
            }
            log.info(new Date().getTime() + ": already put all items");


            Thread.sleep(200000000);

        } catch (Exception ex) {
        } finally {
            CloseableUtils.closeQuietly(queue);
            CloseableUtils.closeQuietly(client);
        }

        Thread.sleep(Integer.MAX_VALUE);
    }
    private static QueueSerializer<String> createQueueSerializer() {
        return new QueueSerializer<String>() {
            @Override
            public byte[] serialize(String item) {
                return item.getBytes(StandardCharsets.UTF_8);
            }
            @Override
            public String deserialize(byte[] bytes) {
                return new String(bytes, StandardCharsets.UTF_8);
            }
        };
    }
    private static QueueConsumer<String> createQueueConsumer(DistributedDelayQueue<String> queue,
                                                             CuratorFramework client,
                                                             String path) {
        return new QueueConsumer<String>() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                log.info("connection new state: " + newState.name());
            }
            @Override
            public void consumeMessage(String message) throws Exception {
                if (message.equals("test-8")){
                    if (queue != null){
                        queue.close();
                    }
                    client.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
                    log.info("------ queue destroy  {} ----", queue);
                }
                log.info(new Date().getTime() + ": consume one message: " + message);
            }
        };
    }

}
