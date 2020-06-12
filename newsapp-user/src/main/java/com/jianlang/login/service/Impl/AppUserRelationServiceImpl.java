package com.jianlang.login.service.Impl;

import com.jianlang.common.zookeeper.sequence.Sequences;
import com.jianlang.model.article.pojos.ApAuthor;
import com.jianlang.model.behavior.dtos.FollowBehaviorDto;
import com.jianlang.model.common.dtos.ResponseResult;
import com.jianlang.model.common.enums.AppHttpCodeEnum;
import com.jianlang.model.mappers.app.ApAuthorMapper;
import com.jianlang.model.mappers.app.ApUserFanMapper;
import com.jianlang.model.mappers.app.ApUserFollowMapper;
import com.jianlang.model.mappers.app.ApUserMapper;
import com.jianlang.model.user.dtos.UserRelationDto;
import com.jianlang.model.user.pojos.ApUser;
import com.jianlang.model.user.pojos.ApUserFan;
import com.jianlang.model.user.pojos.ApUserFollow;
import com.jianlang.login.service.AppFollowBehaviorService;
import com.jianlang.login.service.AppUserRelationService;
import com.jianlang.utils.common.BurstUtils;
import com.jianlang.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@SuppressWarnings("all")
public class AppUserRelationServiceImpl implements AppUserRelationService {

    @Autowired
    private ApAuthorMapper apAuthorMapper;
    @Autowired
    private ApUserMapper apUserMapper;
    @Autowired
    private ApUserFollowMapper apUserFollowMapper;
    @Autowired
    private ApUserFanMapper apUserFanMapper;
    @Autowired
    private Sequences sequences;
    @Autowired
    private AppFollowBehaviorService appFollowBehaviorService;

    /**
     * 用户关注和取消关注操作
     * @param dto
     * @return
     */
    @Override
    public ResponseResult follow(UserRelationDto dto) {
        if (dto.getOperation()==null || dto.getOperation() < 0 || dto.getOperation() > 1){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "无效操作参数");
        }
        //获取作者id followid 当前关注用户的id
        Integer followId = dto.getUserId();
        if( followId == null && dto.getAuthorId()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "FollowId 和 AuthorId 为空");
        }else if(followId == null){
            //根据作者信息查询
            ApAuthor apAuthor = apAuthorMapper.selectById(dto.getAuthorId());
            if (apAuthor != null){
                followId = apAuthor.getUserId().intValue();
            }
        }
        if (followId == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "关注人不存在");
        }
        //判断当前用户是否已经关注
        ApUser user = AppThreadLocalUtils.getUser();
        if (user != null){
            if (dto.getOperation() == 0){
                return followByUserId(user, followId, dto.getArticleId());
                //关注操作
                //保存 Ap_user_follow ap_user_fan
                //保存关注行为
            }else{
                return UnfollowByUserId(user, followId);
                //0 关注
                //取消关注操作
                //删除 Ap_user_follow ap_user_fan
            }
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
    }

    /**
     * 处理关注操作
     * 保存 Ap_user_follow ap_user_fan
     * @param user
     * @param followId
     * @param authorId
     * @return
     */
    private ResponseResult followByUserId(ApUser user, Integer followId, Integer articleId) {
        //判断关注的用户是否存在
        ApUser followingUser = apUserMapper.selectById(followId);
        if (followingUser == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "关注用户不存在");
        }
        ApUserFollow apUserFollow = apUserFollowMapper.selectByFollowId("", user.getId(), followId);
        if (apUserFollow != null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST);
        }

        ApUserFan apUserFan = apUserFanMapper.selectByFansId("", followId, user.getId());
        if (apUserFan == null){
            apUserFan = new ApUserFan();
            apUserFan.setId(sequences.sequenceApUserFan());
            apUserFan.setUserId(followId);
            apUserFan.setFansId(user.getId());
            apUserFan.setFansName(user.getName());
            apUserFan.setLevel((short)0);
            apUserFan.setIsDisplay(true);
            apUserFan.setIsShieldComment(false);
            apUserFan.setIsShieldLetter(false);
            apUserFan.setBurst(BurstUtils.encrypt(apUserFan.getId(), apUserFan.getUserId()));
            apUserFanMapper.insert(apUserFan);
        }
        apUserFollow = new ApUserFollow();
        apUserFollow.setId(sequences.sequenceApUserFollow());
        apUserFollow.setUserId(user.getId());
        apUserFollow.setFollowId(followId);
        apUserFollow.setFollowName(followingUser.getName());
        apUserFollow.setCreatedTime(new Date());
        apUserFollow.setLevel((short) 0);
        apUserFollow.setIsNotice(true);
        apUserFollow.setBurst(BurstUtils.encrypt(apUserFollow.getId(),apUserFollow.getUserId()));
        int code = apUserFollowMapper.insert(apUserFollow);
        //记录行为表
        FollowBehaviorDto dto = new FollowBehaviorDto();
        dto.setFollowId(followId);
        dto.setArticleId(articleId);
        appFollowBehaviorService.saveFollowBehavior(dto);
        return ResponseResult.okResult(code);
    }

    /**
     * 取消关注操作
     * @param user
     * @param followId
     * @return
     */
    private ResponseResult UnfollowByUserId(ApUser user, Integer followId) {
        ApUserFollow apUserFollow = apUserFollowMapper.selectByFollowId("", user.getId(), followId);
        if (apUserFollow == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        ApUserFan apUserFan = apUserFanMapper.selectByFansId("", followId, user.getId());
        if (apUserFan != null){
            apUserFanMapper.deleteByFansId("", followId, user.getId());
        }
        int code = apUserFollowMapper.deleteByFollowId("", user.getId(), followId);
        return ResponseResult.okResult(code);
    }
}
