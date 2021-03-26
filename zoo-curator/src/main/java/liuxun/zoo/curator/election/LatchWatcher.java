package liuxun.zoo.curator.election;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.junit.Test;

import java.io.EOFException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * LeaderLatch (阻塞选举) + Watcher(通知) 实现唯一客户端通知 进行分布式计算
 * 缺陷: 无法自动选举以及控制当leader的时间 (分布式计算的时间)
 */

@Slf4j
public class LatchWatcher {

    public void test() throws Exception {
        LeaderLatch leaderLatch = new LeaderLatch(Client.getCf(), "/servers/leader2");
        /*
        LeaderLatchListener listener = new LeaderLatchListener() {
            @Override
            public void isLeader() {
                log.info("i am master");
            }

            @Override
            public void notLeader() {
                log.info("i am salver");
            }
        };
        leaderLatch.addListener(listener);
         */
        leaderLatch.start();
        new Thread(() -> {
            //阻塞等待自己有领导权
            try {
                leaderLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (EOFException e) {
                e.printStackTrace();
            }
        });


        final TreeCache treeCache = new TreeCache(Client.getCf(), "/aaa");
        treeCache.getListenable().addListener((curatorFramework, treeCacheEvent) -> {
            boolean isLeader = leaderLatch.hasLeadership();
            log.info("{}", isLeader);
            if (isLeader){
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
    public void test1() throws Exception {
        test();
    }

    @Test
    public void test2() throws Exception {
        test();
    }
}

