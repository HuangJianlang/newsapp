package com.jianlang.model.crawler.pojos;

import lombok.Data;

import java.util.Date;
@Data
public class ClIpPool {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_ip_pool.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_ip_pool.supplier
     *
     * @mbggenerated
     */
    private String supplier;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_ip_pool.ip
     *
     * @mbggenerated
     */
    private String ip;

    /**
     * 端口号
     */
    private int port;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 耗时
     */
    private Integer duration;

    /**
     * 错误信息
     */
    private String error;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_ip_pool.is_enable
     *
     * @mbggenerated
     */
    private Boolean isEnable;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_ip_pool.ranges
     *
     * @mbggenerated
     */
    private String ranges;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cl_ip_pool.created_time
     *
     * @mbggenerated
     */
    private Date createdTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnable() {
        return isEnable;
    }

    public void setEnable(Boolean enable) {
        isEnable = enable;
    }

    public String getRanges() {
        return ranges;
    }

    public void setRanges(String ranges) {
        this.ranges = ranges;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}