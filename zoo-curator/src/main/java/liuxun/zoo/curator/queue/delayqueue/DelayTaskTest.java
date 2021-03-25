package liuxun.zoo.curator.queue.delayqueue;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.utils.CloseableUtils;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DelayTaskTest {
    public static void main(String[] args) throws Exception {

    }

    @Test
    public void test1() throws Exception {
        DelayTaskProducer producer=new DelayTaskProducer();
        long now=new Date().getTime();
        System.out.println(MessageFormat.format("start time - {0}",now));
        producer.produce("2",now+ TimeUnit.SECONDS.toMillis(2));
        producer.produce("1",now+ TimeUnit.SECONDS.toMillis(1));
        producer.produce("4",now+ TimeUnit.SECONDS.toMillis(4));
        producer.produce("3",now+ TimeUnit.SECONDS.toMillis(3));
        producer.produce("5",now+ TimeUnit.SECONDS.toMillis(5));
        TimeUnit.HOURS.sleep(1);
        producer.close();
    }

    @Test
    public void test2() throws Exception {
        DelayTaskProducer producer=new DelayTaskProducer();
        TimeUnit.HOURS.sleep(1);
    }
}
