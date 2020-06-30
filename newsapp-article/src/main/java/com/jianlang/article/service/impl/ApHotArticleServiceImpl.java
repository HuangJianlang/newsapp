package com.jianlang.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.jianlang.article.service.ApHotArticleService;
import com.jianlang.common.article.constants.ArticleConstants;
import com.jianlang.model.admin.pojos.AdChannel;
import com.jianlang.model.article.pojos.ApArticle;
import com.jianlang.model.article.pojos.ApHotArticles;
import com.jianlang.model.behavior.pojos.ApBehaviorEntry;
import com.jianlang.model.mappers.admin.AdChannelMapper;
import com.jianlang.model.mappers.app.ApArticleMapper;
import com.jianlang.model.mappers.app.ApHotArticlesMapper;
import com.jianlang.model.mappers.app.AppShowBehaviorEntryMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("all")
public class ApHotArticleServiceImpl implements ApHotArticleService {

    @Autowired
    private ApArticleMapper apArticleMapper;
    @Autowired
    private AdChannelMapper adChannelMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AppShowBehaviorEntryMapper appShowBehaviorEntryMapper;
    @Autowired
    private ApHotArticlesMapper apHotArticlesMapper;

    @Override
    public void computeHotArticle() {
        //查询前一天新发布的文章列表
        String lastDay = DateTime.now().minusDays(1).toString("yyyy-MM-dd 00:00:00");
        List<ApArticle> articles = apArticleMapper.loadLastArticleForHot(lastDay);
        //计算文章热度
        List<ApHotArticles> hotArticlesList = computeHotArticle(articles);
        //缓存文章中的频道
        cacheTagToRedis(articles);
        //给用户保存一份文章
        List<ApBehaviorEntry> apBehaviorEntries = appShowBehaviorEntryMapper.selectAllEntry();
        for (ApHotArticles hotArticles : hotArticlesList) {
            apHotArticlesMapper.insert(hotArticles);
            //给每一个用户保存热点数据
            saveHotArticleForEntryList(hotArticles, apBehaviorEntries);
        }

    }

    private void saveHotArticleForEntryList(ApHotArticles hotArticles, List<ApBehaviorEntry> apBehaviorEntries) {
        for (ApBehaviorEntry apBehaviorEntry : apBehaviorEntries) {
            hotArticles.setEntryId(apBehaviorEntry.getId());
            apHotArticlesMapper.insert(hotArticles);
        }
    }

    private void cacheTagToRedis(List<ApArticle> hotArticlesList) {
        List<AdChannel> channels = adChannelMapper.selectAll();
        List<ApArticle> articles = null;
        for (AdChannel channel : channels) {
            articles = hotArticlesList.stream().filter(p -> p.getChannelId().equals(channel.getId())).collect(Collectors.toList());
            if (articles.size() > 30){
                articles = articles.subList(0, 30);
            }
            if (articles.size() == 0){
                redisTemplate.opsForValue().set(ArticleConstants.HOT_ARTICLE_FIRST_PAGE+channel.getId(), "");
            }
            redisTemplate.opsForValue().set(ArticleConstants.HOT_ARTICLE_FIRST_PAGE+channel.getId(), JSON.toJSONString(articles));
        }
    }

    private List<ApHotArticles> computeHotArticle(List<ApArticle> articles) {
        List<ApHotArticles> hotArticles = new ArrayList<>();
        ApHotArticles hotArticle = null;
        for (ApArticle article : articles){
            hotArticle = initHotBaseApArticle(article);
            Integer score = computeScore(article);
            hotArticle.setScore(score);
            hotArticles.add(hotArticle);
        }
        //排序，获取一定数量的数据
        hotArticles.sort(new Comparator<ApHotArticles>() {
            @Override
            public int compare(ApHotArticles o1, ApHotArticles o2) {
                return o2.getScore() - o1.getScore();
            }
        });
        if (hotArticles.size() > 1000){
            return hotArticles.subList(0, 1000);
        }
        return hotArticles;
    }

    private Integer computeScore(ApArticle article) {
        Integer score = 0;
        if (article.getLikes() != null){
            score += article.getLikes();
        }
        if (article.getCollection() != null){
            score += article.getCollection();
        }
        if (article.getComment() != null){
            score += article.getComment();
        }
        if (article.getViews() != null){
            score += article.getViews();
        }
        return score;
    }

    //初始化热度文章信息
    private ApHotArticles initHotBaseApArticle(ApArticle article) {
        ApHotArticles hotArticles = new ApHotArticles();
        hotArticles.setEntryId(0);
        hotArticles.setTagId(article.getChannelId());
        hotArticles.setTagName(article.getChannelName());
        hotArticles.setScore(0);
        hotArticles.setArticleId(article.getId());
        hotArticles.setProvinceId(article.getProvinceId());
        hotArticles.setCityId(article.getCityId());
        hotArticles.setCountyId(article.getCountyId());
        hotArticles.setIsRead(0);
        hotArticles.setReleaseDate(article.getPublishTime());
        hotArticles.setCreatedTime(new Date());
        return hotArticles;
    }
}
