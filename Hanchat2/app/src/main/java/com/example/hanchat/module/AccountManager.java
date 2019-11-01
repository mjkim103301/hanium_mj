package com.example.hanchat.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hanchat.R;
import com.example.hanchat.module.connecter.AuthHttpConnecter;
import com.example.hanchat.module.connecter.HttpConnecter;

import org.json.JSONException;
import org.json.JSONObject;

/*
    계정과 관련된 모든 일을 처리하는 매니저

 */
public class AccountManager {

    //AccountManager 전용 콜백 인터페이스
    public interface AccountCallback{
        //일이 끝나면 알림  -  result : 성공여부
        void TaskFinished(Boolean result);

        //연결에 실패하면 알림  -  인터넷 연결 자체가 실패함
        void ConnectionFailed();
    }


    private static AccountManager instance = null;
    private final static String prefId = "AccountPref";
    private HttpConnecter httpConnecter = HttpConnecter.getinstance(R.string.server_ip, R.string.server_port);

    Context appContext;
    SharedPreferences pref;
    private Long pid = 0L;
    private String loginToken = "";

    public Long getPid(){
        return pid;
    }
    public String getLoginToken() {
        return loginToken;
    }

    private AccountManager(){
        appContext = ApplicationSharedRepository.getAppContext();
        pref = appContext.getSharedPreferences(prefId, appContext.MODE_PRIVATE);
    }

    public static AccountManager getInstance(){
        if(instance == null)
            instance = new AccountManager();
        return instance;
    }

    /*
        자동 로그인
        가장 먼저 pref 확인
        id가 저장되어 있으면 id와 password로 Login 실행
        id가 없고 pid가 저장되어 있으면 pid와 저번 logintoken으로 Login 실행
        둘다 없으면 새로운 유저 만들기
     */
    public void autoLogin(AccountCallback callback){
        if(pref.contains("id")){
            String id = pref.getString("id", null);
            String pwd = pref.getString("password", null);

            login(id, pwd, callback);
        }
        else if(pref.contains("pid")){
            pid = pref.getLong("pid", 0L);
            String loginToken = pref.getString("logintoken", "");
            login(pid, loginToken, callback);
        }
        else{
            createUser(callback);
        }
    }

