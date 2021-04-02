package liuxun.zoo.curator.election;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.junit.Test;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Lock(分布式锁) + Watcher(通知) 实现唯一客户端通知 进行分布式计算
 *
 */

@Slf4j
public class LockWatcher {

    public void test() throws Exception {
        final InterProcessMutex lock = new InterProcessMutex(Client.getCf(), "/lock");

        final TreeCache treeCache = new TreeCache(Client.getCf(), "/aaa");
        treeCache.getListenable().addListener((curatorFramework, treeCacheEvent) -> {

                try {
                    // 尝试1秒内获取锁
                    boolean acquire = lock.acquire(1, TimeUnit.MILLISECONDS);
                    final Collection<String> participantNodes = lock.getParticipantNodes();
                    if (acquire) {
                        log.info("{}", treeCacheEvent.getType());
                        final ChildData data = treeCacheEvent.getData();
                        log.info("{}", data != null ? data.getPath() : "null");
                        log.info("{}", data != null ? new String(data.getData()) : "null");
                    }
                }finally {
                    lock.release();
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

