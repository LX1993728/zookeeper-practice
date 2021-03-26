package liuxun.zoo.curator.ninestars.core.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

@Slf4j
public abstract class ZkWatcher {
    private final TreeCache treeCache;
    private final String path;
    private final CuratorFramework cf;
    private Boolean notifyAll = false;

    public ZkWatcher(String path) {
        this(path, null, false);
    }

    public ZkWatcher(String path, boolean notifyAll) {
        this(path, null, notifyAll);
    }

    public ZkWatcher(String path, CuratorFramework cf, boolean notifyAll) {
        assert path != null;
        this.path = path;
        this.notifyAll = notifyAll;
        this.cf = cf == null ? ZkCli.getCf() : cf;
        this.treeCache = new TreeCache(this.cf, this.path);
    }

    public void start() throws Exception {
        this.treeCache.getListenable().addListener((curatorFramework, treeCacheEvent) -> {
            if (this.notifyAll){
                resolveWatcherEvent(curatorFramework, treeCacheEvent);
                return;
            }

            if (ZkLeader.isLeader()){
                resolveWatcherEvent(curatorFramework, treeCacheEvent);
            }
        });
        this.treeCache.start();
    }

    public void stop(){
        this.treeCache.close();
    }

    protected abstract void resolveWatcherEvent(CuratorFramework cf, TreeCacheEvent treeCacheEvent);
}
