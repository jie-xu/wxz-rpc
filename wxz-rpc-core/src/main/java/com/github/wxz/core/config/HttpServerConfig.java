package com.github.wxz.core.config;



/**
 * @author xianzhi.wang
 * @date 2017/12/24 -13:04
 */
public class HttpServerConfig extends ServerConfig {
    /**
     * 压缩级别
     */
    private int compressionLevel = 6;
    /**
     * 请求最大长度
     */
    private int maxContentLength = 1024 * 1024;
    /**
     * 是否使用SSL证书
     */
    private boolean isEnableSsl = false;
    /**
     * 是否使用自签证书
     */
    private boolean isSelfSignedCertificate = true;
    /**
     * 自签证书使用的域名
     */
    private String selfSignedCertificateDomain = "wxz.com";
    /**
     * 钥匙链
     */
    private String sslKeyCertChainFile = "";
    /**
     * 密钥文件
     */
    private String sslKeyFile = "";
    /**
     * 密码
     */
    private String sslKeyPassword = "";
    /**
     * sysNo
     */
    private String sysNo = "";
    /**
     * 返回结果是否需要gzip
     */
    private boolean isResponseGzip = true;
    /**
     * 是否记录异常的请求日志
     */
    private boolean isStoreErrorRequestLog = false;

    public int getCompressionLevel() {
        return compressionLevel;
    }

    public void setCompressionLevel(int compressionLevel) {
        this.compressionLevel = compressionLevel;
    }

    public int getMaxContentLength() {
        return maxContentLength;
    }

    public void setMaxContentLength(int maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    public boolean getIsEnableSsl() {
        return isEnableSsl;
    }

    public void setIsEnableSsl(boolean isEnableSsl) {
        this.isEnableSsl = isEnableSsl;
    }

    public boolean getIsSelfSignedCertificate() {
        return isSelfSignedCertificate;
    }

    public void setIsSelfSignedCertificate(boolean isSelfSignedCertificate) {
        this.isSelfSignedCertificate = isSelfSignedCertificate;
    }

    public String getSelfSignedCertificateDomain() {
        return selfSignedCertificateDomain;
    }

    public void setSelfSignedCertificateDomain(String selfSignedCertificateDomain) {
        this.selfSignedCertificateDomain = selfSignedCertificateDomain;
    }

    public String getSslKeyCertChainFile() {
        return sslKeyCertChainFile;
    }

    public void setSslKeyCertChainFile(String sslKeyCertChainFile) {
        this.sslKeyCertChainFile = sslKeyCertChainFile;
    }

    public String getSslKeyFile() {
        return sslKeyFile;
    }

    public void setSslKeyFile(String sslKeyFile) {
        this.sslKeyFile = sslKeyFile;
    }

    public String getSslKeyPassword() {
        return sslKeyPassword;
    }

    public void setSslKeyPassword(String sslKeyPassword) {
        this.sslKeyPassword = sslKeyPassword;
    }

    public String getSysNo() {
        return sysNo;
    }

    public void setSysNo(String sysNo) {
        this.sysNo = sysNo;
    }

    public boolean getIsResponseGzip() {
        return isResponseGzip;
    }

    public void setIsResponseGzip(boolean isResponseGzip) {
        this.isResponseGzip = isResponseGzip;
    }

    public boolean getIsStoreErrorRequestLog() {
        return isStoreErrorRequestLog;
    }

    public void setIsStoreErrorRequestLog(boolean isStoreErrorRequestLog) {
        this.isStoreErrorRequestLog = isStoreErrorRequestLog;
    }
}
