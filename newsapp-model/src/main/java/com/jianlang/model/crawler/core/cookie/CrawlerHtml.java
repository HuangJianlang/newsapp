package com.jianlang.model.crawler.core.cookie;


import com.jianlang.model.crawler.core.proxy.CrawlerProxy;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
/**
 * 普通的实体类
 * 用于记录url, html, proxy
 */
public class CrawlerHtml {

    public CrawlerHtml() {
    }

    public CrawlerHtml(String url) {
        this.url = url;
    }


    private String url;

    private String html;

    private CrawlerProxy proxy;

    private List<CrawlerCookie> crawlerCookieList = null;

}
