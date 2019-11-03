package com.example.hanchat.module.connecter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import androidx.annotation.StringRes;

import com.example.hanchat.module.ApplicationSharedRepository;

import org.json.JSONException;
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
import java.util.Iterator;
import java.util.Map;
import java.util.Random;



/*완료*/
//8.16  경고 수정

//먼저 맨위의 패키지 이름부터 바꾸고
//AndroidManifest.xml 파일에서 <uses-permission android:name="android.permission.INTERNET" /> 추가

/*
    인터넷 연결해주는 커넥터
 */


public class HttpConnecter {

    //통신 완료 후에 실행될 콜백 함수 정의를 위한 인터페이스
    public interface ResponseRecivedCallback {
        String KEY_RESULT = "result";

        //서버에서 받은 데이터를 처리할 콜백 함수 (쓰레드로 실행됨)
        //UIChange로 전달할 데이터를 return
        void DataReceived(JSONObject data) throws JSONException;


        //데이터를 모두 처리하고 나서 UI 등을 처리할 콜백 함수 (메인 쓰레드에서 실행됨)
        //UI 등과 같이 메인 쓰레드가 아니면 실행할 수 없는 부분을 처리
        //위의 함수에서 Object를 받아서 처리하는 함수를 만드세요
        void DataInvoked(JSONObject data) throws JSONException;

        //예외 (오류)가 났을시의 처리 - 메인 스레드에서 실행
        void ConnectionFailed(Exception e);
    }

    private static Map<String, HttpConnecter> instanceMap = new HashMap<>();

//    private static Context appContext = null;
    private Handler handler;
    private String Host;

    //생성자로 ip주소, 포트번호를 전달받음
    private HttpConnecter(String Host){
        this.Host = Host;
        this.handler = new Handler();
    }

    //ip주소, 포트번호를 전달받음
    public static HttpConnecter getinstance(String Hostip, int Port){
        return getinstance(Hostip, String.valueOf(Port));
    }

    //ip주소, 포트번호를 전달받음
    public static HttpConnecter getinstance(String Hostip, String Port){
        String host = "http://" + Hostip + ":" + Port;
        if(instanceMap.containsKey(host)){
            return instanceMap.get(host);
        }
        else{
            HttpConnecter newinstance = new HttpConnecter(host);
            instanceMap.put(host, newinstance);
            return newinstance;
        }
    }


    //ip주소, 포트번호를 전달받음
    public static HttpConnecter getinstance(@StringRes int Hostip, @StringRes int Port){
        //if(appContext == null)
        Context appContext = ApplicationSharedRepository.getAppContext();
        return getinstance(appContext.getString(Hostip), appContext.getString(Port));

    }

    //Post 형식으로 전달할때
    //Pathname : 주소에서 /로 시작해서 ? 전까지의 부분 (ex : "/chatbot_data"), Map과 통신 이후에 수행할 콜백을 받음
    //콜백은 위의 인터페이스 활용
    public void Post(@StringRes int Pathname, JSONObject data, ResponseRecivedCallback responseRecivedCallback){
        Context appContext = ApplicationSharedRepository.getAppContext();
        this.Post(appContext.getString(Pathname), data, responseRecivedCallback);
    }
    public void Post(String Pathname, JSONObject data, ResponseRecivedCallback responseRecivedCallback) {
        PostSender th_Sender = new PostSender();
        th_Sender.SetMessage(data);
        senderSetting(th_Sender, Pathname, responseRecivedCallback);
    }

    public void Get(String Pathname, Map<String, String> data, ResponseRecivedCallback responseRecivedCallback){
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
        th_Sender.SetCallback(responseRecivedCallback);
        th_Sender.start();
    }


    public void sendImage(String Pathname, JSONObject data, String filepath, ResponseRecivedCallback responseRecivedCallback) {
        ImageSender th_Sender = new ImageSender();
        th_Sender.SetMessage(data, filepath);
        senderSetting(th_Sender, Pathname, responseRecivedCallback);
    }

