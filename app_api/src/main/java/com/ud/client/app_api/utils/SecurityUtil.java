package com.ud.client.app_api.utils;

import android.util.Base64;

import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Locale;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by lc++ on 15-9-8.
 */
public class SecurityUtil {

    public static final String DES = "DES/CBC/PKCS5Padding";
    public static final String RSA = "";
    public static final String AES = "";
    private static final String ENCODE = "utf-8";
    private static SecurityUtil instance;
    private SecurityUtil(){}
    public static SecurityUtil getInstance(){
        if(null==instance){
            synchronized (SecurityUtil.class) {
                if(null==instance)instance = new SecurityUtil();
            }
        }
        return instance;
    }

    //-------------------------------工具
    /**
     * 生成一个32位种子
     *
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * byte数组转hex字符串
     * @param buff
     * @return
     */
    public static String byte2Hex(byte buff[]){
        StringBuilder sb = new StringBuilder();
        int len = buff.length;
        String out = null;
        for (int i = 0; i < len; i++) {
//	          out = Integer.toHexString(0xFF & digest[i] + 0xABCDEF); //加任意 salt
            out = Integer.toHexString(0xFF & buff[i]);
            if (out.length() == 1) {
                sb.append("0");//如果为1位 前面补个0
            }
            sb.append(out);
        }
        return sb.toString();
    }

    /**
     * hex字符串转byte数组
     * @param hexStr
     * @return
     */
    public static byte[] hex2Byte(String hexStr){
        if(hexStr.length() < 1)return null;
        byte[] result = new byte[hexStr.length()/2];
        int length = hexStr.length()/2;
        for (int i = 0;i<length; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    //-------------------------------散列算法
    /**
     * 生成md5签名
     * @param msg
     * @return
     */
    public static String md5(String msg){
        String sign = null;
        try{
            byte[] input = msg.getBytes("utf-8");
            MessageDigest md5 = MessageDigest.getInstance("MD5");//初始化md5实例
            md5.update(input);//输入消息
            //方式1
//            BigInteger big = new BigInteger(1,md5.digest());//生成消息摘要
//            sign = big.toString(16);
            //方式2
            byte[] digest = md5.digest();
            sign = byte2Hex(digest);
        }catch (Exception e){
            e.printStackTrace();
        }
        return sign;
    }

    /**
     * 生成sha1签名
     * @param msg
     * @return
     */
    public static String sha1(String msg){
        String sign = null;
        try{
            byte[] input = msg.getBytes("utf-8");
            MessageDigest md5 = MessageDigest.getInstance("SHA-1");//初始化md5实例
            md5.update(input);//输入消息
            //方式1
            BigInteger big = new BigInteger(1,md5.digest());//生成消息摘要
            sign = big.toString(16);
            //方式2
//            byte[] digest = md5.digest();
//            sign = byte2Hex(digest);
        }catch (Exception e){
            e.printStackTrace();
        }
        return sign;
    }

    //--------------------------------aes算法

    //方式１

    public static byte[] encrypt(String content, String password){
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom seRandom = SecureRandom.getInstance("SHA1PRNG");
            seRandom.setSeed(password.getBytes("utf-8"));
            kgen.init(128, seRandom);
//            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(byteContent);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(byte[] content, String password){
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom seRandom = SecureRandom.getInstance("SHA1PRNG");
            seRandom.setSeed(password.getBytes("utf-8"));
            kgen.init(128, seRandom);
//            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    //-------------------兼容模式的aes加解密
    //方式2
    /**
     * aes加密
     * @param input
     * @param key
     * @return
     */
    public static String aesEncrypt(String input, String key){
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            KeyGenerator.getInstance("AES").generateKey();
            cipher.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(key.getBytes(), "AES"));
            byte[] bytes = cipher.doFinal(input.getBytes());
//            return new String(Base64.encode(bytes,Base64.DEFAULT),"utf-8").toString();
            return byte2Hex(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * aes解密
     * @param input
     * @param key
     * @return
     */
    public static String aesDecrypt(String input, String key){
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
//            byte[] bytes = Base64.decode(input, Base64.DEFAULT);
            byte[] bytes = hex2Byte(input);
            bytes = cipher.doFinal(bytes);
            return new String(bytes, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //方式３
    public static String aes(String json) {
        try {
            String data = json;
            String key = "1234567812345678";
            String iv = "1234567812345678";
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
        }
        return null;
    }

    public static String sea(String data) {
        try {
            String key = "1234567812345678";
            String iv = "1234567812345678";
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
        }
        return null;
    }





    //-------------------------------测试des加密算法

    /**
     * 生成key
     * @return
     */
    public byte[] getKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(DES);
            keyGenerator.init(64);
            return keyGenerator.generateKey().getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getStrKey(byte[] key){
        StringBuilder sb = new StringBuilder();
        for(byte b:key){
            sb.append(b);
        }
        return sb.toString();
    }

    /**
     * 生成密钥
     * @param key
     * @return
     */
    public Key getSecret(byte[] key){
        try {
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(DES);
            return secretKeyFactory.generateSecret(desKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 加密
     * @param text
     * @param key
     * @return
     */
    public String encrypt(String text,byte[] key){
        try {
            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(Cipher.ENCRYPT_MODE, getSecret(key));
            byte[] bytes = cipher.doFinal(text.getBytes(ENCODE));
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * @param text
     * @param key
     * @return
     */
    public String decrypt(String text,byte[] key){
        try {
            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(Cipher.DECRYPT_MODE, getSecret(key));
            byte[] bytes = cipher.doFinal(Base64.decode(text, Base64.DEFAULT));
            return new String(bytes,ENCODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //-----------------des算法
    public static String desEncrpt(String input, String key) {
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(DES);
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
            byte[] bytes = cipher.doFinal(input.getBytes());
            return byte2Hex(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String desDecrypt(String input, String key) {
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(DES);
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return new String(cipher.doFinal(hex2Byte(input)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
