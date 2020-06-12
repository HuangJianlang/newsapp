package com.jianlang.model.mappers.app;

import com.jianlang.model.article.pojos.ApCollection;
import org.apache.ibatis.annotations.Param;


public interface ApCollectionMapper {
    /**
     *
     * @param burst
     * @param objectId
     * @param entryId 文章id
     * @param type 收藏的类型 0 为文章 1 为动态
     * @return
     */
    ApCollection selectForEntryId(@Param("burst") String burst, @Param("objectId") Integer objectId, @Param("entryId")Integer entryId, @Param("type")Short type);
}
