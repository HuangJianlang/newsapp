package com.jianlang.crawler.service;

import com.jianlang.model.crawler.core.proxy.CrawlerProxy;
import com.jianlang.model.crawler.pojos.ClIpPool;

import java.util.List;

public interface CrawlerIpPoolService {
    void saveCrawlerIpPool(ClIpPool clIpPool);

    /**
     * 检查ip是否存在
     * @return
     */
    boolean checkExist(String host, int port);

    void updateCrawlerIpPool(ClIpPool clIpPool);

    /**
     * 查询所有数据
     * @param clIpPool
     * @return
     */
    List<ClIpPool> queryList(ClIpPool clIpPool);

    /**
     * 查询可用的ip 列表
     * @param clIpPool
     * @return
     */
    List<ClIpPool> queryAvailableList(ClIpPool clIpPool);

    void delete(ClIpPool clIpPool);

    /**
     * 设置Ip为不可用
     * @param proxy
     * @param errorMsg
     */
    void unAvailableProxy(CrawlerProxy proxy, String errorMsg);
}
