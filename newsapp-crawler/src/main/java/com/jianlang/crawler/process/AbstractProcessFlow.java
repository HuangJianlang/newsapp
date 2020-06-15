package com.jianlang.crawler.process;


import com.alibaba.fastjson.JSON;
import com.jianlang.model.crawler.core.cookie.CrawlerCookie;
import com.jianlang.model.crawler.core.cookie.CrawlerHtml;
import com.jianlang.model.crawler.core.parse.ParseItem;
import com.jianlang.model.crawler.core.parse.impl.CrawlerParseItem;
import com.jianlang.model.crawler.core.proxy.CrawlerProxy;
import com.jianlang.model.crawler.core.proxy.CrawlerProxyProvider;
import com.jianlang.model.crawler.enums.CrawlerEnum;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Request;

/**
 * ProcessFlow 的抽象模板类，对其他子类通用方法的一些抽取
 * 已经模板抽象方法的一些定义
 */
@Log4j2
public abstract class AbstractProcessFlow implements ProcessFlow {


}
