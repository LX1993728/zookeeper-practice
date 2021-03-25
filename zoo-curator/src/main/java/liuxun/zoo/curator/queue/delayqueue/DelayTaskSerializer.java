package liuxun.zoo.curator.queue.delayqueue;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.queue.QueueSerializer;

import java.nio.charset.StandardCharsets;

@Slf4j
public class DelayTaskSerializer implements QueueSerializer<String> {
    @Override
    public byte[] serialize(String item) {
        return item.getBytes(StandardCharsets.UTF_8);
    }
    @Override
    public String deserialize(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
