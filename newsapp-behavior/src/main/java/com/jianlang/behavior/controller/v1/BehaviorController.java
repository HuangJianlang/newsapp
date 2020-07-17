package com.jianlang.behavior.controller.v1;

import com.jianlang.behavior.apis.BehaviorControllerApi;
import com.jianlang.behavior.service.AppLikesBehaviorService;
import com.jianlang.behavior.service.AppReadBehaviorService;
import com.jianlang.behavior.service.AppShowBehaviorService;
import com.jianlang.behavior.service.AppUnlikesBehaviorService;
import com.jianlang.model.behavior.dtos.LikesBehaviorDto;
import com.jianlang.model.behavior.dtos.ReadBehaviorDto;
import com.jianlang.model.behavior.dtos.ShowBehaviorDto;
import com.jianlang.model.behavior.dtos.UnLikesBehaviorDto;
import com.jianlang.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/behavior")
public class BehaviorController implements BehaviorControllerApi {

    @Autowired
    private AppShowBehaviorService appShowBehaviorService;

    @Autowired
    private AppLikesBehaviorService appLikesBehaviorService;

    @Autowired
    private AppUnlikesBehaviorService appUnlikesBehaviorService;

    @Autowired
    private AppReadBehaviorService appReadBehaviorService;

    @Override
    @PostMapping("/like_behavior")
    public ResponseResult saveLikesBehavior(@RequestBody LikesBehaviorDto dto) {
        return appLikesBehaviorService.saveLikesBehavior(dto);
    }

    /**
     * save data from frontend(dto) to database
     * @param dto
     * @return
     */
    @Override
    @PostMapping("/show_behavior")
    public ResponseResult saveShowBehavior(@RequestBody ShowBehaviorDto dto) {
        return appShowBehaviorService.saveShowBehavior(dto);
    }

    @Override
    @PostMapping("/unlike_behavior")
    public ResponseResult saveUnlikesBehavior(@RequestBody UnLikesBehaviorDto dto) {
        return appUnlikesBehaviorService.saveUnlikeBehavior(dto);
    }

    @Override
    @PostMapping("/read_behavior")
    public ResponseResult saveReadBehavior(@RequestBody ReadBehaviorDto dto) {
        return appReadBehaviorService.saveReadBehavior(dto);
    }
}
