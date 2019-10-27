package com.example.hanchat.module;

import java.security.MessageDigest;
import java.util.Random;


public class Tools {
    public static String getEncryptedString(String str, String salt){
        str += salt;
        String encryptedstr = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.update(str.getBytes());
            encryptedstr = bytesToHex(digest.digest());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return encryptedstr;
    }

    public static String Encrypt(String password, String[] salts){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                password = Tools.getEncryptedString(password, salts[i]);
            }
        }
        return password;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b: bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public static String getRandomString(int length){
        StringBuffer temp = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0-9
                    temp.append((rnd.nextInt(10)));
                    break;
            }
        }
        return temp.toString();
    }
}
