package liuxun.zoo.curator.election;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * LeaderSelector(回调选举) + Watcher(通知) 实现唯一客户端通知 进行分布式计算
 *
 */

@Slf4j
public class SelectorWatcher {

    public void test() throws Exception {
        AtomicReference<Boolean> isLeader = new AtomicReference<>(false);
        LeaderSelector leaderSelector = new LeaderSelector(Client.getCf(), "/servers/leader", new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                log.debug("成为leader了");
                isLeader.compareAndSet(false, true);
                //log.info("isLeader = {}", isLeader);
                // 这里的阻塞时间相当于当领导 或者是执行任务的时间
                TimeUnit.SECONDS.sleep(10);  //sleep 10秒
                isLeader.compareAndSet(true, false);
                //注意当takeLeadership方法返回之后，相当于放弃成为leader了
                log.debug("放弃成为leader");
            }
        });
        //放弃领导权之后，自动再次竞选
        leaderSelector.autoRequeue();
        leaderSelector.start();

        final TreeCache treeCache = new TreeCache(Client.getCf(), "/aaa");
        treeCache.getListenable().addListener((curatorFramework, treeCacheEvent) -> {
            if (isLeader.get()){
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

