package com.jianlang.article.service.impl;

import com.jianlang.article.service.AppArticleInfoService;
import com.jianlang.model.article.dtos.ArticleInfoDto;
import com.jianlang.model.article.pojos.ApArticleConfig;
import com.jianlang.model.article.pojos.ApArticleContent;
import com.jianlang.model.article.pojos.ApAuthor;
import com.jianlang.model.article.pojos.ApCollection;
import com.jianlang.model.behavior.pojos.ApBehaviorEntry;
import com.jianlang.model.behavior.pojos.ApLikesBehavior;
import com.jianlang.model.behavior.pojos.ApUnlikesBehavior;
import com.jianlang.model.common.dtos.ResponseResult;
import com.jianlang.model.common.enums.AppHttpCodeEnum;
import com.jianlang.model.crawler.core.parse.ZipUtils;
import com.jianlang.model.mappers.app.*;
import com.jianlang.model.user.pojos.ApUser;
import com.jianlang.model.user.pojos.ApUserFollow;
import com.jianlang.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@SuppressWarnings("all")
public class AppArticleInfoServiceImpl implements AppArticleInfoService {

    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;
    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
    @Autowired
    private AppShowBehaviorEntryMapper appShowBehaviorEntryMapper;
    @Autowired
    private ApCollectionMapper apCollectionMapper;
    @Autowired
    private ApLikesBehaviorMapper apLikesBehaviorMapper;
    @Autowired
    private ApUnlikesBehaviorMapper apUnlikesBehaviorMapper;
    @Autowired
    private ApAuthorMapper apAuthorMapper;
    @Autowired
    private ApUserFollowMapper apUserFollowMapper;

    @Override
    public ResponseResult loadArticleBehavior(ArticleInfoDto dto) {
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null && dto.getEquipmentId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        //1.get user id
        Long id = null;
        if(user != null){
            id = user.getId();
        }
        ApBehaviorEntry apBehaviorEntry = appShowBehaviorEntryMapper.selectByUserIdOrEquipmentId(id, dto.getEquipmentId());

        String burst = "";
        boolean isCollection=false, isLike=false, isUnlike=false, isFollow=false;
        //2 get collection, like, unlike based on entry_id and article id
        ApCollection apCollection = apCollectionMapper.selectForEntryId(burst, apBehaviorEntry.getId(), dto.getArticleId(), ApCollection.Type.ARTICLE.getCode());
        if (apCollection != null){
            isCollection = true;
        }

        //like
        ApLikesBehavior apLikesBehavior = apLikesBehaviorMapper.selectLastLike(burst, apBehaviorEntry.getId(), dto.getArticleId(), ApLikesBehavior.Type.ARTICLE.getCode());
        if (apLikesBehavior != null && apLikesBehavior.getOperation() == ApLikesBehavior.Operation.LIKE.getCode()){
            isLike = true;
        }

        //unlike
        ApUnlikesBehavior apUnlikesBehavior = apUnlikesBehaviorMapper.selectLastUnLike(apBehaviorEntry.getId(), dto.getArticleId());
        if (apUnlikesBehavior != null && apUnlikesBehavior.getType() == ApUnlikesBehavior.Type.UNLIKE.getCode()){
            isUnlike = true;
        }

        //5 get app login user(apUser) id base on author id
        ApAuthor apAuthor = apAuthorMapper.selectById(dto.getAuthorId());
        if (user != null && apAuthor!=null && apAuthor.getUserId()!=null){
            ApUserFollow apUserFollow = apUserFollowMapper.selectByFollowId(burst, user.getId(), apAuthor.getUserId().intValue());
            if (apUserFollow != null){
                isFollow=true;
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("isfollow", isFollow);
        data.put("islike", isLike);
        data.put("isunlike", isUnlike);
        data.put("incollection", isCollection);
        return ResponseResult.okResult(data);
    }

    /**
     * 根据article id 查询config信息
     * 判断当前文章是否被删除
     * @param articleId
     * @return
     */
    @Override
    public ResponseResult getArticleInfo(Integer articleId) {
        if (articleId == null || articleId < 1){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        Map<String, Object> data = new HashMap<>();

        ApArticleConfig apArticleConfig = apArticleConfigMapper.selectByArticleId(articleId);
        if (apArticleConfig == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }else if(! apArticleConfig.getIsDelete()){
            ApArticleContent apArticleContent = apArticleContentMapper.selectByArticleId(articleId);
            String content = ZipUtils.gunzip(apArticleContent.getContent());
            apArticleContent.setContent(content);
            data.put("content", apArticleContent);
        }
        data.put("config", apArticleConfig);
        return ResponseResult.okResult(data);
    }
}
