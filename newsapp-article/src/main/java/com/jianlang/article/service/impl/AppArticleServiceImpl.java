package com.jianlang.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.jianlang.article.service.AppArticleService;
import com.jianlang.common.article.constants.ArticleConstants;
import com.jianlang.model.article.dtos.ArticleHomeDto;
import com.jianlang.model.article.pojos.ApArticle;
import com.jianlang.model.article.pojos.ApHotArticles;
import com.jianlang.model.behavior.pojos.ApBehaviorEntry;
import com.jianlang.model.common.dtos.ResponseResult;
import com.jianlang.model.mappers.app.ApArticleMapper;
import com.jianlang.model.mappers.app.ApHotArticlesMapper;
import com.jianlang.model.mappers.app.ApUserArticleListMapper;
import com.jianlang.model.mappers.app.AppShowBehaviorEntryMapper;
import com.jianlang.model.mess.app.ArticleVisitStreamDto;
import com.jianlang.model.user.pojos.ApUser;
import com.jianlang.model.user.pojos.ApUserArticleList;
import com.jianlang.utils.threadlocal.AppThreadLocalUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

//Service加在实现上
@Service
@Log4j2
@SuppressWarnings("all")
public class AppArticleServiceImpl implements AppArticleService {

    private static final short MAXIMUM_PAGE_SIZE = 50;

    @Override
    public ResponseResult load(ArticleHomeDto dto, Short loadType) {
        dto = validateDTO(dto, loadType);
        //get user info
        ApUser user = AppThreadLocalUtils.getUser();
        List<ApArticle> articles = null;
        //user exist
        if (user != null){
            articles = getUserArticles(user, dto, loadType);
        }else{
            articles = getDefaultArticles(dto, loadType);
        }
        return ResponseResult.okResult(articles);
    }

    @Autowired
    private AppShowBehaviorEntryMapper appShowBehaviorEntryMapper;
    @Autowired
    private ApUserArticleListMapper apUserArticleListMapper;
    @Autowired
    private ApArticleMapper apArticleMapper;

    private List<ApArticle> getUserArticles(ApUser user, ArticleHomeDto dto, Short loadType){
        List<ApUserArticleList> list = apUserArticleListMapper.loadArticleIdListByUser(user, dto, loadType);
        List<ApArticle> articles;
        if (! list.isEmpty()){
            articles = apArticleMapper.loadArticleListByIdList(list);
        }else {
            articles = getDefaultArticles(dto, loadType);
        }
        return articles;
    }

    /**
     * get default article
     * @param dto
     * @param loadType
     * @return
     */
    private List<ApArticle> getDefaultArticles(ArticleHomeDto dto, Short loadType){
        return apArticleMapper.loadArticleListByLocation(dto, loadType);
    }

    @Override
    public ResponseResult updateArticleView(ArticleVisitStreamDto dto) {
        int rows = apArticleMapper.updateArticleView(dto.getArticleId(),
                dto.getView(),dto.getCollect(),dto.getCommont(),dto.getLike());
        log.info("更新文章阅读数#articleId：{},dto：{}", dto.getArticleId(), JSON.toJSONString(dto),rows);
        return ResponseResult.okResult(rows);
    }

    @Override
    public ResponseResult loadV2(ArticleHomeDto dto, Short loadType, boolean firstPage) {
        dto = validateDTO(dto, loadType);
        //get user info
        ApUser user = AppThreadLocalUtils.getUser();

        //in first page?
        if (firstPage){
            List<ApArticle> cacheArticles = getCacheArticleV2(dto);
            if (cacheArticles.size() > 0){
                log.info(" loaded cache article tag:{}", dto.getTag());
                return ResponseResult.okResult(cacheArticles);
            }
        }

        if (user != null) {
            List<ApArticle> userArticleList = getUserArticlesV2(user, dto, loadType);
            return ResponseResult.okResult(userArticleList);
        } else {
            List<ApArticle> defaultArticleList = getDefaultArticlesV2(dto, loadType);
            return ResponseResult.okResult(defaultArticleList);
        }
    }


