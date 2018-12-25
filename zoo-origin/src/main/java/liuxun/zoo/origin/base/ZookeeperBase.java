package liuxun.zoo.origin.base;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

/**
 * Zookeeper base学习笔记
 *
 * @author liuxun
 */
public class ZookeeperBase {

    /**
     * zookeeper地址
     */
    private static final String CONNECT_ADDR = "192.168.1.150:2181,192.168.1.151:2181,192.168.1.152:2181";
    /**
     * session超时时间
     */
    private static final int SESSION_OUTTIME = 2000;//ms
    /**
     * 信号量，阻塞程序执行，用于等待zookeeper连接成功，发送成功信号
     */
    private static final CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {

        ZooKeeper zk = new ZooKeeper(CONNECT_ADDR, SESSION_OUTTIME, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                //获取事件的状态
                KeeperState keeperState = event.getState();
                //获取事件类型
                EventType eventType = event.getType();
                //如果是建立连接
                if (KeeperState.SyncConnected == keeperState) {
                    if (EventType.None == eventType) {
                        //如果建立连接成功，则发送信号量，让后续阻塞程序向下执行
                        connectedSemaphore.countDown();
                        System.out.println("zk 建立连接");
                    }
                }
            }
        });

        //进行阻塞
        connectedSemaphore.await();

        System.out.println("...");
        //创建父节点 持久节点
//		final String result = zk.create("/testRoot", "testRoot".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//		System.out.println(result);

        //创建子节点 临时节点(连接断开后节点消失)
//		final String result = zk.create("/testRoot/children", "children data".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//		System.out.println(result);
//		Thread.sleep(20000);


        //获取节点洗信息
//		byte[] data = zk.getData("/testRoot", false, null);
//		System.out.println(new String(data));
//		System.out.println(zk.getChildren("/testRoot", false));

        //修改节点的值
//		zk.setData("/testRoot", "modify data root".getBytes(), -1);
//		byte[] data = zk.getData("/testRoot", false, null);
//		System.out.println(new String(data));

        List<String> paths = zk.getChildren("/testRoot", false);
        for (String path : paths) {
            System.out.println(path);
            byte[] data = zk.getData("/testRoot/" + path, false, null);
            System.out.println(new String(data));
        }

        //判断节点是否存在
//		System.out.println(zk.exists("/testRoot/children", false));
        //删除节点
//		zk.delete("/testRoot/children", -1);
//		System.out.println(zk.exists("/testRoot/children", false));


        // 异步删除 节点
//		zk.delete("/testRoot", -1, new AsyncCallback.VoidCallback() {
//			@Override
//			public void processResult(int rc, String path, Object ctx) {
//				System.out.println(rc);
//				System.out.println(path);
//				System.out.println(ctx);
//			}
//		},"a");


        
        zk.close();


    }

}
