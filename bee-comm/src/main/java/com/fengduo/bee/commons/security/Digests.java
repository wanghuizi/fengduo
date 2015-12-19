/*
 * Copyright 2015-2020 Fengduo.com All right reserved. This software is the confidential and proprietary information of
 * Fengduo.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.com.
 */
package com.fengduo.bee.commons.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.lang3.Validate;

import com.fengduo.bee.commons.core.utils.Exceptions;

/**
 * 支持SHA-1/MD5消息摘要的工具类. 返回ByteSource，可进一步被编码为Hex, Base64或UrlSafeBase64
 * 
 * @author zxc May 28, 2015 11:17:41 AM
 */
public class Digests {

    public static final String  MD5              = "MD5";
    public static final String  SHA1_ALGORITHM   = "SHA-1";
    public static final String  SHA256_ALGORITHM = "SHA-256";
    public static final String  SHA512_ALGORITHM = "SHA-512";
    public static final int     HASH_INTERATIONS = 1024;

    private static SecureRandom random           = new SecureRandom();

    /**
     * 对输入字符串进行sha1散列.
     */
    public static byte[] sha1(byte[] input) {
        return digest(input, SHA1_ALGORITHM, null, 1);
    }

    public static byte[] sha1(byte[] input, byte[] salt) {
        return digest(input, SHA1_ALGORITHM, salt, 1);
    }

    /**
     * 获取 SHA-1 消息摘要实例
     * 
     * @return SHA-1 消息摘要实例
     * @throws RuntimeException 当 {@link java.security.NoSuchAlgorithmException} 发生时
     */
    public static byte[] sha1(byte[] input, byte[] salt, int iterations) {
        return digest(input, SHA1_ALGORITHM, salt, iterations);
    }

    /**
     * 获取 SHA-256 消息摘要实例
     * 
     * @return SHA-256 消息摘要实例
     * @throws RuntimeException 当 {@link java.security.NoSuchAlgorithmException} 发生时
     */
    public static byte[] sha256(byte[] input, byte[] salt, int iterations) {
        return digest(input, SHA256_ALGORITHM, salt, iterations);
    }

    /**
     * 获取 SHA-384 消息摘要实例
     * 
     * @return SHA-384 消息摘要实例
     * @throws RuntimeException 当 {@link java.security.NoSuchAlgorithmException} 发生时
     */
    public static byte[] sha384(byte[] input, byte[] salt, int iterations) {
        return digest(input, "SHA-384", salt, iterations);
    }

    /**
     * 获取 SHA-512 消息摘要实例
     * 
     * <pre>
     * 与DigestUtils.sha512Hex方法类似(使用512位SHA加密算法)
     * </pre>
     * 
     * @return SHA-512 消息摘要实例
     * @throws RuntimeException 当 {@link java.security.NoSuchAlgorithmException} 发生时
     */
    public static byte[] sha512(byte[] input, byte[] salt, int iterations) {
        return digest(input, SHA512_ALGORITHM, salt, iterations);
    }

    /**
     * 根据给定密钥生成算法创建密钥
     * 
     * @param algorithm 密钥算法
     * @return 密钥
     * @throws RuntimeException 当 {@link java.security.NoSuchAlgorithmException} 发生时
     */
    private static byte[] getHmacKey(String algorithm) {
        // 初始化KeyGenerator
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
        // 产生密钥
        SecretKey secretKey = keyGenerator.generateKey();
        // 获得密钥
        return secretKey.getEncoded();
    }

    /**
     * 获取 HmaSHA512的密钥
     * 
     * @return HmaSHA384的密钥
     * @throws RuntimeException 当 {@link java.security.NoSuchAlgorithmException} 发生时
     */
    public static byte[] getHmaSHA512key() {
        return getHmacKey("HmacSHA512");
    }

    /**
     * 对字符串进行散列, 支持md5与sha1算法.
     */
    private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            if (salt != null) {
                digest.update(salt);
            }

            byte[] result = digest.digest(input);

            for (int i = 1; i < iterations; i++) {
                digest.reset();
                result = digest.digest(result);
            }
            return result;
        } catch (GeneralSecurityException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 生成随机的Byte[]作为salt.
     * 
     * @param numBytes byte数组的大小
     */
    public static byte[] generateSalt(int numBytes) {
        Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes);

        byte[] bytes = new byte[numBytes];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * 对文件进行md5散列.
     */
    public static byte[] md5(InputStream input) throws IOException {
        return digest(input, MD5);
    }

    /**
     * 对文件进行sha1散列.
     */
    public static byte[] sha1(InputStream input) throws IOException {
        return digest(input, SHA1_ALGORITHM);
    }

    private static byte[] digest(InputStream input, String algorithm) throws IOException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            int bufferLength = 8 * 1024;
            byte[] buffer = new byte[bufferLength];
            int read = input.read(buffer, 0, bufferLength);

            while (read > -1) {
                messageDigest.update(buffer, 0, read);
                read = input.read(buffer, 0, bufferLength);
            }

            return messageDigest.digest();
        } catch (GeneralSecurityException e) {
            throw Exceptions.unchecked(e);
        }
    }
}
