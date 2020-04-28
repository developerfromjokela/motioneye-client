/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.database;

import android.annotation.SuppressLint;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESCryptor {
    private static final String ALGORITHM = "AES";
    private static final String KEY = "tsbfJMP22tJhAkp8";

    public static String encrypt(String value) throws Exception {
        Key key = generateKey();
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(AESCryptor.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedByteValue = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);

    }

    public static String decrypt(String value) throws Exception {
        Key key = generateKey();
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(AESCryptor.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte[] decryptedByteValue = cipher.doFinal(decryptedValue64);
        return new String(decryptedByteValue, StandardCharsets.UTF_8);

    }

    private static Key generateKey() {
        return new SecretKeySpec(AESCryptor.KEY.getBytes(), AESCryptor.ALGORITHM);
    }
}
