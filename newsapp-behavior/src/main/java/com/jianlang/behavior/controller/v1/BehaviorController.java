package com.jianlang.behavior.controller.v1;

import com.jianlang.article.apis.BehaviorControllerApi;
import com.jianlang.behavior.service.AppShowBehaviorService;
import com.jianlang.model.behavior.dtos.ShowBehaviorDto;
import com.jianlang.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/behavior")
public class BehaviorController implements BehaviorControllerApi {

    @Autowired
    private AppShowBehaviorService appShowBehaviorService;
    /**
     * save data from frontend(dto) to database
     * @param dto
     * @return
     */
    @Override
    @RequestMapping("/save_behavior")
    public ResponseResult saveShowBehavior(ShowBehaviorDto dto) {
        return appShowBehaviorService.saveShowBehavior(dto);
    }
}
