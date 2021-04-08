package com.zoo.ninestar.config.zoo;

import com.zoo.ninestar.domains.NSEventData;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Configuration
public class ZooClientConfig {
    // 任务分配地址
    public static final String NOTIFY_TASK_ACT = "/notify_task";
    // 简单签名验证
    public static final String NOTIFY_TASK_ASSIGN = "notify_task_assgin";
    // 任务分配 - 对应数据缓冲队列的最大长度
    private static final int NOTIFY_QUEUE_MAX_SIZE = 3000;
    // 任务执行 -  任务执行对应数据缓冲队列对应的最大长度
    private static final int BUSINESS_QUEUE_MAX_SIZE = 5000;
    /**
     * 没达到最大任务数时，添加队列 && 创建任务 --- 达到后只 添加队列 && 不创建任务
     */
    // 任务分配 - 最大任务数
    public static final int NOTIFY_TASK_MAX_SIZE = 100;
    // 任务执行 -  最大任务数
    public static final int BUSINESS_TASK_MAX_SIZE = 200;


    // 任务分配队列 （存放数据）
    public static final BlockingQueue<NSEventData> NOTIFY_QUEUE = new ArrayBlockingQueue<>(NOTIFY_QUEUE_MAX_SIZE);
    // 任务执行队列 (存放数据)
    public static final BlockingQueue<NSEventData> BUSINESS_QUEUE = new ArrayBlockingQueue<>(BUSINESS_QUEUE_MAX_SIZE);

    // ------------------------------  the follows config is used for zk curator  client ---------------------------------
    private static final String CONNECT_ADDR = "localhost:2181";
    private static final int SESSION_OUTTIME = 500000000;//ms
    private static final int WAIT_FOR_SHUTDOWN_OUTTIME = 500000000;//ms

    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework curatorFramework() {
        return CuratorFrameworkFactory.builder()
                .connectString(CONNECT_ADDR)
                .sessionTimeoutMs(SESSION_OUTTIME)
                .waitForShutdownTimeoutMs(WAIT_FOR_SHUTDOWN_OUTTIME)
                .retryPolicy(new ExponentialBackoffRetry(10000, 10))
                .build();
    }

}
