package com.jianlang.model.behavior.dtos;


import com.jianlang.model.annotation.IdEncrypt;
import com.jianlang.model.article.pojos.ApArticle;
import lombok.Data;

import java.util.List;

@Data
public class ShowBehaviorDto {

    // 设备ID
    @IdEncrypt
    Integer equipmentId;
    List<ApArticle> articleIds;

}
