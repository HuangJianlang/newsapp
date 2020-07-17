package com.jianlang.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.jianlang.article.service.ApArticleSearchService;
import com.jianlang.article.utils.Trie;
import com.jianlang.common.common.contants.ESIndexConstants;
import com.jianlang.model.article.dtos.UserSearchDto;
import com.jianlang.model.article.pojos.ApArticle;
import com.jianlang.model.article.pojos.ApAssociateWords;
import com.jianlang.model.article.pojos.ApHotWords;
import com.jianlang.model.behavior.pojos.ApBehaviorEntry;
import com.jianlang.model.common.dtos.ResponseResult;
import com.jianlang.model.common.enums.AppHttpCodeEnum;
import com.jianlang.model.mappers.app.*;
import com.jianlang.model.user.pojos.ApUser;
import com.jianlang.model.user.pojos.ApUserSearch;
import com.jianlang.utils.threadlocal.AppThreadLocalUtils;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("all")
public class ApArticleSearchServiceImpl implements ApArticleSearchService {

    private static final String ASSOCIATE_LIST_KEY = "associate_list";

    @Autowired
    private AppShowBehaviorEntryMapper appShowBehaviorEntryMapper;
    @Autowired
    private ApUserSearchMapper apUserSearchMapper;
    @Autowired
    private ApHotWordsMapper apHotWordsMapper;
    @Autowired
    private ApAssociateWordsMapper apAssociateWordsMapper;
    @Autowired
    private JestClient client;
    @Autowired
    private ApArticleMapper apArticleMapper;

    public ResponseResult getEntryId(UserSearchDto dto){
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
        return ResponseResult.okResult(apBehaviorEntry.getId());
    }

    @Override
    public ResponseResult findUserSearch(UserSearchDto dto) {
        if(dto.getPageSize() > 50 || dto.getPageSize() < 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ResponseResult result = getEntryId(dto);
        if (result.getCode() != AppHttpCodeEnum.SUCCESS.getCode()){
            return result;
        }

        List<ApUserSearch> apUserSearches = apUserSearchMapper.selectByEntryId((int) result.getData(), dto.getPageSize());
        return ResponseResult.okResult(apUserSearches);
    }

    @Override
    public ResponseResult delUserSearch(UserSearchDto dto) {
        if (dto.getHisList() == null || dto.getHisList().size() <= 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //获取行为实体id
        ResponseResult result = getEntryId(dto);
        if (result.getCode() != AppHttpCodeEnum.SUCCESS.getCode()){
            return result;
        }
        List<Integer> searchIds = dto.getHisList().stream().map(ApUserSearch::getId).collect(Collectors.toList());
        int code = apUserSearchMapper.delUserSearch((int) result.getData(), searchIds);
        return ResponseResult.okResult(code);
    }

    @Override
    public ResponseResult clearUserSearch(UserSearchDto dto) {
        ResponseResult result = getEntryId(dto);
        if (result.getCode() != AppHttpCodeEnum.SUCCESS.getCode()){
            return result;
        }
        int code = apUserSearchMapper.clearUserSearch((int) result.getData());
        return ResponseResult.okResult(code);
    }

    @Override
    public ResponseResult hotKeyWords(String date) {
        if (StringUtils.isEmpty(date)){
            date = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        }
        List<ApHotWords> apHotWords = apHotWordsMapper.queryByHotDate(date);
        return ResponseResult.okResult(apHotWords);
    }

    @Override
    public ResponseResult searchAssociateWords(UserSearchDto dto) {
        if(dto.getPageSize() > 50 || dto.getPageSize() < 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        List<ApAssociateWords> apAssociateWords = apAssociateWordsMapper.selectByAssociateWords("%"+dto.getSearchWords()+"%", dto.getPageSize());
        return ResponseResult.okResult(apAssociateWords);
    }

    @Override
    public ResponseResult esArticleSearch(UserSearchDto dto) {
        //保存搜索记录, 只在第一页查询时进行保存
        if(dto.getFromIndex()==0){
            ResponseResult result = getEntryId(dto);
            if (result.getCode() != AppHttpCodeEnum.SUCCESS.getCode()){
                return result;
            }
            saveUserSearch((int)result.getData(), dto.getSearchWords());
        }
        //构建查询条件 client
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //name 是根据在kibana中设置的定义
        //按照关键字查询， 分页查询
        searchSourceBuilder.query(QueryBuilders.matchQuery("title", dto.getSearchWords()));
        searchSourceBuilder.from(dto.getFromIndex());
        searchSourceBuilder.size(dto.getPageSize());
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(ESIndexConstants.ARTICLE_INDEX)
                .addType(ESIndexConstants.DEFAULT_DOC)
                .build();
        try {
            //获取结果
            SearchResult searchResult = client.execute(search);
            List<ApArticle> apArticleList = searchResult.getSourceAsObjectList(ApArticle.class);
            List<ApArticle> resultList = new ArrayList<>();
            for (ApArticle apArticle : apArticleList) {
                apArticle = apArticleMapper.selectById(Long.valueOf(apArticle.getId()));
                if (apArticle != null){
                    resultList.add(apArticle);
                }
            }
            return ResponseResult.okResult(resultList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
    }

    @Override
    public ResponseResult saveUserSearch(Integer entryId, String searchWord) {
        int cnt = apUserSearchMapper.checkExist(entryId, searchWord);
        if (cnt > 0){
            return ResponseResult.okResult(cnt);
        }
        ApUserSearch apUserSearch = new ApUserSearch();
        apUserSearch.setEntryId(entryId);
        apUserSearch.setKeyword(searchWord);
        apUserSearch.setStatus(1);;
        apUserSearch.setCreatedTime(new Date());
        int code = apUserSearchMapper.insert(apUserSearch);
        return ResponseResult.okResult(code);
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public ResponseResult searchAssociateV2(UserSearchDto userSearchDto) {
        if (userSearchDto != null && userSearchDto.getPageSize() > 50){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //search in cache -> if not exist -> to database and save it to cache
        String associateList = redisTemplate.opsForValue().get(ASSOCIATE_LIST_KEY);
        List<ApAssociateWords> apAssociateWords = null;
        if (StringUtils.isNotEmpty(associateList)){
            apAssociateWords = JSON.parseArray(associateList, ApAssociateWords.class);
        } else {
            apAssociateWords = apAssociateWordsMapper.selectAllAssociateWords();
            redisTemplate.opsForValue().set(ASSOCIATE_LIST_KEY, JSON.toJSONString(apAssociateWords));
        }

        //Trie to search data
        Trie tree = new Trie();
        for (ApAssociateWords word : apAssociateWords) {
            tree.insert(word.getAssociateWords());
        }

        List<String> ret = tree.startWith(userSearchDto.getSearchWords());
        List<ApAssociateWords> recommandWords = new ArrayList<>();
        for (String word : ret) {
            ApAssociateWords associateWords = new ApAssociateWords();
            associateWords.setAssociateWords(word);
            recommandWords.add(associateWords);
        }
        return ResponseResult.okResult(recommandWords);
    }
}
