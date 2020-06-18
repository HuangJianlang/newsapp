package com.jianlang.crawler.config;

import com.jianlang.crawler.helper.CookieHelper;
import com.jianlang.crawler.helper.CrawlerHelper;
import com.jianlang.crawler.process.entity.CrawlerConfigProperty;
import com.jianlang.crawler.utils.SeleniumClient;
import com.jianlang.model.crawler.core.callback.DataValidateCallBack;
import com.jianlang.model.crawler.core.parse.ParseRule;
import com.jianlang.model.crawler.core.proxy.CrawlerProxyProvider;
import com.jianlang.model.crawler.enums.CrawlerEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import us.codecraft.webmagic.Spider;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Getter
@Setter
@Configuration
@PropertySource("classpath:crawler.properties")
@ConfigurationProperties(prefix = "crawler.init.url")
//这里主要是获取要爬取网页的url
public class CrawlerConfig {

    private Spider spider;
    private String prefix;
    private String suffix;
    //获取 properties中的选项
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("crawler");
    private static final String CRUX_COOKIE_NAME = resourceBundle.getString("crux.cookie.name");
//    private boolean isUsedProxyIp = Boolean.parseBoolean(resourceBundle.getString("proxy.isUsedProxyIp"));
    @Value("${proxy.isUsedProxyIp}")
    private boolean isUsedProxyIp;
    @Value("${init.crawler.Xpath}")
    private String initCrawlerXpath;
    @Value("${helper.crawler.Xpath}")
    private String helperCrawlerXpath;
    @Value("${crawler.help.nextPagingSize}")
    private Integer crawlerPageSize;


    /**
     * 获取首页nav下的所有页面
     * https://www.csdn.net/nav/java
     * https://www.csdn.net/nav/web
     * https://www.csdn.net/nav/python...
     * @return
     */
    public List<String> getInitCrawlerUrlList(){
        List<String> initCrawlerUrlList = new ArrayList<>();
        //验证suffix的有效性
        //String suffix = "java,web,arch,db,mobile,ops,sec,cloud"
        if(StringUtils.isNotEmpty(suffix)){
            //["java", "web", "arch", "db", "mobile","ops","sec","cloud"]
            String[] urlArray = suffix.split(",");
            //urlArray如果不为空，则处理
            if(urlArray!=null && urlArray.length>0){
                for(int i = 0; i < urlArray.length; i++){
                    String initUrl = urlArray[i];
                    if(StringUtils.isNotEmpty(initUrl)){
                        if(! initUrl.toLowerCase().startsWith("http")){
                            //把https://www.csdn.net/nav/ 与 java web arch 拼接
                            initCrawlerUrlList.add(prefix+initUrl);
                        }
                    }
                }
            }
        }
        return initCrawlerUrlList;
    }

    /**
     * @Bean Indicates that a method produces a bean to be managed by the Spring container.
     * @return
     */
    @Bean
    public SeleniumClient getSeleniumClient() {
        return new SeleniumClient();
    }


    /**
     * 设置Cookie辅助类
     *
     * @return
     */
    @Bean
    public CookieHelper getCookieHelper() {
        return new CookieHelper(CRUX_COOKIE_NAME);
    }

    /**
     * 数据校验匿名内部类
     * @param cookieHelper
     * @return
     */
    private DataValidateCallBack getDataValidateCallBack(CookieHelper cookieHelper) {
        return new DataValidateCallBack() {
            @Override
            public boolean validate(String content) {
                boolean flag = true;
                if (StringUtils.isEmpty(content)) {
                    flag = false;
                } else {
                    boolean isContains_acw_sc_v2 = content.contains("acw_sc__v2");
                    boolean isContains_location_reload = content.contains("document.location.reload()");
                    if (isContains_acw_sc_v2 && isContains_location_reload) {
                        flag = false;
                    }
                }
                return flag;
            }
        };
    }

    /**
     * CrawerHelper 辅助类
     *
     * @return
     */
    @Bean
    public CrawlerHelper getCrawerHelper() {
        CookieHelper cookieHelper = getCookieHelper();
        CrawlerHelper crawerHelper = new CrawlerHelper();
        DataValidateCallBack dataValidateCallBack = getDataValidateCallBack(cookieHelper);
        crawerHelper.setDataValidateCallBack(dataValidateCallBack);
        return crawerHelper;
    }

    /**
     * 是否使用代理Ip
     */
    /**
     * CrawlerProxyProvider bean
     *
     * @return
     */
    @Bean
    public CrawlerProxyProvider getCrawlerProxyProvider() {
        CrawlerProxyProvider crawlerProxyProvider = new CrawlerProxyProvider();
        crawlerProxyProvider.setUsedProxyIp(isUsedProxyIp);
        return crawlerProxyProvider;
    }

    @Bean
    public CrawlerConfigProperty getCrawlerConfigProperty(){
        CrawlerConfigProperty crawlerConfigProperty = new CrawlerConfigProperty();
        //初始化url页面
        crawlerConfigProperty.setInitCrawlerUrlList(getInitCrawlerUrlList());
        crawlerConfigProperty.setInitCrawlerXpath(initCrawlerXpath);
        crawlerConfigProperty.setHelpCrawlerXpath(helperCrawlerXpath);
        crawlerConfigProperty.setCrawlerHelpNextPagingSize(crawlerPageSize);

        crawlerConfigProperty.setTargetParseRuleList(getTargetParseRules());
        return crawlerConfigProperty;
    }

    private List<ParseRule> getTargetParseRules(){
        List<ParseRule> parseRules = new ArrayList<ParseRule>(){{
            add(new ParseRule("title", CrawlerEnum.ParseRuleType.XPATH, "//h1[@class='title-article']/text()"));
            //作者
            add(new ParseRule("author", CrawlerEnum.ParseRuleType.XPATH, "//a[@class='follow-nickName']/text()"));
            //发布日期
            add(new ParseRule("releaseDate", CrawlerEnum.ParseRuleType.XPATH, "//span[@class='time']/text()"));
            //标签
            add(new ParseRule("labels", CrawlerEnum.ParseRuleType.XPATH, "//span[@class='tags-box']/a/text()"));
            //个人空间
            add(new ParseRule("personalSpace", CrawlerEnum.ParseRuleType.XPATH, "//a[@class='follow-nickName']/@href"));
            //阅读量
            add(new ParseRule("readCount", CrawlerEnum.ParseRuleType.XPATH, "//span[@class='read-count']/text()"));
            //点赞量
            add(new ParseRule("likes", CrawlerEnum.ParseRuleType.XPATH, "//div[@class='tool-box']/ul[@class='meau-list']/li[@class='btn-like-box']/button/p/text()"));
            //回复次数
            add(new ParseRule("commentCount", CrawlerEnum.ParseRuleType.XPATH, "//div[@class='tool-box']/ul[@class='meau-list']/li[@class='to-commentBox']/button/p/text()"));
            //html内容
            //获取文章下所有内容
            add(new ParseRule("content", CrawlerEnum.ParseRuleType.XPATH, "//div[@id='content_views']/html()"));
        }};

        return parseRules;
    }

    public Spider getSpider(){
        return spider;
    }

    public void setSpider(Spider spider){
        this.spider = spider;
    }
}
