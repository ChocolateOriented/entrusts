package com.entrusts.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 签名验证类
 */
public class SignatureUtils {

    private static final Logger logger = LoggerFactory.getLogger(SignatureUtils.class);

    /**
     * 生成签名用的时间戳
     *
     * @param timestamp 当前时间戳
     * @return 签名用的时间戳
     */
    public static String generateTimestamp(long timestamp) {
        String sTimestamp = String.valueOf(timestamp);
        return timestamp % 2 == 0 ? sTimestamp.substring(0, 10) : sTimestamp.substring(sTimestamp.length() - 10);
    }

    /**
     * 生成未登陆用户签名
     *
     * @param content      请求content post请求为空则传{}，get请求不传
     * @param mo9Timestamp 签名用的时间戳
     * @param privateKey   私钥
     * @return
     */
    public static String generateUnLoginUserSign(String content, String mo9Timestamp, String privateKey) {
        return EncryptionUtils.md5Encode(content + mo9Timestamp + privateKey);
    }

    /**
     * 生成登陆用户签名
     *
     * @param content      请求content post请求为空则传{}，get请求不传
     * @param mo9Timestamp 签名用的时间戳
     * @param privateKey   私钥
     * @param accessToken  用户登陆凭证
     * @return
     */
    public static String generateLoginUserSign(String content, String mo9Timestamp, String privateKey, String accessToken) {
        return EncryptionUtils.md5Encode(content + mo9Timestamp + privateKey + accessToken);
    }

    public static String buildCoreWebSign(String json, long timestamp, String secrt, String[] exclude) {
        Set<String> excludeFields = new HashSet<>();
        if (exclude != null && exclude.length > 0) {
            excludeFields.addAll(Arrays.asList(exclude));
        }

        JSONObject jsonObject = JSON.parseObject(json);
        Map<String, String> arguments = new HashMap<>();
        Set<String> keySet = jsonObject.keySet();
        Iterator<String> var9 = keySet.iterator();

        while (true) {
            String key;
            do {
                if (!var9.hasNext()) {
                    String sign = sign(arguments, timestamp, secrt);
                    return sign;
                }

                key = (String) var9.next();
            } while (!excludeFields.isEmpty() && excludeFields.contains(key));

            String value = jsonObject.getString(key);
            arguments.put(key, value);
        }
    }

    private static String sign(Map<String, String> params, long timestamp, String privateKey) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        StringBuilder content = new StringBuilder();

        String sign;
        for (int i = 0; i < keys.size(); ++i) {
            sign = (String) keys.get(i);
            String value = (String) params.get(sign);
            content.append(sign);
            content.append("=");
            content.append(value);
            content.append("&");
        }

        String signSource = content.toString().substring(0, content.lastIndexOf("&"));
        signSource = signSource + Long.toString(timestamp);
        signSource = signSource + privateKey;
        if (logger.isDebugEnabled()) {
            logger.debug("签名原串：{}", signSource);
        }

        sign = md5(signSource);
        if (logger.isDebugEnabled()) {
            logger.debug("签名结果：{}", signSource);
        }

        return sign;
    }

    private static String md5(String source) {
        MessageDigest md = null;

        try {
            byte[] bt = source.getBytes("UTF-8");
            md = MessageDigest.getInstance("MD5");
            md.update(bt);
            return bytesToHexString(md.digest());
        } catch (NoSuchAlgorithmException var4) {
            logger.error("非法摘要算法", var4);
            throw new RuntimeException("非法摘要算法", var4);
        } catch (UnsupportedEncodingException var5) {
            logger.error("不支持UTF-8编码", var5);
            return null;
        }
    }

    
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
