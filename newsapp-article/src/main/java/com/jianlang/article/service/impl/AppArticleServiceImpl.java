package com.jianlang.article.service.impl;

import com.jianlang.article.service.AppArticleService;
import com.jianlang.common.article.constants.ArticleContants;
import com.jianlang.model.article.dtos.ArticleHomeDto;
import com.jianlang.model.article.pojos.ApArticle;
import com.jianlang.model.common.dtos.ResponseResult;
import com.jianlang.model.mappers.app.ApArticleMapper;
import com.jianlang.model.mappers.app.ApUserArticleListMapper;
import com.jianlang.model.user.pojos.ApUser;
import com.jianlang.model.user.pojos.ApUserArticleList;
import com.jianlang.utils.threadlocal.AppThreadLocalUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.nntp.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

//Service加在实现上
@Service
@SuppressWarnings("all")
public class AppArticleServiceImpl implements AppArticleService {

    private static final short MAXIMUM_PAGE_SIZE = 50;

    @Override
    public ResponseResult load(ArticleHomeDto dto, Short loadType) {
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
            dto.setTag(ArticleContants.DEFAULT_TAG);
        }
        //
        if(!loadType.equals(ArticleContants.LOADTYPE_LOAD_MORE) && !loadType.equals(ArticleContants.LOADTYPE_LOAD_NEW)){
            loadType = ArticleContants.LOADTYPE_LOAD_MORE;
        }
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
}