package liuxun.zoo.curator.ninestars.core.common;

import liuxun.zoo.curator.election.Client;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class ZkLeader {
    private static final AtomicReference<Boolean> isLeader = new AtomicReference<>(false);
    private static final LeaderSelector leaderSelector = new LeaderSelector(Client.getCf(), "/servers/leader", new LeaderSelectorListenerAdapter() {
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

    static {
        //放弃领导权之后，自动再次竞选
        leaderSelector.autoRequeue();
        leaderSelector.start();
    }

    public static Boolean isLeader(){
        return isLeader.get();
    }
}
