package com.redis.server;

import redis.embedded.RedisServer;

import java.io.IOException;

public class StandaloneRedisServer {
    public static void main(String[] args) throws IOException {
        RedisServer redisServer = new RedisServer(6379);
        redisServer.start();
        System.out.println("redis server start....");
    }
}
