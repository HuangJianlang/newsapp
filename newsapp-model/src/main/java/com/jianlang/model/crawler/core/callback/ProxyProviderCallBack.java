package com.jianlang.model.crawler.core.callback;

import com.jianlang.model.crawler.core.proxy.CrawlerProxy;

import java.util.List;

/**
 * IP池更新回调
 */
public interface ProxyProviderCallBack {
    public List<CrawlerProxy> getProxyList();
    public void unavailable(CrawlerProxy crawlerProxy);
}