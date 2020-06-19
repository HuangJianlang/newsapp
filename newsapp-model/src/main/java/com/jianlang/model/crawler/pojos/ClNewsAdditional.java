package com.jianlang.model.crawler.pojos;

import lombok.Data;

import java.util.Date;

/**
 * 用于方向爬去数据时，更新相关信息
 */
@Data
public class ClNewsAdditional {
    private Integer id;
    private Integer newsId;
    private String url;
    private Integer readCount;
    private Integer likes;
    private Integer comment;
    private Integer forward;
    private Integer unlikes;
    private Integer collection;
    private Date createdTime;
    private Date count;
    private Date updatedTime;
    private Date nextUpdateTime;
    private Integer updateNum;



}