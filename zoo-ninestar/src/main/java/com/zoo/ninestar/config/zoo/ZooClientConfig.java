package com.zoo.ninestar.config.zoo;

import com.zoo.ninestar.domains.NSEventData;
import lombok.extern.slf4j.Slf4j;
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
    private static final int NOTIFY_TASK_MAX_SIZE = 100;
    // 任务执行 -  最大任务数
    private static final int BUSINESS_TASK_MAX_SIZE = 200;


    // 任务分配队列 （存放数据）
    public static final BlockingQueue<NSEventData> NOTIFY_QUEUE = new ArrayBlockingQueue<>(NOTIFY_QUEUE_MAX_SIZE);
    // 任务执行队列 (存放数据)
    public static final BlockingQueue<NSEventData> BUSINESS_QUEUE = new ArrayBlockingQueue<>(BUSINESS_QUEUE_MAX_SIZE);


}
