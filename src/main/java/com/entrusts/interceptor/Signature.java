package com.entrusts.interceptor;


import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;

/**
 * 签名验证类
 */
public class Signature {

    private static final String HEAD_TIMESTAMP = "Timestamp";
    private static final String HEAD_SIGN = "Sign";
    private static final String JSON_BODY_ATTRIBUTE = "JSON_REQUEST_BODY";
    private static final String EMPTY_JSON = "{}";

    //签名私钥
    private String privateKey;
    //签名有效时间 单位:s
    private Integer signEffectiveTime;

    /**
     * 验证签名是否正确
     *
     * @param request   请求对象
     * @param needLogin 请求是否需要用户身份信息
     * @return
     */
    public boolean validateSign(HttpServletRequest request, boolean needLogin) {
        String sTimestamp = request.getHeader(HEAD_TIMESTAMP);
        String sign = request.getHeader(HEAD_SIGN);
        if (StringUtils.isBlank(sTimestamp) || StringUtils.isBlank(sign)) {
//            log.info("时间戳{}或签名{}为空", sTimestamp, sign);
            return false;
        }
        long timestamp = Long.parseLong(sTimestamp);
        long currentTimeMillis = getCurrentTimeMillis();
        if (currentTimeMillis - timestamp > signEffectiveTime * 1000L) {
//            log.info("签名时间超时无效");
            return false;
        }
        //获得加密时间戳
        String mo9Timestamp = SignatureUtils.generateTimestamp(timestamp);
        //获得签名内容
        String signContent = getSignContent(request);
        //将请求body存入attribute
        request.setAttribute(JSON_BODY_ATTRIBUTE, signContent);
        //获取最终签名
        String serverSign = getServerSign(signContent, mo9Timestamp, privateKey, needLogin);
        if (!sign.equalsIgnoreCase(serverSign)) {
            // 此日志日后需要删除
//            log.info("签名不正确 request sign {},Timestamp {},server sign{} ", sign, sTimestamp, serverSign);
//            log.info("签名过程 mo9Timestamp:{},signContent:{},needLogin:{}", mo9Timestamp, signContent, needLogin);
            return false;
        }
        return true;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void setSignEffectiveTime(Integer signEffectiveTime) {
        this.signEffectiveTime = signEffectiveTime;
    }

    /**
     * 获取请求内容，如果是get请求则为空
     *
     * @param request 请求对象
     * @return 签名内容
     */
    private String getSignContent(HttpServletRequest request) {
        String content = null;
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            InputStream inputStream = null;
            try {
                inputStream = request.getInputStream();
                content = IOUtils.toString(inputStream, "utf-8");
            } catch (IOException e) {
//                log.error("获取请求body失败{}", e);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        } else {
            content = request.getQueryString();
        }
        if (StringUtils.isBlank(content) || EMPTY_JSON.equals(content)) {
            content = StringUtils.EMPTY;
        }
        return content;
    }

    /**
     * 获得最后签名
     *
     * @param signContent  签名content
     * @param mo9Timestamp 加密时间戳
     * @param privateKey   私钥
     * @param needLogin    请求是否需要用户身份
     * @return 最后签名
     */
    private String getServerSign(String signContent, String mo9Timestamp, String privateKey, boolean needLogin) {
        String serverSign;
        //判断是否需要验证身份
        if (!needLogin) {
            serverSign = SignatureUtils.generateUnLoginUserSign(signContent, mo9Timestamp, privateKey);
        } else {
            serverSign = SignatureUtils.generateLoginUserSign(signContent, mo9Timestamp, privateKey, CommonRequestContext.getInstance().getAccessToken());
        }
        return serverSign;
    }

    private Long getCurrentTimeMillis() {
        String country = CommonRequestContext.getInstance().getCountry();
        if ("ID".equals(country)) {
            Instant instant = Instant.ofEpochMilli(System.currentTimeMillis());
            return Timestamp.valueOf(instant.atZone(ZoneId.of("GMT+7")).toLocalDateTime()).getTime();
        }

        return System.currentTimeMillis();
    }

}