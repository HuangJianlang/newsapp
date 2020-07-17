package com.jianlang.crawler.service.impl;

import com.jianlang.common.common.util.HMStringUtils;
import com.jianlang.crawler.service.AdLabelService;
import com.jianlang.model.admin.pojos.AdChannel;
import com.jianlang.model.admin.pojos.AdChannelLabel;
import com.jianlang.model.admin.pojos.AdLabel;
import com.jianlang.model.mappers.admin.AdChannelLabelMapper;
import com.jianlang.model.mappers.admin.AdChannelMapper;
import com.jianlang.model.mappers.admin.AdLabelMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@SuppressWarnings("all")
public class AdLabelServiceImpl implements AdLabelService {

    @Autowired
    private AdLabelMapper adLabelMapper;
    @Autowired
    private AdChannelLabelMapper adChannelLabelMapper;
    @Autowired
    private AdChannelMapper adChannelMapper;

    @Override
    public String getLabelIds(String labels) {
        long currentTime = System.currentTimeMillis();
        log.info("Getting channls info by labels:{}", labels);
        List<AdLabel> adLabels = null;
        if (labels != null){
            labels = labels.toLowerCase();
            if (labels.startsWith(" ")){
                labels = labels.substring(1, labels.length());
            }
            List<String> labelList = Arrays.asList(labels.split(" "));
            labelList = new ArrayList<>(labelList);
            adLabels = adLabelMapper.queryAdLabelByLabels(labelList);
            if (adLabels != null && !adLabels.isEmpty()){
                //保存部分labelList
                adLabels = addLabelList(labelList, adLabels);
            } else {
                //保存所有的labelList
                adLabels = addLabelList(labelList);
            }
        }
        // TODO: 6/17/20 Id 与 label不对应
        List<String> labelIdsList = adLabels.stream().map(label -> HMStringUtils.toString(label.getId())).collect(Collectors.toList());
        String result = HMStringUtils.listToStr(labelIdsList, ",");
        log.info("Getting channel info completes, label:{}, labelsIds:{}, time:{}", labels, result, System.currentTimeMillis()-currentTime);
        return result;
    }

    /**
     * filter and save
     * @param labelList
     * @param adLabels
     * @return
     */
    private List<AdLabel> addLabelList(List<String> labelList, List<AdLabel> adLabels) {
        if(labelList !=null && !labelList.isEmpty()){
            for(AdLabel adLabel : adLabels){
                int i = 0;
                while (i < labelList.size()){
                    if(labelList.get(i).contains(adLabel.getName())){
                        labelList.remove(i);
                        continue;
                    }
                    i++;
                }
            }
        }
        if (labelList!=null && ! labelList.isEmpty()){
            adLabels.addAll(addLabelList(labelList));
        }
        return adLabels;
    }

    /**
     * save all
     * @param labelList
     * @return
     */
    private List<AdLabel> addLabelList(List<String> labelList) {
        List<AdLabel> adLabelList = new ArrayList<>();
        for (String label : labelList) {
            adLabelList.add(addLabel(label));
        }
        return adLabelList;
    }

    /**
     * 保存操作
     * @param label
     * @return
     */
    private AdLabel addLabel(String label) {
        AdLabel adLabel = new AdLabel();
        adLabel.setName(label);
        adLabel.setType(Boolean.TRUE);
        adLabel.setCreatedTime(new Date());
        adLabelMapper.insert(adLabel);
        return adLabel;
    }

    @Override
    public Integer getAdChannelByLabelIds(String labels) {
        Integer channelId = 0;
        try {
            channelId = getSecurityAdChannelByLabelIds(labels);
        }catch (Exception e){
            log.error("Getting channel info fail, errorMess:{}", e.getMessage());
        }
        return channelId;
    }

    private Integer getSecurityAdChannelByLabelIds(String labelIds) {
        long currentTime = System.currentTimeMillis();
        log.info("Get channel info, labelIds:{}", labelIds);
        Integer channelId = 0;
        if (StringUtils.isNotEmpty(labelIds)){
            List<String> labelIdsList = Arrays.asList(labelIds.split(","));
            List<AdLabel> adLabelList = adLabelMapper.queryAdLabelByLabelIds(labelIdsList);
            if (adLabelList !=null && !adLabelList.isEmpty()){
                channelId = getAdChannelIdByLabelId(adLabelList.get(0).getId());
            }
            if (channelId == null){
                channelId = 0;
            }
        }
        log.info("Getting channel info complete, Label:{}, channelId:{}, time:{}", labelIds, channelId, System.currentTimeMillis()-currentTime);
        return channelId;
    }

    private Integer getAdChannelIdByLabelId(Integer id) {
        Integer channelId = 0;
        AdChannelLabel adChannelLabel = adChannelLabelMapper.selectByLabelId(id);
        if(adChannelLabel !=null){
            channelId = adChannelLabel.getChannelId();
        }
        return channelId;
    }

    @Override
    public Integer getAdChannelId(String channelName) {
        Integer id = 0;
        AdChannel adChannel = adChannelMapper.selectByChannelName(channelName);
        if (adChannel != null){
            id = adChannel.getId();
        }
        return id;
    }
}
