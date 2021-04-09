package com.zoo.ninestar.components;

import com.zoo.ninestar.config.zoo.ZooClientConfig;
import com.zoo.ninestar.config.zoo.ZooPkPathConfig;
import com.zoo.ninestar.domains.NSEventData;
import com.zoo.ninestar.enums.NSEventType;
import com.zoo.ninestar.tasks.NSNotifyTask;
import com.zoo.ninestar.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.leader.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * 分布式任务分配器
 */
@Slf4j
@Component
public class NSNotifyTaskDispatcher {

    @Qualifier("taskExecutor")
    @Autowired
    private Executor taskExecutor;

    @Autowired
    private CuratorFramework curatorFramework;

    @Value("${server.port}")
    private Integer port;

    private CuratorCache curatorCache;
    private CuratorCacheListener listener;
    private LeaderSelector leaderSelector;

    public void openListener(){
        curatorCache = CuratorCache.builder(curatorFramework, ZooPkPathConfig.getPksPrefix(false)).build();
        listener = CuratorCacheListener
                .builder()
                .forTreeCache(curatorFramework, new TreeCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                        switch (event.getType()){
                            case NODE_ADDED:
                            case NODE_UPDATED:
                            case NODE_REMOVED:
                            log.info("path={}\ttype={}", event.getData().getPath(), event.getType());
                            waitForElectCompAndResolveEvent(event);
                        }
                    }
                })
                .afterInitialized()
                .build();
        curatorCache.listenable().addListener(listener);
        curatorCache.start();
    }

    public void leaderSelect(){
        String localIp = IPUtil.getLocalIP();
        String participantId = String.format("%s:%s", localIp, port);
        log.info("register participantId={}", participantId);
        leaderSelector = new LeaderSelector(curatorFramework, ZooPkPathConfig.getLeaderPath(), new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                log.debug("become leader！");
                TimeUnit.MINUTES.sleep(30);
                log.debug("give up the leader..");
            }
        });
        leaderSelector.setId(participantId);
        leaderSelector.autoRequeue();
        leaderSelector.start();
    }

    /**
     * accept the zk event and  dispatch notify tasks
     * @param event
     */
    private void acceptEventAndDispatchNotifyTasks(TreeCacheEvent event) throws Exception {
        String path = event.getData().getPath();
        if (!ZooPkPathConfig.isPathNeedBeResolved(path)){
            log.warn("the notify zk path: {} is not need be resolved, ignore", path);
            return;
        }
        final NSEventData eventData = new NSEventData();
        eventData.setPath(path);
        Type type = event.getType();
        if (type == Type.NODE_ADDED){
            eventData.setType(NSEventType.ADDED);
        }else if (type ==  Type.NODE_UPDATED){
            eventData.setType(NSEventType.UPDATED);
        }else if (type == Type.NODE_REMOVED){
            eventData.setType(NSEventType.REMOVED);
        }

        if (event.getData().getData() != null){
            eventData.setData(new String(event.getData().getData(), StandardCharsets.UTF_8));
        }
        String address = getRandomID();
        Assert.isTrue(StringUtils.isNotBlank(address), "random address cannot be null!!!");
        eventData.setAddress(address);

        // start dispatcher notify task
        final int notifyTaskCount = NSNotifyTask.notifyTaskCount.get();
        if (notifyTaskCount <= ZooClientConfig.NOTIFY_TASK_MAX_SIZE){
            taskExecutor.execute(new NSNotifyTask(eventData));
        }else {
            ZooClientConfig.NOTIFY_QUEUE.put(eventData);
        }
    }

    private void waitForElectCompAndResolveEvent(TreeCacheEvent event) throws Exception {
        while (StringUtils.isEmpty(leaderSelector.getLeader().getId())) {
            TimeUnit.MICROSECONDS.sleep(100);
        }
        String id = leaderSelector.getLeader().getId();
        // 如果当前进程被选举为leader，则进行任务的调度
        if (leaderSelector.hasLeadership()) {
            acceptEventAndDispatchNotifyTasks(event);
        }
    }

    private String getRandomID() throws Exception {
        List<String> pIdsList = new ArrayList<>();
        final Collection<Participant> participants = leaderSelector.getParticipants();
        final int pSize = participants.size();
        // 获取所有注册的进程ID
        for (Participant p : participants) {
            if (pSize > 1) {
                // leader 只负责任务调度
                if (!p.isLeader()) {
                    pIdsList.add(p.getId());
                }
            } else {
                pIdsList.add(p.getId());
            }
        }
        String targetPid = null;
        if (pIdsList.size() > 1) {
            // 获取进程号
            int min = 0;
            int max = pIdsList.size() - 1;
            int index = min + (int) (Math.random() * (max - min + 1));
            targetPid = pIdsList.get(index);
        } else if (pIdsList.size() == 1) {
            targetPid = pIdsList.get(0);
        }
        return targetPid;
    }
}
