package com.example.hanchat.module;

import android.app.ProgressDialog;
import android.content.Intent;

import com.example.hanchat.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
// #### : 데이터 포맷
// android.accounts.Account = getSelectedAccount()
//선택한 Google 계정을 반환하거나 null없음을 반환합니다
// String = getSelectedAccountName()
//        선택한 Google 계정 이름 (이메일 주소)를 반환, 예를 들어 "johndoe@gmail.com", 또는 null없음을 위해
public class CalendarAPIManager implements EasyPermissions.PermissionCallbacks {
    private String TAG = "@@@@ ";

    private AppCompatActivity activity;
    private Fragment fragment;

    // Google Calendar API 객체
    private com.google.api.services.calendar.Calendar mService = null;
    //Google Calendar API 호출, AsyncTask 재사용
    private int mID = 0;

    private GoogleAccountCredential mCredential;
    private ProgressDialog mProgress;

    private static final int REQUEST_ACCOUNT_PICKER = 1000;
    private static final int REQUEST_AUTHORIZATION = 1001;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String PREF_ACCOUNT_NAME = "accountName";
    // Google Calendar API 읽고쓰기 모두가능 (Not CALENDAR_READONLY)
    private static final String[] SCOPES = {CalendarScopes.CALENDAR};

    public CalendarAPIManager(AppCompatActivity activity){
        this.activity= activity;

        // 계정 연동 회원 가입
        // mID = 1;

        // 일정 추가
        // mID = 2;

        // 일정 조회
        //mID = 3;

        mProgress = new ProgressDialog(activity);
        mProgress.setMessage("Google Calendar API 호출 중입니다.");

        // Google Calendar API 사용자 인증 초기화
        mCredential = GoogleAccountCredential.usingOAuth2(
                activity.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    public String getUserName() {
        return mCredential.getSelectedAccountName();
    }

    public String getUserID() {
        return mCredential.getSelectedAccountName();
    }

    // 비동기적으로 Google Calendar API 호출
    private class MakeRequestTask extends AsyncTask<Void, Void, String> {
        private Exception mLastError = null;
        private AppCompatActivity activity;
        List<String> eventStrings = new ArrayList<String>();

        private MakeRequestTask(AppCompatActivity activity, GoogleAccountCredential credential) {
            this.activity = activity;

            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            mService = new com.google.api.services.calendar.Calendar
                    .Builder(transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
        }

        // doInBackground()의 준비작업
        @Override
        protected void onPreExecute() {
            mProgress.show();
            Toast.makeText(activity, "데이터 가져오는 중...", Toast.LENGTH_LONG).show();
        }

        // 백그라운드에서 Google Calendar API 호출 처리 실행
        @Override
        protected String doInBackground(Void... params) {
            try {
                if (mID == 1) {
                    return createCalendar();
                } else if (mID == 2) {
                    return addEvent();
                } else if (mID == 3) {
                    return getEvent();
                }
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
            return null;
        }

        // CalendarTitle 이름의 캘린더에서 10개의 이벤트를 가져와 리턴
        private String getEvent() throws IOException {
            DateTime now = new DateTime(System.currentTimeMillis());

            String calendarID = getCalendarID("Hanchat");
            if (calendarID == null) {

                return "캘린더를 먼저 생성하세요.";
            }


            Events events = mService.events().list(calendarID)//"primary")
                    .setMaxResults(10)
                    //.setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();


            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {

                    // 모든 이벤트가 시작 시간을 갖고 있지는 않음
                    // 그런 경우 시작 날짜만 사용
                    start = event.getStart().getDate();
                }

                eventStrings.add(String.format("%s \n (%s)", event.getSummary(), start));
            }


            return eventStrings.size() + "개의 데이터를 가져왔습니다.";
        }

        // 선택되어 있는 Google 계정에 새 캘린더를 추가한다.
        private String createCalendar() throws IOException {

            String ids = getCalendarID("Hanchat");
            if (ids != null)
                return "이미 캘린더가 생성되어 있습니다. ";

            // 새로운 캘린더 생성
            com.google.api.services.calendar.model.Calendar calendar = new Calendar();

            // 캘린더의 제목 설정
            calendar.setSummary("Hanchat");


            // 캘린더의 시간대 설정
            calendar.setTimeZone("Asia/Seoul");

            // 구글 캘린더에 새로 만든 캘린더를 추가
            Calendar createdCalendar = mService.calendars().insert(calendar).execute();

            // 추가한 캘린더의 ID를 가져옴.
            String calendarId = createdCalendar.getId();


            // 구글 캘린더의 캘린더 목록에서 새로 만든 캘린더를 검색
            CalendarListEntry calendarListEntry = mService.calendarList().get(calendarId).execute();

            // 캘린더의 배경색을 파란색으로 표시  RGB
            calendarListEntry.setBackgroundColor("#0000ff");

            // 변경한 내용을 구글 캘린더에 반영
            CalendarListEntry updatedCalendarListEntry =
                    mService.calendarList()
                            .update(calendarListEntry.getId(), calendarListEntry)
                            .setColorRgbFormat(true)
                            .execute();

            // 새로 추가한 캘린더의 ID를 리턴
            return "캘린더가 생성되었습니다.";
        }


        @Override
        protected void onPostExecute(String output) {
            mProgress.hide();
            Toast.makeText(activity, output, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();

            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    activity.startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            REQUEST_AUTHORIZATION);
                } else {
                    Toast.makeText(activity, "MakeRequestTask 에러 발생: " + mLastError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, "요청 취소됨", Toast.LENGTH_SHORT).show();
            }
        }

        // 일정 추가
        private String addEvent() {
            String calendarID = getCalendarID("Hanchat");

            if (calendarID == null) {
                return "캘린더를 먼저 생성하세요.";
            }

            Event event = new Event()
                    .setSummary("구글 캘린더 테스트")                                  //#### 캘린더의 이름
                    .setLocation("서울시")                                             //#### 캘린더의 위치
                    .setDescription("캘린더에 이벤트 추가하는 것을 테스트합니다.");    //#### 캘린더의 메모


            java.util.Calendar calander;

            calander = java.util.Calendar.getInstance();
            SimpleDateFormat simpledateformat;
            simpledateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+09:00", Locale.KOREA);    //#### 캘린더의 날짜 시간데이터
            String datetime = simpledateformat.format(calander.getTime());

            DateTime startDateTime = new DateTime(datetime);
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone("Asia/Seoul");
            event.setStart(start);

            Log.d(TAG, datetime);


            DateTime endDateTime = new DateTime(datetime);
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone("Asia/Seoul");
            event.setEnd(end);

            //String[] recurrence = new String[]{"RRULE:FREQ=DAILY;COUNT=2"};
            //event.setRecurrence(Arrays.asList(recurrence));


            try {
                event = mService.events().insert(calendarID, event).execute();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Exception", "Exception : " + e.toString());
            }
            System.out.printf("Event created: %s\n", event.getHtmlLink());
            Log.e("Event", "created : " + event.getHtmlLink());
            String eventStrings = "created : " + event.getHtmlLink();
            return eventStrings;
        }
    }

    // 구글 플레이 서비스, 구글 계정, 인터넷 사용가능 시 Google Calendar API 비동기 호출
    private void getResultsFromApi() {
        Log.d(TAG, "isGooglePlayServicesAvailable() : " + isGooglePlayServicesAvailable());
        Log.d(TAG, "mCredential.getSelectedAccountName() is valid : " + !(mCredential.getSelectedAccountName() == null));
        Log.d(TAG, "isDeviceOnline() : " + isDeviceOnline());

        // 1. Google Play Services를 사용할 수 없는 경우
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        }
        // 2. 유효한 Google 계정이 선택되어 있지 않은 경우
        else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        }
        // 3. 인터넷을 사용할 수 없는 경우
        else if (!isDeviceOnline()) {
            Toast.makeText(activity, activity.getString(R.string.internet_1), Toast.LENGTH_SHORT).show();
        }
        // 위에 모두 해당하지 않을 경우에 Google Calendar API 호출
        else {
            new MakeRequestTask(activity, mCredential).execute();
        }

    }

