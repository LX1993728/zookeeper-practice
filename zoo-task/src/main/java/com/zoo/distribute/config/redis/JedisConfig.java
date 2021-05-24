package com.zoo.distribute.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Slf4j
@Configuration
public class JedisConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private long maxWait;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int minIdle;

    @Bean
    public JedisPool jedisProvider() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWait);
        config.setMaxTotal(maxActive);
        config.setMinIdle(minIdle);
        JedisPool jedisPool = new JedisPool(config, host, port, timeout, null);

        log.info("JedisPool生成成功！");
        log.info("Redis地址：" + host + ":" + port);

        return jedisPool;
    }
}

