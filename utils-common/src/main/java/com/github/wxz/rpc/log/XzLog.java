package com.github.wxz.rpc.log;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.wxz.rpc.utils.HostUtils;

import java.util.Date;

/**
 * @author xianzhi.wang
 * @date 2017/12/24 -14:11
 */
public class XzLog {
    //系统编号
    @JSONField(name = "sys_no")
    private String sysNo = "";
    //客户标识
    @JSONField(name = "u_id")
    private String uid = "";
    //操作时间
    @JSONField(name = "o_time", format = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date operateTime = new Date();
    //操作渠道
    @JSONField(name = "o_channel")
    private String operateChannel = "";
    //客户端IP
    @JSONField(name = "c_ip")
    private String clientIp = "";
    //服务端IP
    @JSONField(name = "s_ip")
    private String serverIp = HostUtils.serverIp;
    //终端设备标识(接口单词写错了)
    @JSONField(name = "device_id")
    private String deviceId = "";
    //入参
    @JSONField(name = "in_param")
    private String inParam = "";
    //出参
    @JSONField(name = "out_param")
    private String outParam = "";
    //操作行为标识
    @JSONField(name = "action")
    private String action = "";
    //日志类型
    @JSONField(name = "log_type")
    private String logType = "";
    //日志级别
    @JSONField(name = "log_level")
    private String logLevel = "";
    //事务流水号
    @JSONField(name = "t_id")
    private String transactionId = "";
    //结果
    @JSONField(name = "result")
    private String result = "";
    //错误编号
    @JSONField(name = "error_no")
    private String errorNo = "";
    //错误说明
    @JSONField(name = "error_msg")
    private String errorMsg = "";
    //日志说明
    @JSONField(name = "remark")
    private String remark = "";
    //耗时
    @JSONField(name = "spent_time")
    private int spentTime = 0;
    //操作人
    @JSONField(name = "c_operator")
    private String operator = "";
    //操作系统
    @JSONField(name = "os")
    private String os = "";
    //app版本号
    @JSONField(name = "app_version")
    private String appVersion = "";

    public String getSysNo() {
        return sysNo;
    }

    public void setSysNo(String sysNo) {
        this.sysNo = sysNo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperateChannel() {
        return operateChannel;
    }

    public void setOperateChannel(String operateChannel) {
        this.operateChannel = operateChannel;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getInParam() {
        return inParam;
    }

    public void setInParam(String inParam) {
        this.inParam = inParam;
    }

    public String getOutParam() {
        return outParam;
    }

    public void setOutParam(String outParam) {
        this.outParam = outParam;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorNo() {
        return errorNo;
    }

    public void setErrorNo(String errorNo) {
        this.errorNo = errorNo;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(int spentTime) {
        this.spentTime = spentTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
