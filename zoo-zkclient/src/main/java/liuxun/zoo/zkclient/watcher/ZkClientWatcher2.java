package liuxun.zoo.zkclient.watcher;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

public class ZkClientWatcher2 {

	/** zookeeper地址 */
//	static final String CONNECT_ADDR = "192.168.1.150:2181,192.168.1.151:2181,192.168.1.152:2181";
	static final String CONNECT_ADDR = "localhost:2181";
	/** session超时时间 */
	static final int SESSION_OUTTIME = 5000;//ms 
	
	
	public static void main(String[] args) throws Exception {
		ZkClient zkc = new ZkClient(new ZkConnection(CONNECT_ADDR), 5000);
		
		zkc.createPersistent("/super", "1234");

		//只监听具体某一节点的数据变化 以及当前节点是否被删除
		zkc.subscribeDataChanges("/super", new IZkDataListener() {
			@Override
			public void handleDataDeleted(String path) throws Exception {
				System.out.println("删除的节点为:" + path);
			}

			@Override
			public void handleDataChange(String path, Object data) throws Exception {
				System.out.println("变更的节点为:" + path + ", 变更内容为:" + data);
			}
		});
		zkc.createPersistent("/super/aaa", "aaaa");

		Thread.sleep(3000);
		zkc.writeData("/super", "456", -1);
		Thread.sleep(1000);
		zkc.writeData("/super/aaa", "bbbbb", -1);
		Thread.sleep(1000);

//		zkc.delete("/super"); delete不能递归删除
		zkc.deleteRecursive("/super"); // 可以递归删除
		Thread.sleep(Integer.MAX_VALUE);
		
		
	}
}
