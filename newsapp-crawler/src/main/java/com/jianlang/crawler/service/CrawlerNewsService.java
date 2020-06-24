package com.jianlang.crawler.service;

import com.jianlang.model.crawler.pojos.ClNews;

import java.util.List;

public interface CrawlerNewsService {
    void saveNews(ClNews clNews);
    void updateNews(ClNews clNews);
    void deleteByUrl(String url);
    List<ClNews> queryList(ClNews clNews);

    //save to Aricle database
    void saveNewsAsArticle();
    void saveNewsAsArticle(ClNews clNews);
    void saveNewsAsArticle(Integer id);
}