    public void sendImage(@StringRes int Pathname, JSONObject data, Bitmap bitmap, ResponseRecivedCallback responseRecivedCallback){
        Context appContext = ApplicationSharedRepository.getAppContext();
        this.sendImage(appContext.getString(Pathname), data, bitmap, responseRecivedCallback);
    }
    public void sendImage(String Pathname, JSONObject data, Bitmap bitmap, ResponseRecivedCallback responseRecivedCallback){
        ImageSender th_Sender = new ImageSender();
        th_Sender.SetMessage(data, bitmap);
        senderSetting(th_Sender, Pathname, responseRecivedCallback);
    }

    private void senderSetting(Sender sender, String Pathname, ResponseRecivedCallback responseRecivedCallback){
        sender.SetConnection(Host + Pathname);

        sender.SetCallback(responseRecivedCallback);
        sender.start();
    }


    private class Sender extends Thread {
        private int timeout = 10000;
        private String _url = "";
        private final String reqtype;
        String contenttype;
        JSONObject json = null;
        private ResponseRecivedCallback cb = null;


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

        //응답을 받은뒤의 콜백함수
        void SetCallback(ResponseRecivedCallback responseRecivedCallback){
            cb = responseRecivedCallback;
        }

        //메세지 설정
        void SetMessage(JSONObject json){
            this.json = json;
        }

        //연결 설정
        private HttpURLConnection openConnection() throws Exception{

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

                BufferedReader reader;//= new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if(ResponseCode == HttpURLConnection.HTTP_OK){
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                }
                else{

                    throw new Exception("error");
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
            catch (final Exception e){
                e.printStackTrace();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        cb.ConnectionFailed(e);
                    }
                });
                return;
            }

            try{
                final JSONObject jsondata = new JSONObject(Receivedata.toString());
                cb.DataReceived(jsondata);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            cb.DataInvoked(jsondata);
                        }
                        catch (final Exception e){
                            e.printStackTrace();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    cb.ConnectionFailed(e);
                                }
                            });
                        }
                    }
                });

            }catch (final Exception e){
                e.printStackTrace();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        cb.ConnectionFailed(e);
                    }
                });

                return;
            }



        }
    }

    private class PostSender extends Sender {
        //final public static String JSON = "application/json; charset=UTF-8";

        PostSender(){
            super("application/json; charset=UTF-8", "POST");
        }

        protected void sendMessage(HttpURLConnection conn) throws Exception{
            if(json == null) return;

            String jsonstr = json.toString();

            byte[] outputBytes = jsonstr.getBytes(StandardCharsets.UTF_8);
            OutputStream os = conn.getOutputStream();
            os.write(outputBytes);
            os.flush();
            os.close();

        }

    }

    private class ImageSender extends Sender {
        final private String lineEnd;
        final private String twoHyphens;
        final private String boundary;

        private String filepath = null;
        private Bitmap bitmap = null;

        ImageSender() {

            super("multipart/form-data; charset=utf-8; boundary=", "POST");

            lineEnd = "\r\n";
            twoHyphens = "--";
            boundary = makeBoundary();
            contenttype += boundary;
        }

        String makeBoundary(){
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


        void SetMessage(JSONObject json, String filepath) {
            super.SetMessage(json);
            this.filepath = filepath;
        }

        void SetMessage(JSONObject json, Bitmap bitmap) {
            super.SetMessage(json);
            this.bitmap = bitmap;
        }


        @Override
        protected void sendMessage(HttpURLConnection conn) throws Exception {

            DataOutputStream ds = new DataOutputStream(conn.getOutputStream());
            String msgbegin = lineEnd + twoHyphens + boundary + lineEnd;


            //일반 데이터 전송
            if (json != null){
                StringBuilder builder = new StringBuilder();
                Iterator<String> keys = json.keys();
                while(keys.hasNext()){
                    String key = keys.next();
                    String value = json.getString(key);

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
