package com.jianlang.crawler.service;

import org.springframework.transaction.annotation.Transactional;

public interface AdLabelService {
    /**
     * 由于方法是以get开头，默认是read-only
     * @param labels 页面中的标签数据, 以逗号分割不同的标签
     * @return ids 标签id, 以逗号分割
     */
    @Transactional(readOnly = false)
    String getLabelIds(String labels);

    /**
     *
     * @param labels 标签id
     * @return 频道id, 若未找到，返回0（未找到）
     */
    @Transactional(readOnly = false)
    Integer getAdChannelByLabelIds(String labels);


}
