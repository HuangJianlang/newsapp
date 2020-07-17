package com.jianlang.model.mappers.admin;

import com.jianlang.model.admin.pojos.AdChannel;

import java.util.List;

public interface AdChannelMapper {
    /**
     * 查询所有
     */
    public List<AdChannel> selectAll();

    AdChannel selectByPrimaryKey(Integer id);

    AdChannel selectByChannelName(String channelName);
}