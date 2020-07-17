package com.jianlang.crawler.service;

import com.jianlang.model.crawler.core.parse.ParseItem;
import com.jianlang.model.crawler.pojos.ClNewsAdditional;

import java.util.Date;
import java.util.List;

public interface CrawlerNewsAdditionalService {
    void saveAdditional(ClNewsAdditional clNewsAdditional);

    /**
     * 根据当前需要更新的时间查询
     * @param currentDate
     * @return
     */
    public List<ClNewsAdditional> queryListByNeedUpdate(Date currentDate);

    /**
     * 条件查询
     * @param clNewsAdditional
     * @return
     */
    List<ClNewsAdditional> queryList(ClNewsAdditional clNewsAdditional);

    public boolean checkExist(String url);

    public ClNewsAdditional getAdditionalByUrl(String url);

    /**
     * 是否是已存在的URL
     *
     * @return
     */
    public boolean isExistsUrl(String url);

    public void updateAdditional(ClNewsAdditional clNewsAdditional);

    /**
     * 再去爬去数据
     * @param additionalList
     * @return
     */
    public List<ParseItem> toParseItem(List<ClNewsAdditional> additionalList);

    /**
     * 获取增量的统计数据
     * @param currentDate
     * @return
     */
    public List<ParseItem> queryIncrementParseItem(Date currentDate);
}
