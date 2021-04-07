package liuxun.zoo.curator.election;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.leader.Participant;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * LeaderSelector(回调选举) + Watcher(通知) 实现唯一客户端通知 进行分布式计算
 *
 */

@Slf4j
public class SelectorWatcher {

    public void test(String participantId) throws Exception {
        LeaderSelector leaderSelector = new LeaderSelector(Client.getCf(), "/servers/leader", new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                log.debug("成为leader了");
                //log.info("isLeader = {}", isLeader);
                // 这里的阻塞时间相当于当领导 或者是执行任务的时间
                TimeUnit.MINUTES.sleep(30);  //sleep 30分钟
                //注意当takeLeadership方法返回之后，相当于放弃成为leader了
                log.debug("放弃成为leader");
            }
        });
        leaderSelector.setId(participantId);
        //放弃领导权之后，自动再次竞选
        leaderSelector.autoRequeue();
        leaderSelector.start();

        final Participant leader = leaderSelector.getLeader();
        log.info("leaderId={}, leader={}", leader.getId(), leader);

        // 阻塞任务队列
        BlockingQueue<Map<String, Object>> blockingQueue = new ArrayBlockingQueue<>(1000);


        final TreeCache treeCache = new TreeCache(Client.getCf(), "/aaa");
        treeCache.getListenable().addListener((curatorFramework, treeCacheEvent) -> {
            // 阻塞直至到leader选举成功，才开始处理任务
            while (StringUtils.isEmpty(leaderSelector.getLeader().getId())){
                TimeUnit.MICROSECONDS.sleep(100);
            }
            String id = leaderSelector.getLeader().getId();
            // log.info("{}--- {}", StringUtils.isEmpty(id),id);
            // 如果当前进程被选举为leader，则进行任务的调度
            if (leaderSelector.hasLeadership()){
                List<String> pIdsList = new ArrayList<>();
                final Collection<Participant> participants = leaderSelector.getParticipants();
                final int pSize = participants.size();
                // 获取所有注册的进程ID
                for (Participant p : participants){
                    if (pSize > 1){
                        // leader 只负责任务调度
                        if (!p.isLeader()){
                            pIdsList.add(p.getId());
                        }
                    }else {
                        pIdsList.add(p.getId());
                    }
                }

                // 分配任务调度
                String targetPid = null;
                if (pIdsList.size() > 1){
                    // 获取进程号
                    int min = 0;
                    int max = pIdsList.size() - 1;
                    int index = min + (int)(Math.random() * (max-min+1));
                    targetPid = pIdsList.get(index);
                }else if (pIdsList.size() == 1){
                    targetPid = pIdsList.get(0);
                }

                // log.info("targetPid={}", targetPid);
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("pid", targetPid);
                dataMap.put("event", treeCacheEvent);
                // 进行任务的分片
                blockingQueue.put(dataMap);

                /*
                log.info("{}", treeCacheEvent.getType());
                final ChildData data = treeCacheEvent.getData();
                log.info("{}", data != null ? data.getPath() : "null");
                log.info("{}", data != null ? new String(data.getData()) : "null");
                 */
            }
        });
        treeCache.start();

        // ==========================  另一个TreeCache负责监听节点的加入或者退出 =======================
        new Thread(()->{
            while (true){
                try {
                    final Map<String, Object> dataMap = blockingQueue.poll(1, TimeUnit.HOURS);
                    String targetId = (String) dataMap.get("pid");
                    TreeCacheEvent event = (TreeCacheEvent) dataMap.get("event");
                    // 开始进行任务的调度分配
                    log.info("targetId={}\tpath={}\tType={}", targetId,event.getData().getPath(), event.getType().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        TimeUnit.HOURS.sleep(1);
    }


    @Test
    public void test1() throws Exception {
        test("114.113.11.22:8099");
    }

    @Test
    public void test2() throws Exception {
        test("115.220.122.11:9100");
    }

    @Test
    public void test3() throws Exception {
        test("115.220.122.11:9200");
    }
}

