package com.example.hanchat.module;

import android.graphics.Bitmap;
import android.os.Handler;

import androidx.annotation.StringRes;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.provider.Settings.Global.getString;


/*완료*/
//8.16  경고 수정

//먼저 맨위의 패키지 이름부터 바꾸고
//AndroidManifest.xml 파일에서 <uses-permission android:name="android.permission.INTERNET" /> 추가
//자세한 사용법은 드라이브에


public class HTTPConnecter {

    //통신 완료 후에 실행될 콜백 함수 정의를 위한 인터페이스
    public interface Callback{
        //서버에서 받은 데이터를 처리할 콜백 함수 (쓰레드로 실행됨)
        //UIChange로 전달할 데이터를 return
        Object DataReceived(String ReceiveString);


        //데이터를 모두 처리하고 나서 UI 등을 처리할 콜백 함수 (메인 쓰레드에서 실행됨)
        //UI 등과 같이 메인 쓰레드가 아니면 실행할 수 없는 부분을 처리
        //위의 함수에서 Object를 받아서 처리하는 함수를 만드세요
        void HandlerMethod(Object obj);


        //함수 실행 순서 : DataReceived  ->  UIChange
    }

    private static Map<String, HTTPConnecter> instanceMap = new HashMap<>();

    private Handler handler;
    private String Host;

    //생성자로 ip주소, 포트번호를 전달받음
    private HTTPConnecter(String Host){
        this.Host = Host;
        this.handler = new Handler();
    }

    //ip주소, 포트번호를 전달받음
    public static HTTPConnecter getinstance(String Hostip, int Port){
        String host = "http://" + Hostip + ":" + Port;
        if(instanceMap.containsKey(host)){
            return instanceMap.get(host);
        }
        else{
            HTTPConnecter newinstance = new HTTPConnecter(host);
            instanceMap.put(host, newinstance);
            return newinstance;
        }
    }

    //ip주소, 포트번호를 전달받음
    public static HTTPConnecter getinstance(@StringRes int Hostip, int Port){
        String host = "http://" + Hostip + ":" + Port;
        if(instanceMap.containsKey(host)){
            return instanceMap.get(host);
        }
        else{
            HTTPConnecter newinstance = new HTTPConnecter(host);
            instanceMap.put(host, newinstance);
            return newinstance;
        }
    }

    //Post 형식으로 전달할때
    //Pathname : 주소에서 /로 시작해서 ? 전까지의 부분 (ex : "/chatbot_data"), Map과 통신 이후에 수행할 콜백을 받음
    //콜백은 위의 인터페이스 활용
    public void Post(String Pathname, Map<String, String> data, Callback callback){
        PostSender th_Sender = new PostSender();
        th_Sender.SetConnection(Host + Pathname);
        th_Sender.SetMessage(data);
        th_Sender.SetCallback(callback);
        th_Sender.start();
    }

    public void Get(String Pathname, Map<String, String> data, Callback callback){
        Sender th_Sender = new Sender();
        String address = Host + Pathname;
        if(data != null){
            StringBuilder QueryString = new StringBuilder();

            for(Map.Entry<String, String> entry : data.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                QueryString.append("&")
                        .append(key)
                        .append("=")
                        .append(value);

            }
            QueryString.setCharAt(0, '?');
            address += QueryString.toString();
        }

        th_Sender.SetConnection(address);
        th_Sender.SetCallback(callback);
        th_Sender.start();
    }

    public void sendImage(String Pathname, Map<String, String> data, String filepath, Callback callback){
        ImageSender th_Sender = new ImageSender();
        th_Sender.SetConnection(Host + Pathname);
        th_Sender.SetMessage(data, filepath);
        th_Sender.SetCallback(callback);
        th_Sender.start();
    }

    public void sendImage(String Pathname, Map<String, String> data, Bitmap bitmap, Callback callback){
        ImageSender th_Sender = new ImageSender();
        th_Sender.SetConnection(Host + Pathname);
        th_Sender.SetMessage(data, bitmap);
        th_Sender.SetCallback(callback);
        th_Sender.start();
    }


    private class Sender extends Thread {
        private int timeout = 10000;
        private String _url = "";
        private final String reqtype;
        String contenttype;
        Map<String, String> map = null;
        private Callback cb = null;


        Sender(){
            contenttype = "application/x-www-form-urlencoded; charset=UTF-8";
            reqtype = "GET";
        }
        Sender(String contenttype, String Requesttype){
            this.contenttype = contenttype;
            this.reqtype = Requesttype;
        }

        //연결할 url 설정
        void SetConnection(String url){ _url = url; }

        //데이터를 받아서 처리할 콜백과 UI를 처리할 콜백 세팅
        void SetCallback(Callback callback){
            cb = callback;
        }
        void SetMessage(Map<String, String> msg){
            map = msg;
        }


