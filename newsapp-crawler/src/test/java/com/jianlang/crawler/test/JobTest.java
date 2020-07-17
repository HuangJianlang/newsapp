package com.jianlang.crawler.test;

import com.jianlang.crawler.proxy.ProxyIpManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JobTest {
    @Autowired
    private ProxyIpManager proxyIpManager;

    @Test
    public void testUpdateProxyIpQuartz(){
        proxyIpManager.updateProxyIp();
    }
}
