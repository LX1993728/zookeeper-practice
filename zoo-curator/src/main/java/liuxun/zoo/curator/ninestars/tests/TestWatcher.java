package liuxun.zoo.curator.ninestars.tests;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestWatcher {

    @Test
    public void test1() throws Exception {
        final TreeCache treeCache = new TreeCache(Client.getCf(), "/aaa");
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                log.info("{}", treeCacheEvent.getType());
                final ChildData data = treeCacheEvent.getData();
                log.info("{}", data != null ? data.getPath() : "null");
                log.info("{}", data != null ? new String(data.getData()) : "null");
            }
        });
        treeCache.start();
        TimeUnit.HOURS.sleep(1);
    }

    @Test
    public void test2() throws Exception {
        final TreeCache treeCache = new TreeCache(Client.getCf(), "/aaa");
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                log.info("{}", treeCacheEvent.getType());
                log.info(treeCacheEvent.getData().getPath());
                log.info(new String(treeCacheEvent.getData().getData()));
            }
        });
        treeCache.start();
        TimeUnit.HOURS.sleep(1);
    }
}

@Slf4j
 class Client{
     private static final String CONNECT_ADDR = "localhost:2181";
     private static final int SESSION_OUTTIME = 5000000;//ms
     private static final RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
     private static final CuratorFramework cf = CuratorFrameworkFactory.builder()
             .connectString(CONNECT_ADDR)
             .sessionTimeoutMs(SESSION_OUTTIME)
             .retryPolicy(retryPolicy)
             .build();
      static {
          start();
      }

     public static void start(){
         synchronized(Client.class){
             log.info("client state= {}", cf.getState().toString());
             if (!cf.getState().equals(CuratorFrameworkState.STARTED)){
                 log.info("curator client is stopped, starting ......");
                 cf.start();
             }else {
                 log.info("curator client is running");
             }
         };
     }

    public static CuratorFramework getCf() {
        if (cf.getState().equals(CuratorFrameworkState.STOPPED)){
            start();
        }
        return cf;
    }
}
