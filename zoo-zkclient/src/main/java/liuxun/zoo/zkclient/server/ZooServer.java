package liuxun.zoo.zkclient.server;

import org.I0Itec.zkclient.IDefaultNameSpace;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkServer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.server.ZooKeeperServer;

/**
 * @apiNote 程序启动为ZooKeeper服务端
 */
public class ZooServer {
    public static void main(String[] args){
        new ZkServer("./data", "./log", new IDefaultNameSpace() {
            @Override
            public void createDefaultNameSpace(ZkClient zkClient) {
                zkClient.create("/default", "default data", CreateMode.PERSISTENT);
            }
        }, 2181, 200, 5000).start();

    }
}
