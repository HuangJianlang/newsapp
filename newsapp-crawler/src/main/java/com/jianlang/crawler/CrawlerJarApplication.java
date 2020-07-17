package com.jianlang.crawler;

import com.jianlang.crawler.manager.ProcessingFlowManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrawlerJarApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrawlerJarApplication.class,args);
    }
}
