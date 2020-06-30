package com.jianlang.behavior.service.impl;

import com.jianlang.behavior.service.AppShowBehaviorService;
import com.jianlang.model.article.pojos.ApArticle;
import com.jianlang.model.behavior.dtos.ShowBehaviorDto;
import com.jianlang.model.behavior.pojos.ApBehaviorEntry;
import com.jianlang.model.behavior.pojos.ApShowBehavior;
import com.jianlang.model.common.dtos.ResponseResult;
import com.jianlang.model.common.enums.AppHttpCodeEnum;
import com.jianlang.model.mappers.app.AppShowBehaviorEntryMapper;
import com.jianlang.model.mappers.app.AppShowBehaviorMapper;
import com.jianlang.model.user.pojos.ApUser;
import com.jianlang.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@SuppressWarnings("all")
public class AppShowBehaviorServiceImpl implements AppShowBehaviorService {

    @Autowired
    private AppShowBehaviorEntryMapper appShowBehaviorEntryMapper;
    @Autowired
    private AppShowBehaviorMapper appShowBehaviorMapper;

    @Override
    public ResponseResult saveShowBehavior(ShowBehaviorDto dto) {
        //get user info and device id
        //get user id or device id get behavior entry id
        //get article ids
        //query ap_show_behavior table based on entry id and article id to filter(passages already in list)
        ApUser user = AppThreadLocalUtils.getUser();
        if(user == null && dto.getEquipmentId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        Long userId = null;
        if(user != null){
            userId = user.getId();
        }
        ApBehaviorEntry apBehaviorEntry = appShowBehaviorEntryMapper.selectByUserIdOrEquipmentId(userId, dto.getEquipmentId());
        if (apBehaviorEntry == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //作为条件查询时候，List类型不能作为条件作为查询，但是数组可以
        //List<ApArticle> articleIds = dto.getArticleIds();
        Integer[] articleIds = new Integer[dto.getArticleIds().size()];
        for(int i=0; i<articleIds.length; i++){
            articleIds[i] = dto.getArticleIds().get(i).getId();
        }

        List<ApShowBehavior> apShowBehaviors = appShowBehaviorMapper.selectListByEntryIdAndArticleIds(apBehaviorEntry.getId(), articleIds);
        //filter data
        List<Integer> articleIdsList = Arrays.asList(articleIds);
        articleIdsList = new ArrayList<>(articleIdsList);
        if (!apShowBehaviors.isEmpty()){
            for(ApShowBehavior behavior : apShowBehaviors){
                Integer articleId = behavior.getArticleId();
                //int index = articleIdsList.indexOf(articleId);
                articleIdsList.remove(articleId);
            }
        }
        //save
        if (!articleIdsList.isEmpty()){
            articleIds = new Integer[articleIdsList.size()];
            articleIdsList.toArray(articleIds);
            appShowBehaviorMapper.saveShowBehavior(articleIds, apBehaviorEntry.getId());
        }

        return ResponseResult.okResult(0);
    }
}
