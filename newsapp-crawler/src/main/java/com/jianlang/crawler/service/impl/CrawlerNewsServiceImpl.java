package com.jianlang.crawler.service.impl;

import com.jianlang.crawler.service.CrawlerNewsService;
import com.jianlang.model.crawler.pojos.ClNews;
import com.jianlang.model.mappers.crawlers.ClNewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("all")
public class CrawlerNewsServiceImpl implements CrawlerNewsService {

    @Autowired
    private ClNewsMapper clNewsMapper;

    @Override
    public void saveNews(ClNews clNews) {
        clNewsMapper.insertSelective(clNews);
    }

    @Override
    public void updateNews(ClNews clNews) {
        clNewsMapper.updateByPrimaryKey(clNews);
    }

    @Override
    public void deleteByUrl(String url) {
        clNewsMapper.deleteByUrl(url);
    }

    @Override
    public List<ClNews> queryList(ClNews clNews) {
        return clNewsMapper.selectList(clNews);
    }
}
