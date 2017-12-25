package com.github.wxz.core.config;

import io.netty.handler.logging.LogLevel;

/**
 * @author xianzhi.wang
 * @date 2017/12/24 -12:59
 */
public abstract class ServerConfig {
    /**
     *  端口
     */

    private int port = 80;
    /**
     * boss线程数量
     */
    private int bossThreadCount = 1;
    /**
     * worker线程数量;0表示使用默认值
     */
    private int workerThreadCount = RpcSystemConfig.SYSTEM_PROPERTY_PARALLEL;
    /**
     * 业务线程数量;0表示使用默认值
     */
    private int businessThreadCount = RpcSystemConfig.SYSTEM_PROPERTY_PARALLEL;
    /**
     * 连接超时断开时间 读 秒;0表示不设置超时时间;
     */
    private int readerIdleTime = 100;
    /**
     * 连接超时断开时间 写 秒;0表示不设置超时时间;
     */
    private int writerIdleTime = 100;
    /**
     * SO_BACKLOG
     */
    private int soBackLog = 1024;
    /**
     * TCP_NO_DELAY
     */
    private boolean tcpNoDelay = true;
    /**
     * netty日志级别
     */
    private LogLevel logLevel = LogLevel.DEBUG;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getBossThreadCount() {
        return bossThreadCount;
    }

    public void setBossThreadCount(int bossThreadCount) {
        this.bossThreadCount = bossThreadCount;
    }

    public int getWorkerThreadCount() {
        return workerThreadCount;
    }

    public void setWorkerThreadCount(int workerThreadCount) {
        this.workerThreadCount = workerThreadCount;
    }

    public int getBusinessThreadCount() {
        return businessThreadCount;
    }

    public void setBusinessThreadCount(int businessThreadCount) {
        this.businessThreadCount = businessThreadCount;
    }

    public int getReaderIdleTime() {
        return readerIdleTime;
    }

    public void setReaderIdleTime(int readerIdleTime) {
        this.readerIdleTime = readerIdleTime;
    }

    public int getWriterIdleTime() {
        return writerIdleTime;
    }

    public void setWriterIdleTime(int writerIdleTime) {
        this.writerIdleTime = writerIdleTime;
    }

    public int getSoBackLog() {
        return soBackLog;
    }

    public void setSoBackLog(int soBackLog) {
        this.soBackLog = soBackLog;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public boolean getTcpNoDelay() {
        return tcpNoDelay;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }
}
