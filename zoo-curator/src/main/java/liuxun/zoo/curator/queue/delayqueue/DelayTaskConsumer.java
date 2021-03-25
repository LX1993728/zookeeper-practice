package liuxun.zoo.curator.queue.delayqueue;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.state.ConnectionState;

import java.text.MessageFormat;

@Slf4j
public class DelayTaskConsumer implements QueueConsumer<String> {
    @Override
    public void consumeMessage(String message) throws Exception {
        log.info(MessageFormat.format("发布资讯。id - {0} , timeStamp - {1} , " +
            "threadName - {2}",message,System.currentTimeMillis(),Thread.currentThread().getName()));
    }

    @Override
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
       log.info(MessageFormat.format("State change . New State is - {0}", connectionState));
    }
}
