package liuxun.zoo.curator.election;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;

@Slf4j
public class Client {
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

    public static void start() {
        synchronized (Client.class) {
            log.info("client state= {}", cf.getState().toString());
            if (!cf.getState().equals(CuratorFrameworkState.STARTED)) {
                log.info("curator client is stopped, starting ......");
                cf.start();
            } else {
                log.info("curator client is running");
            }
        }
        ;
    }

    public static CuratorFramework getCf() {
        if (cf.getState().equals(CuratorFrameworkState.STOPPED)) {
            start();
        }
        return cf;
    }
}
