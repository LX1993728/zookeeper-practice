package liuxun.zoo.curator.server;


import org.apache.curator.test.TestingServer;

import java.io.File;

/**
 * @apiNote 单机版ZooKeeper测试服务器
 * @author liuxun
 */
public class CuratorZooServer {
    public static void main(String[] args) throws Exception{
        new TestingServer(2181,new File("./data")).start();
    }
}
