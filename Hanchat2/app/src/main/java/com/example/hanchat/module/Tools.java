package com.example.hanchat.module;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/*
    잡다한 작업 모음
 */
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

    private JSONObject mapTOJsonObject(Map<String, ?> data) throws Exception {
        JSONObject json = new JSONObject();
        if (data != null){
            for (Map.Entry<String, ?> entry : data.entrySet()) {
                String key = entry.getKey();
                json.put(key, entry.getValue());
            }
        }
        return json;
    }

    public static Date StringToDate(String input){
        String dateinfo = input.substring(0, 20);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Date date = null;
        try {
            date = simpleDateFormat.parse(dateinfo);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}