        private HttpURLConnection openConnection() throws Exception {

            URL url = new URL(_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {

                conn.setConnectTimeout(timeout);
                conn.setRequestMethod(reqtype);
                conn.setRequestProperty("Content-Type", contenttype);
                conn.setRequestProperty("Accept-Charset", "UTF-8");
                conn.setDoInput(true);
                conn.setDoOutput(true);

            }
            else{
                throw new Exception("connection failed");
            }

            return conn;
        }

        protected void sendMessage(HttpURLConnection conn) throws Exception { }

        public void run(){
            StringBuilder Receivedata = new StringBuilder();

            try{
                //연결 세팅하고 만들기
                HttpURLConnection conn = openConnection();

                //메시지 보내기
                sendMessage(conn);


                //메시지 받기
                final int ResponseCode = conn.getResponseCode();

                BufferedReader reader;
                if(ResponseCode == HttpURLConnection.HTTP_OK){
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                }
                else{
                    reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                String line;

                while (true) {
                    line = reader.readLine();
                    if (line == null) break;

                    Receivedata.append(line);
                }


                reader.close();
                conn.disconnect();

                //데이터 주고받기 완료
                //데이터를 주고받을때마다 연결을 햇다 끊엇다 해야하나?

            }
            catch (Exception e){
                e.printStackTrace();
            }

            final Object obj = cb.DataReceived(Receivedata.toString());

            handler.post(new Runnable() {
                @Override
                public void run() {
                    cb.HandlerMethod(obj);
                }
            });

        }
    }

    private class PostSender extends Sender {
        //final public static String JSON = "application/json; charset=UTF-8";

        PostSender(){
            super("application/json; charset=UTF-8", "POST");
        }

        protected void sendMessage(HttpURLConnection conn) throws Exception{
            if(map == null) throw new Exception("data is null");

            JSONObject jsonObject = new JSONObject();
            for( Map.Entry<String, String> entry : map.entrySet() ) {
                String key = entry.getKey();
                String value = entry.getValue();
                jsonObject.put(key, value);
            }

            String jsonstr = jsonObject.toString();

            byte[] outputBytes = jsonstr.getBytes(StandardCharsets.UTF_8);
            OutputStream os = conn.getOutputStream();
            os.write(outputBytes);
            os.flush();
            os.close();

        }

    }

    private class ImageSender extends Sender {
        final private String lineEnd = "\r\n";
        final private String twoHyphens = "--";
        final private String boundary;

        private String filepath = null;
        private Bitmap bitmap = null;

        ImageSender() {

            super("multipart/form-data; charset=utf-8; boundary=", "POST");

            boundary = makeboundary();
            contenttype += boundary;

        }

        String makeboundary(){
            StringBuilder builder = new StringBuilder("HC");
            Random rnd = new Random();
            for (int i = 0; i < 10; i++) {
                int rIndex = rnd.nextInt(3);
                switch (rIndex) {
                    case 0:
                        // a-z
                        builder.append((char) (rnd.nextInt(26)) + 97);
                        break;
                    case 1:
                        // A-Z
                        builder.append((char) (rnd.nextInt(26)) + 65);
                        break;
                    case 2:
                        // 0-9
                        builder.append((rnd.nextInt(10)));
                        break;
                }
            }

            return builder.toString();
        }


        void SetMessage(Map<String, String> map, String filepath) {
            super.SetMessage(map);
            this.filepath = filepath;
        }

        void SetMessage(Map<String, String> map, Bitmap bitmap) {
            super.SetMessage(map);
            this.bitmap = bitmap;
        }


        @Override
        protected void sendMessage(HttpURLConnection conn) throws Exception {

            DataOutputStream ds = new DataOutputStream(conn.getOutputStream());
            String msgbegin = lineEnd + twoHyphens + boundary + lineEnd;


            //일반 데이터 전송
            if (map != null){
                StringBuilder builder = new StringBuilder();
                for( Map.Entry<String, String> entry : map.entrySet() ) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    //이 3개가 한묶음 - { 키 : 문자열 }한가지를 보낼때
                    builder.append(msgbegin)
                            .append("Content-Disposition: form-data; name=\"")
                            .append(key)
                            .append("\"")
                            .append(lineEnd)
                            .append(lineEnd)
                            .append(URLEncoder.encode(value, "utf-8"));

                }

                ds.writeBytes(builder.toString());
            }


            //이미지 전송
            if(bitmap != null){
                ds.writeBytes(msgbegin);
                ds.writeBytes("Content-Disposition: form-data; name=\"userimage\"; filename=\"imagefile\"");
                ds.writeBytes(lineEnd + lineEnd);

                ByteArrayOutputStream os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                ds.write(os.toByteArray());
                os.close();

            }
            else if(filepath != null) {

                byte[] buffer;
                int maxBufferSize = 5 * 1024 * 1024;

                ds.writeBytes(msgbegin);
                ds.writeBytes("Content-Disposition: form-data; name=\"userimage\"; filename=\"imagefile\"");

                //이 문장은 써야하는지 모르겟
                //ds.writeBytes(lineEnd + "Content-Type: application/octet-stream");

                ds.writeBytes(lineEnd + lineEnd);

                FileInputStream fs = new FileInputStream(filepath);
                buffer = new byte[maxBufferSize];
                int length;
                while((length= fs.read(buffer)) != -1){
                    ds.write(buffer, 0, length);
                }
                fs.close();
            }

            //여기가 데이터 전송 끝이다 선언
            ds.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);

            ds.flush();
            ds.close();

        }
    }

}
