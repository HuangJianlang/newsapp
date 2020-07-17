package com.jianlang.crawler.service.impl;

import com.jianlang.crawler.service.CrawlerNewsCommentService;
import com.jianlang.model.crawler.pojos.ClNewsComment;
import com.jianlang.model.mappers.crawlers.ClNewsCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("all")
public class CrawlerNewsCommentServiceImpl implements CrawlerNewsCommentService {

    @Autowired
    private ClNewsCommentMapper clNewsCommentMapper;

    @Override
    public void saveClNewsComment(ClNewsComment clNewsComment) {
        clNewsCommentMapper.insert(clNewsComment);
    }
}
