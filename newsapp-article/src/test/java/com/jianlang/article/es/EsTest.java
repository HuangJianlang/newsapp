package com.jianlang.article.es;

import com.jianlang.common.common.contants.ESIndexConstants;
import com.jianlang.common.common.pojo.EsIndexEntity;
import com.jianlang.model.article.dtos.ArticleHomeDto;
import com.jianlang.model.article.pojos.ApArticle;
import com.jianlang.model.article.pojos.ApArticleContent;
import com.jianlang.model.crawler.core.parse.ZipUtils;
import com.jianlang.model.mappers.app.ApArticleContentMapper;
import com.jianlang.model.mappers.app.ApArticleMapper;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EsTest {

    @Autowired
    private JestClient jestClient;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;

    @Test
    // TODO: 6/12/20 知道如何保存文章后返回测试 
    public void testSave() throws IOException {

        ArticleHomeDto dto = new ArticleHomeDto();
        dto.setSize(50);
        dto.setTag("__all__");
        List<ApArticle> apArticles = apArticleMapper.loadArticleListByLocation(dto, null);
        for (ApArticle apArticle : apArticles) {
            ApArticleContent apArticleContent = apArticleContentMapper.selectByArticleId(apArticle.getId());

            EsIndexEntity esIndexEntity = new EsIndexEntity();
            esIndexEntity.setId(Long.valueOf(apArticle.getId()));
            esIndexEntity.setChannelId(new Long(apArticle.getChannelId()));
            esIndexEntity.setContent(ZipUtils.gunzip(apArticleContent.getContent()));
            esIndexEntity.setPublishTime(apArticle.getPublishTime());
            esIndexEntity.setStatus(new Long(1));
            esIndexEntity.setTag("article");
            esIndexEntity.setTitle(apArticle.getTitle());
            Index.Builder builder = new Index.Builder(esIndexEntity);
            builder.id(apArticle.getId().toString());
            builder.refresh(true);
            Index index = builder.index(ESIndexConstants.ARTICLE_INDEX).type(ESIndexConstants.DEFAULT_DOC).build();
            JestResult result = jestClient.execute(index);
            if (result != null && !result.isSucceeded()) {
                throw new RuntimeException(result.getErrorMessage() + "插入更新索引失败!");
            }
        }
    }
}