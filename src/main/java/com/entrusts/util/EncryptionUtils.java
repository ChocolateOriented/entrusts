package com.entrusts.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtils {

    private static final Logger logger = LoggerFactory.getLogger(EncryptionUtils.class);

    private EncryptionUtils() {
    }

    /**
     * 加密算法
     */
    private static final String ALGORITHM_SHA1 = "$sha1$";

    /**
     * 密码加密
     *
     * @param salt     盐值
     * @param password 密码
     * @return 加密后的密码
     */
    public static String password(String salt, String password) {
        String hasedPassword = DigestUtils.sha1Hex(password);
        return DigestUtils.sha1Hex(ALGORITHM_SHA1 + salt + hasedPassword);
    }

    /**
     * sha-1加密
     *
     * @param src
     * @return
     */
    public static String sha1Encode(String src) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(src.getBytes());
            return Hex.encodeHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            logger.error("sha1Encode NoSuchAlgorithmException {}", e);
        }
        return null;
    }

    /**
     * MD5加密
     *
     * @param src
     * @return
     */
    public static String md5Encode(String src) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md.digest(src.getBytes());
            return Hex.encodeHexString(md5Bytes);
        } catch (NoSuchAlgorithmException e) {
            logger.error("sha1Encode NoSuchAlgorithmException {}", e);
        }
        return null;
    }

    /**
     * base64加密
     *
     * @param src
     * @return
     */
    public static String base64Encode(String src) {
        String value = null;
        try {
            value = new String(Base64.encodeBase64(src.getBytes()), "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("base64 encode error!", e);
        }
        return value;
    }

    /**
     * base64解密
     *
     * @param key
     * @return
     */
    public static String base64Decode(String key) {
        String value = null;
        try {
            value = new String(Base64.decodeBase64(key.getBytes()), "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("base64 decode error!", e);
        }
        return value;
    }

    /**
     * 手机号加密
     *
     * @return
     */
    public static String encrypeMobile(String mobile) {
        return mobile.substring(0, 3) + "****" + mobile.substring(7, 11);
    }

}
