package com.jianlang.crawler.process.processor.impl;

import com.jianlang.crawler.helper.CrawlerHelper;
import com.jianlang.crawler.process.entity.CrawlerConfigProperty;
import com.jianlang.crawler.process.processor.AbstractCrawlerPageProcessor;
import com.jianlang.crawler.utils.HtmlParser;
import com.jianlang.model.crawler.enums.CrawlerEnum;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;

//helper 需要考虑是否有分页的问题
@Component
@Log4j2
public class CrawlerHelpPageProcessor extends AbstractCrawlerPageProcessor {

    @Autowired
    private CrawlerConfigProperty crawlerConfigProperty;

    @Autowired
    private CrawlerHelper crawlerHelper;

    /**
     * Init 处理首页信息，获取了用户页面的所有url
     * 接下来是 处理helper(用户页面) 页面数据
     * @param page
     */
    @Override
    public void handlePage(Page page) {
        //获取处理类型
        String handleType = crawlerHelper.getHandleType(page.getRequest());
        long currentTime = System.currentTimeMillis();
        //个人空间的url
        String requestUrl = page.getUrl().get();
        log.info("Parsing Helper web page starts, url:{}, handleType:{}", requestUrl, handleType);
        //配置抓取规则
        String helpCrawlerXpath = crawlerConfigProperty.getHelpCrawlerXpath();
        Integer crawlerHelpNextPagingSize = crawlerConfigProperty.getCrawlerHelpNextPagingSize();
        //获取下一步的url
        List<String> helpUrls = page.getHtml().xpath(helpCrawlerXpath).links().all();
        if (crawlerHelpNextPagingSize != null && crawlerHelpNextPagingSize > 1){
            //分页逻辑处理
            List<String> docPageUrlList = getDocPageUrlList(requestUrl, crawlerHelpNextPagingSize);
            if (docPageUrlList!=null && !docPageUrlList.isEmpty()){
                helpUrls.addAll(docPageUrlList);
            }
        }
        //到下一步， 交给下一级处理
        addSpiderRequest(helpUrls, page.getRequest(), CrawlerEnum.DocumentType.PAGE);
        log.info("Parsing Helper web page ends, url:{}, handleType:{}, time:{}", page.getUrl(), handleType, System.currentTimeMillis()-currentTime);
    }

    //用户页面的suffix
    private final String helperUrlSuffix = "?utm_source=feed";
    //用户帖子中分页页面的url
    private final String helperPageSuffix = "/article/list/";

    private List<String> getDocPageUrlList(String url, Integer pageSize){
        List<String> docPageDocUrl = null;
        //获取分页页面url : /article/list/2 /article/list/3 /article/list/4...
        List<String> pageUrls = generateHelpPagingUrl(url, pageSize);
        //获取分页页面中blog的url /article/detail/0000 /article/detail/0001
        docPageDocUrl = getPageDocUrl(pageUrls);
        return docPageDocUrl;
    }

    /**
     * 获取分页后的url，文章的url列表
     * @param pageUrls
     * @return
     */
    private List<String> getPageDocUrl(List<String> pageUrls) {
        long currentTime = System.currentTimeMillis();
        log.info("blog page getting");
        List<String> docUrlList = new ArrayList<>();
        int failCount = 0;
        if (!pageUrls.isEmpty()){
            for (String url: pageUrls){
                log.info("Helper web paging, url:", url);
                //发送请求 获取/article/list/* 中的html
                String htmlData = getOriginalRequestHtmlData(url, null);
                boolean validate = crawlerHelper.getDataValidateCallBack().validate(htmlData);
                if (validate){
                    //解析数据
                    List<String> urlList = new Html(htmlData).xpath(crawlerConfigProperty.getHelpCrawlerXpath()).links().all();
                    if (!urlList.isEmpty()){
                        docUrlList.addAll(urlList);
                    }else {
                        failCount++;
                        if (failCount > 2){
                            break;
                        }
                    }
                }
            }
        }
        log.info("Getting article detail page urls complete, time: ", System.currentTimeMillis() - currentTime);
        return docUrlList;
    }

    //本质是生成分页页面/article/list/2
    private List<String> generateHelpPagingUrl(String url, Integer pageSize) {
        String pageUrl = url + helperPageSuffix;
        List<String> pagingUrls = new ArrayList<>();
        for (int i = 2; i < pageSize ; i++){
            pagingUrls.add(pageUrl+i);
        }
        return pagingUrls;
    }

    @Override
    public boolean isNeedHandleType(String handleType) {
        return CrawlerEnum.handleType.FORWARD.name().equals(handleType);
    }

    /**
     * 处理文档的类型
     * @param documentType
     * @return
     */
    @Override
    public boolean isNeedDocumentType(String documentType) {
        return CrawlerEnum.DocumentType.HELP.name().equals(documentType);
    }


    /**
     * 越大，优先级越低
     * @return
     */
    @Override
    public int getPriority() {
        return 110;
    }
}