    @Autowired
    private ApHotArticlesMapper apHotArticlesMapper;

    /**
     * 加载默认的热文章数据
     * @param dto
     * @param loadType
     * @return
     */
    private List<ApArticle> getDefaultArticlesV2(ArticleHomeDto dto, Short loadType) {
        List<ApArticle> retArticle = new ArrayList<>();
        List<ApHotArticles> hotArticles = apHotArticlesMapper.loadHotListByLocation(dto, loadType);
        if (hotArticles != null && hotArticles.size() != 0){
            for (ApHotArticles hotArticle : hotArticles) {
                ApArticle apArticle = apArticleMapper.selectById(Long.valueOf(hotArticle.getArticleId()));
                retArticle.add(apArticle);
            }
        }
        return retArticle;
    }

    private List<ApArticle> getUserArticlesV2(ApUser user, ArticleHomeDto dto, Short loadType) {
        if(user == null){
            return Lists.newArrayList();
        }
        Long userId = user.getId();
        ApBehaviorEntry apBehaviorEntry = appShowBehaviorEntryMapper.selectByUserIdOrEquipmentId(userId, null);
        // 行为实体找以及注册了，逻辑上这里是必定有值得，除非参数错误
        if(apBehaviorEntry==null){
            return Lists.newArrayList();
        }
        Integer entryId =  apBehaviorEntry.getId();
        if (entryId == null) {
            entryId = 0;
        }
        List<ApHotArticles> hotArticles = apHotArticlesMapper.loadArticleIdListByEntryId(entryId, dto, loadType);
        //search from hot articles
        if (!hotArticles.isEmpty()){
            List<Integer> articleIdList = hotArticles.stream().map(p -> p.getArticleId()).collect(Collectors.toList());
            List<ApArticle> articles = apArticleMapper.loadArticleListByIdListV2(articleIdList);
            return articles;
        } else {
            return getDefaultArticlesV2(dto, loadType);
        }
    }


    @Autowired
    private StringRedisTemplate redisTemplate;

    private List<ApArticle> getCacheArticleV2(ArticleHomeDto dto) {
        log.info("Checking hot artcle in cache... tage:{}", dto.getTag());
        String articles = redisTemplate.opsForValue().get(ArticleConstants.HOT_ARTICLE_FIRST_PAGE + dto.getTag());
        if (StringUtils.isEmpty(articles)){
            return Lists.newArrayList();
        }
        List<ApArticle> hotArticles = JSON.parseArray(articles, ApArticle.class);
        log.info("Getting cache hot article tag:{}, size:{}", dto.getTag(), hotArticles.size());
        return hotArticles;
    }

    private ArticleHomeDto validateDTO(ArticleHomeDto dto, Short loadType){
        //validate params
        if (dto == null){
            dto = new ArticleHomeDto();
        }
        //validate Date
        if (dto.getMaxBehotTime() == null){
            dto.setMaxBehotTime(new Date());
        }
        if (dto.getMinBehotTime() == null){
            dto.setMinBehotTime(new Date());
        }
        //validate page size
        Integer pageSize = dto.getSize();
        if (pageSize==null || pageSize <= 0){
            pageSize = 20;
        }
        //ensure maximum page size
        pageSize = Math.min(pageSize, MAXIMUM_PAGE_SIZE);
        dto.setSize(pageSize);

        //validate channel
        if(StringUtils.isEmpty(dto.getTag())){
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        //
        if(!loadType.equals(ArticleConstants.LOADTYPE_LOAD_MORE) && !loadType.equals(ArticleConstants.LOADTYPE_LOAD_NEW)){
            loadType = ArticleConstants.LOADTYPE_LOAD_MORE;
        }
        return dto;
    }
}