    // Google Play Services가 설치 확인
    private boolean isGooglePlayServicesAvailable() {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();

        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    // 구글 계정 선택 다이얼로그
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        // GET_ACCOUNTS 권한을 가지고 있다면

        if (EasyPermissions.hasPermissions(activity, Manifest.permission.GET_ACCOUNTS)) {
            // SharedPreferences에서 저장된 Google 계정 이름을 보여줌
            String accountName = activity.getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);

            if (accountName != null) {

                Log.d(TAG, "chooseAccount() : accountName != null");
                // 선택된 구글 계정으로 설정
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            }
            else {
                // 사용자가 구글 계정을 선택할 수 있는 다이얼로그를 보여줌
                activity.startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);

            }
        }
        // GET_ACCOUNTS 권한을 가지고 있지 않다면
        else {
            // 사용자에게 GET_ACCOUNTS 권한을 요구하는 다이얼로그를 보여줌 (주소록 권한 요청함)
            Log.d(TAG, "EasyPermissions.hasPermissions(activity, Manifest.permission.GET_ACCOUNTS)");
            EasyPermissions.requestPermissions(
                     activity,
                    activity.getString(R.string.permission_3),
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);

        }
    }

    // 안드로이드 디바이스가 인터넷 연결되어 있는지 확인
    // 연결되어 있다면 True 리턴, 아니면 False 리턴
    private boolean isDeviceOnline() {

        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    // Google Play Services 최신버전 업데이트 유도
    private void acquireGooglePlayServices() {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(activity);

        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {

            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    // 서비스 업데이트 다이얼로그
    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();

        Dialog dialog = apiAvailability.getErrorDialog(
                activity,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES
        );
        dialog.show();
    }


    //구글 플레이 서비스 업데이트 다이얼로그, 구글 계정 선택 다이얼로그, 인증 다이얼로그에서 되돌아올때 호출
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case REQUEST_GOOGLE_PLAY_SERVICES:

                if (resultCode != RESULT_OK) {
                    Toast.makeText(activity,  "앱을 실행시키려면 구글 플레이 서비스가 필요합니다."
                            + "구글 플레이 서비스를 설치 후 다시 실행하세요.", Toast.LENGTH_SHORT).show();
                } else {

                    getResultsFromApi();
                }
                break;


            case REQUEST_ACCOUNT_PICKER:
                Log.d(TAG,"REQUEST_ACCOUNT_PICKER : "+ REQUEST_ACCOUNT_PICKER);
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    Log.d(TAG,"data.getExtras() : "+ data.getExtras() );
                    Log.d(TAG, "accountName : "+ data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
                    if (accountName != null) {
                        SharedPreferences settings = activity.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;


            case REQUEST_AUTHORIZATION:

                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG,"onRequestPermissionsResult");
        activity.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
// EasyPermissions 라이브러리를 사용하여 요청한 권한을 사용자가 승인한 경우 호출된다.
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG,"onPermissionsGranted");
        if (requestCode == REQUEST_GOOGLE_PLAY_SERVICES) {
            chooseAccount();
        }
    }

    // EasyPermissions 라이브러리를 사용하여 요청한 권한을 사용자가 거부한 경우 호출된다.
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG,"onPermissionsDenied");
    }


    // 캘린더 이름에 대응하는 캘린더 ID를 리턴
    private String getCalendarID(String calendarTitle) {
        String id = null;

        // Iterate through entries in calendar list
        String pageToken = null;
        do {
            CalendarList calendarList = null;
            try {
                calendarList = mService.calendarList().list().setPageToken(pageToken).execute();
            }
            catch (UserRecoverableAuthIOException e) {
                activity.startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            // 일정 리스트
            List<CalendarListEntry> items = calendarList.getItems();

            for (CalendarListEntry c : items) {
                if (c.getSummary().equals(calendarTitle)) {
                    // 일정 아이디
                    id = c.getId();
                    Log.d(TAG, id+" "+c.getSummary());
                }
            }
            pageToken = (calendarList).getNextPageToken();
        } while (pageToken != null);

        return id;
    }

    public void setmIDButton(int m){
        mID = m;
        getResultsFromApi();
    }

}
