package com.jianlang.crawler.proxy;

import com.jianlang.common.common.util.HMStringUtils;
import com.jianlang.crawler.service.CrawlerIpPoolService;
import com.jianlang.crawler.utils.ProxyIpUtils;
import com.jianlang.crawler.utils.SeleniumClient;
import com.jianlang.model.crawler.core.cookie.CrawlerHtml;
import com.jianlang.model.crawler.core.proxy.CrawlerProxyProvider;
import com.jianlang.model.crawler.core.proxy.ProxyValidate;
import com.jianlang.model.crawler.pojos.ClIpPool;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 动态代替ip管理类
 */

@Component
@Log4j2
public class ProxyIpManager {

    @Autowired
    private CrawlerIpPoolService crawlerIpPoolService;

    /**
     * 校验动态代理ip
     */
    public void validateProxyIp(){
        List<ClIpPool> clIpPools = crawlerIpPoolService.queryList(new ClIpPool());
        if (clIpPools != null && !clIpPools.isEmpty()){
            for (ClIpPool pool : clIpPools){
                Boolean enable = pool.getEnable();
                validateProxyIp(pool);
                if (! enable && !pool.getEnable()){
                    crawlerIpPoolService.delete(pool);
                }else {
                    crawlerIpPoolService.updateCrawlerIpPool(pool);
                }
            }
        }
    }

    /**
     * 校验IP是否可用
     * @param clIpPool
     */
    public void validateProxyIp(ClIpPool clIpPool){
        clIpPool.setEnable(false);
        //封装了proxy的参数
        ProxyValidate proxyValidate = new ProxyValidate(clIpPool.getIp(), clIpPool.getPort());
        try {
            ProxyIpUtils.validateProxyIp(proxyValidate);
        }catch (Exception e){
            log.error((e.getMessage()));
        }
        if(proxyValidate.getReturnCode() == 200){
            clIpPool.setEnable(true);
        }
        clIpPool.setCode(proxyValidate.getReturnCode());
        clIpPool.setDuration(proxyValidate.getDuration());
        clIpPool.setError(HMStringUtils.getFixedLengthStr(proxyValidate.getError(), 70));
    }

    /**
     * 更新动态代理ip
     */
    public void updateProxyIp(){
        //下载目标url
        List<ClIpPool> clIpPoolList = getProxyIpList();
        if (clIpPoolList != null && ! clIpPoolList.isEmpty()){
            for (ClIpPool pool : clIpPoolList){
                validateProxyIp(pool);
                if (pool.getEnable()){
                    boolean exist = crawlerIpPoolService.checkExist(pool.getIp(), pool.getPort());
                    if (!exist){
                        crawlerIpPoolService.saveCrawlerIpPool(pool);
                    }
                }
            }

        }
    }

    @Autowired
    private SeleniumClient seleniumClient;
    @Autowired
    private CrawlerProxyProvider proxyProvider;
    @Value("${proxy.get.url}")
    private String proxyIpSource;

    //正则
    Pattern proxyIpParttern = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)\\:(\\d+)");

    /**
     * 获取抓去proxy的ip
     * @return
     */
    private List<ClIpPool> getProxyIpList() {
        List<ClIpPool> clIpPoolList = new ArrayList<>();
        CrawlerHtml crawlerHtml = seleniumClient.getCrawlerHtml(proxyIpSource, proxyProvider.getRandomProxy(), "waf_cookie");
        //parse
        if (crawlerHtml != null && StringUtils.isNotEmpty(crawlerHtml.getHtml())){
            Matcher matcher = proxyIpParttern.matcher(crawlerHtml.getHtml());
            while (matcher.find()){
                String host = matcher.group(1);
                String port = matcher.group(2);
                ClIpPool clIpPool = new ClIpPool();
                clIpPool.setIp(host);
                clIpPool.setPort(Integer.parseInt(port));
                clIpPool.setCreatedTime(new Date());
                clIpPool.setSupplier("89 website");
                clIpPoolList.add(clIpPool);
            }
        }
        return clIpPoolList;
    }
}
