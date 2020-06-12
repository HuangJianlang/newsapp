package com.jianlang.common.zookeeper;

import com.google.common.collect.Maps;
import com.jianlang.common.zookeeper.sequence.ZkSequence;
import com.jianlang.common.zookeeper.sequence.ZkSequenceEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Map;

@Getter
@Setter
public class ZookeeperClient {

    Logger logger = LoggerFactory.getLogger(ZookeeperClient.class);

    private String host;
    private String sequencePath;

    private CuratorFramework client = null;

    private Map<String, ZkSequence> zkSequenceMap = Maps.newConcurrentMap();

    // 重试休眠时间
    private final int SLEEP_TIME_MS = 1000;
    // 最大重试1000次
    private final int MAX_RETRIES = 1000;
    //会话超时时间
    private final int SESSION_TIMEOUT = 30 * 1000;
    //连接超时时间
    private final int CONNECTION_TIMEOUT = 3 * 1000;

    public ZookeeperClient(String host, String sequencePath){
        this.host = host;
        this.sequencePath = sequencePath;
    }

    /*
    SpringBoot 中该注释的作用是初始化类时，也运行init这个方法
     */
    @PostConstruct
    public void init(){
        this.client = CuratorFrameworkFactory.builder()
                .connectString(this.getHost())
                .connectionTimeoutMs(CONNECTION_TIMEOUT)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(new ExponentialBackoffRetry(SLEEP_TIME_MS, MAX_RETRIES)).build();
        this.client.start();
        this.initZkSequence();
    }

    /**
     * 绑定表以及与他相关的id，使其能够按照本表当前序列自增
     */
    public void initZkSequence(){
        //拿到具体的表
        ZkSequenceEnum[] list = ZkSequenceEnum.values();
        for (int i=0; i < list.length; i++){
            String tableName = list[i].name();
            String path = this.sequencePath+tableName;
            ZkSequence zkSequence = new ZkSequence(this.client, path);
            zkSequenceMap.put(tableName, zkSequence);
        }
    }

    /**
     * 获取当前的值
     * @param tableName
     */

    public Long sequence(ZkSequenceEnum tableName){
        try {
            ZkSequence zkSequence = zkSequenceMap.get(tableName.name());
            if(zkSequence != null) {
                return zkSequence.sequence();
            }
        } catch (Exception e){
            logger.error("获取[{}]Sequence 错误: {}", tableName, e);
            e.printStackTrace();
        }
        return null;
    }
}
