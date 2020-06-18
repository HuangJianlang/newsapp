package com.jianlang.model.mappers.admin;

import com.jianlang.model.admin.pojos.AdLabel;

import java.util.List;

public interface AdLabelMapper {
    int insert(AdLabel record);
    //有条件新增
    int insertSelective(AdLabel record);

    int deleteByPrimaryKey(Integer id);

    AdLabel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdLabel record);
    int updateByPrimaryKey(AdLabel record);

    List<AdLabel> queryAdLabelByLabels(List<String> labelList);
    List<AdLabel> queryAdLabelByLabelIds(List<String> labelList);

}