    /*
        id와 암호화된 password
        또는 pid와 logintoken  으로 로그인 시도
        로그인이 성공하면 pref에 id와 암호화된 password 저장
        또는 pid와 logintoken 저장
     */
    private void login(final String id, final String encryptedPassword, final AccountCallback callback){
        JSONObject data = new JSONObject();
        try{
            data.put("id", id);
            data.put("password", encryptedPassword);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        httpConnecter.Post(R.string.route_login, data, new HttpConnecter.ResponseRecivedCallback() {
            @Override
            public void DataReceived(JSONObject data) throws JSONException{
                if(data.getBoolean(KEY_RESULT)){
                    pid = data.getLong("pid");
                    loginToken = data.getString("logintoken");

                    pref.edit()
                            .putString("id", id)
                            .putString("password", encryptedPassword)
                            .apply();
                }
            }

            @Override
            public void DataInvoked(JSONObject data) throws JSONException {
                callback.TaskFinished(data.getBoolean(KEY_RESULT));
            }

            @Override
            public void ConnectionFailed(Exception e) {
                callback.ConnectionFailed();
            }
        });
    }

    private void login(final long pid, final String loginToken, final AccountCallback callback){
        JSONObject data = new JSONObject();
        try{
            data.put("pid", pid);
            data.put("logintoken", loginToken);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        httpConnecter.Post(R.string.route_login, data, new HttpConnecter.ResponseRecivedCallback() {
            @Override
            public void DataReceived(JSONObject data) throws JSONException {
                if(data.getBoolean(KEY_RESULT)){
                    AccountManager.this.pid = data.getLong("pid");
                    AccountManager.this.loginToken = data.getString("logintoken");
                    pref.edit().putLong("pid", pid).putString("logintoken", loginToken).apply();
                }
            }

            @Override
            public void DataInvoked(JSONObject data) throws JSONException {
                callback.TaskFinished(data.getBoolean(KEY_RESULT));
            }

            @Override
            public void ConnectionFailed(Exception e) {
                callback.ConnectionFailed();
            }
        });

    }

    /*
        새로운 로그인 시도
        서버에게 id를 보내고 salts[5]를 받아옴
        salts[5]를 이용해 password를 암호화하고
        id와 암호화된 password로 Login 함수 실행
     */
    public void newLogin(final String id, final String password, final AccountCallback callback){

        GetSalts(id, new HttpConnecter.ResponseRecivedCallback() {
            String encryptedPassword = "";
            @Override
            public void DataReceived(JSONObject data) throws JSONException {
                if(data.getBoolean(KEY_RESULT)){
                    String[] salts = new String[5];
                    for (int i = 0; i < 5; i++) {
                        salts[i] = data.getString("salt" + i);
                    }
                    encryptedPassword = PasswordEncryption(password, salts);
                }
            }

            @Override
            public void DataInvoked(JSONObject data) throws JSONException {
                if(data.getBoolean(KEY_RESULT)){
                    login(id, encryptedPassword, callback);
                }
                else{
                    callback.TaskFinished(false);
                }
            }

            @Override
            public void ConnectionFailed(Exception e) {
                callback.ConnectionFailed();
            }
        });
    }

    /*
        유저 새로 생성
        서버에게 알리고 pid와 logintoken을 받아옴
        -> 바로 로그인 완료
        pref에 pid와 logintoken 저장
     */
    public void createUser(final AccountCallback callback){
        httpConnecter.Post(R.string.route_createUser, null, new HttpConnecter.ResponseRecivedCallback() {
            @Override
            public void DataReceived(JSONObject data) throws JSONException {
                if(data.getBoolean(KEY_RESULT)){
                    pid = data.getLong("pid");
                    loginToken = data.getString("logintoken");
                    pref.edit().putLong("pid", pid).putString("logintoken", loginToken).apply();
                }
            }

            @Override
            public void DataInvoked(JSONObject data) throws JSONException {
                callback.TaskFinished(data.getBoolean(KEY_RESULT));
            }

            @Override
            public void ConnectionFailed(Exception e) {
                callback.ConnectionFailed();
            }
        });
    }

    /*
        회원가입
        (이미 pid와 logintoken을 가지고 있어야 함)
        현재 pid를 기반으로 id와 password를 등록
        무작위 문자열을 생성해 salts[5]를 먼저 생성하고
        그 salts[5]를 이용해 password를 암호화
        AuthHttpConnecter를 이용해 id와 암호화된 password, salts[5]를 서버에 보내 등록
     */
    public void signUp(final String id, String password, final AccountCallback callback){
        String[] salts = new String[5];
        for (int i = 0; i < 5; i++) {
            salts[i] = Tools.getRandomString(32);
        }
        final String encryptedPassword = PasswordEncryption(password, salts);


        JSONObject data = new JSONObject();
        try{
            data.put("id", id);
            data.put("password", encryptedPassword);
            for(int i = 0 ;i < 5; i++){
                data.put("salt" + i , salts[i]);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


        new AuthHttpConnecter(httpConnecter)
                .Post(null, data, new HttpConnecter.ResponseRecivedCallback() {
            @Override
            public void DataReceived(JSONObject data) throws JSONException {
                if(data.getBoolean(KEY_RESULT)){
                    pref.edit()
                            .putString("id", data.getString("id"))
                            .putString("password", encryptedPassword)
                            .apply();
                }
            }

            @Override
            public void DataInvoked(JSONObject data) throws JSONException {
                callback.TaskFinished(data.getBoolean(KEY_RESULT));
            }

            @Override
            public void ConnectionFailed(Exception e) {
                callback.ConnectionFailed();
            }
        });

    }

    /*
        id를 이용해 salts[5]를 받아와 이번 password를 암호화하고
        pref에 저장된 password와 같은지 확인
     */
    public void checkPassword(String id, final String password, final AccountCallback callback){
        GetSalts(id, new HttpConnecter.ResponseRecivedCallback() {
            String encryptedPassword = "";
            @Override
            public void DataReceived(JSONObject data) throws JSONException {
                if (data.getBoolean(KEY_RESULT)){
                    String[] salts = new String[5];
                    for (int i = 0; i < 5; i++) {
                        salts[i] = data.getString("salt" + i);
                    }
                    encryptedPassword = PasswordEncryption(password, salts);
                }
            }

            @Override
            public void DataInvoked(JSONObject data) throws JSONException {
                callback.TaskFinished(pref.getString("password", "") == encryptedPassword);
            }

            @Override
            public void ConnectionFailed(Exception e) {
                callback.ConnectionFailed();
            }
        });
    }

    private void GetSalts(String id, final HttpConnecter.ResponseRecivedCallback callback){
        JSONObject data = new JSONObject();
        try{
            data.put("id", id);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        httpConnecter.Post(R.string.route_getSalts, data, callback);
    }

    //비밀번호 암호화
    private String PasswordEncryption(String password, String[] salts){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                password = Tools.getEncryptedString(password, salts[i]);
            }
        }
        return password;
    }
}
