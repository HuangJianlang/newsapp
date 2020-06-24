package com.jianlang.common.quartz;

import com.google.common.collect.Maps;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Map;

/**
 * 所有任务的自动管理抽象类
 */
public abstract class AbstractJob extends QuartzJobBean {

    /**
     * 执行完成后从数据库中删除
     * @return
     */
    public boolean isComplateAfterDelete(){return true;}

    /**
     * 是否启动自动尝试恢复
     * @return
     */
    public boolean isStartAutoRecovery(){return true;}

    /**
     * The name of a job
     * @return
     */
    public String name(){return this.getClass().getName();}

    /**
     * The group of a job
     * @return
     */
    public String group(){return "default";}


    public String descJob(){return "";}

    /**
     * the description of trigger
     * @return
     */
    public String descTrigger(){return "";}


    public Map<String,?> initParam(){return Maps.newHashMap();}


    public boolean isAutoOverwrite(){return true;}

    /**
     * 返回调度策略表达式,可以多个
     * @return
     */
    public abstract String[] triggerCron();

    /**
     * 如果是@DisallowConcurrentExecution，是否继承上次任务执行的结果
     * 该方法未做实现
     * @return
     */
    @Deprecated
    public boolean isExtendPreviouData(){return false;}

}