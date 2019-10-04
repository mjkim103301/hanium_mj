package com.example.hanchat.module;

public class CalendarAPIManager {
}


/*
package com.example.hanchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

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
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class CalendarActivity extends NavActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , EasyPermissions.PermissionCallbacks
{
    Button bt_go_chat;
    Intent intent;
    Intent intent_profile;
    String TAG = "@@@@ ";

    // Google Calendar API 객체
    private com.google.api.services.calendar.Calendar mService = null;
    //Google Calendar API 호출, AsyncTask 재사용
    private int mID = 0;

    GoogleAccountCredential mCredential;
    private TextView mStatusText;
    private TextView mResultText;
    private Button mGetEventButton;
    private Button mAddEventButton;
    private Button mAddCalendarButton;
    ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        intent = new Intent(com.example.hanchat.CalendarActivity.this, MainActivity.class);
        intent_profile = new Intent(com.example.hanchat.CalendarActivity.this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        // 우측 상단 버튼 (캘린더 화면으로 이동)
        bt_go_chat = findViewById(R.id.bt_go_chat);

        NavSetting();
        ButtonSetting();

        //google

        mAddCalendarButton = (Button) findViewById(R.id.button_main_add_calendar);
        mAddEventButton = (Button) findViewById(R.id.button_main_add_event);
        mGetEventButton = (Button) findViewById(R.id.button_main_get_event);

        mStatusText = (TextView) findViewById(R.id.textview_main_status);
        mResultText = (TextView) findViewById(R.id.textview_main_result);

        mAddCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddCalendarButton.setEnabled(false);
                mStatusText.setText("");
                mID = 1;           //캘린더 생성
                getResultsFromApi();
                mAddCalendarButton.setEnabled(true);
            }
        });


        mAddEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddEventButton.setEnabled(false);
                mStatusText.setText("");
                mID = 2;        //이벤트 생성
                getResultsFromApi();
                mAddEventButton.setEnabled(true);
            }
        });


        mGetEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetEventButton.setEnabled(false);
                mStatusText.setText("");
                mID = 3;        //이벤트 가져오기
                getResultsFromApi();
                mGetEventButton.setEnabled(true);
            }
        });


        // Google Calendar API의 호출 결과를 표시하는 TextView를 준비
        mResultText.setVerticalScrollBarEnabled(true);
        mResultText.setMovementMethod(new ScrollingMovementMethod());

        mStatusText.setVerticalScrollBarEnabled(true);
        mStatusText.setMovementMethod(new ScrollingMovementMethod());
        mStatusText.setText("버튼을 눌러 테스트를 진행하세요.");


        // Google Calendar API 호출중에 표시되는 ProgressDialog
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Google Calendar API 호출 중입니다.");


        // Google Calendar API 사용하기 위해 필요한 인증 초기화( 자격 증명 credentials, 서비스 객체 )
        // OAuth 2.0를 사용하여 구글 계정 선택 및 인증하기 위한 준비
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    //버튼 세팅들은 여기에
    private void ButtonSetting(){
        // 네비게이션 헤더 클릭시 프로필로 이동
//        headerview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(intent_profile);
//            }
//        });

        bt_go_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 동일한 activity가 stack에 연속적으로 쌓였을때 activity를 재사용 | 새 태스크를 만드는 플래그
                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
        });

    }



    //     다음 사전 조건을 모두 만족해야 Google Calendar API를 사용 가능
//     1. Google Play Services 설치                         isGooglePlayServicesAvailable()
//     2. 유효한 구글 계정 선택                             mCredential.getSelectedAccountName() == null
//     3. 안드로이드 디바이스에서 인터넷 사용 가능          isDeviceOnline()
    private void getResultsFromApi() {

        Log.d(TAG, "!sGooglePlayServicesAvailable() : "+ !isGooglePlayServicesAvailable());
        Log.d(TAG, "mCredential.getSelectedAccountName() == null : "+ (mCredential.getSelectedAccountName() == null));
        Log.d(TAG, "!isDeviceOnline() : "+ !isDeviceOnline());


        // Google Play Services를 사용할 수 없는 경우
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        }
        // 유효한 Google 계정이 선택되어 있지 않은 경우
        else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        }
        // 인터넷을 사용할 수 없는 경우
        else if (!isDeviceOnline()) {
            mStatusText.setText("인터넷 연결을 확인하세요.");
        }
        // 위에 모두 해당하지 않을 경우에 Google Calendar API 호출
        else {
            new com.example.hanchat.CalendarActivity.MakeRequestTask(this, mCredential).execute();
        }

    }

    // 안드로이드 디바이스에 최신 버전의 Google Play Services가 설치되어 있는지 확인
    private boolean isGooglePlayServicesAvailable() {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();

        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    // 구글 계정 선택 다이얼로그
    //Google Calendar API의 자격 증명(Credential) 에 사용할 구글 계정을 설정한다.
    // 전에 사용자가 구글 계정을 선택한 적이 없다면 다이얼로그에서 사용자를 선택하도록 한다.
    // GET_ACCOUNTS 퍼미션이 필요하다.
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {

        // GET_ACCOUNTS 권한을 가지고 있다면
        if (EasyPermissions.hasPermissions(this, Manifest.permission.GET_ACCOUNTS)) {


            // SharedPreferences에서 저장된 Google 계정 이름을 가져온다.
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {

                // 선택된 구글 계정 이름으로 설정한다.
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {


                // 사용자가 구글 계정을 선택할 수 있는 다이얼로그를 보여준다.
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }



            // GET_ACCOUNTS 권한을 가지고 있지 않다면
        } else {


            // 사용자에게 GET_ACCOUNTS 권한을 요구하는 다이얼로그를 보여준다.(주소록 권한 요청함)
            EasyPermissions.requestPermissions(
                    (Activity)this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    // 안드로이드 디바이스가 인터넷 연결되어 있는지 확인한다. 연결되어 있다면 True 리턴, 아니면 False 리턴
    private boolean isDeviceOnline() {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    // 비동기적으로 Google Calendar API 호출
    private class MakeRequestTask extends AsyncTask<Void, Void, String> {

        private Exception mLastError = null;
        private com.example.hanchat.CalendarActivity mActivity;
        List<String> eventStrings = new ArrayList<String>();


        private MakeRequestTask(com.example.hanchat.CalendarActivity activity, GoogleAccountCredential credential) {
            mActivity = activity;

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
            mStatusText.setText("데이터 가져오는 중...");
            mResultText.setText("");
        }

        // 백그라운드에서 Google Calendar API 호출 처리 실행
        @Override
        protected String doInBackground(Void... params) {
            try {
                if ( mID == 1) {
                    return createCalendar();
                }
                else if (mID == 2) {
                    return addEvent();
                }
                else if (mID == 3) {
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
            if ( calendarID == null ){

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

            if ( ids != null ){

                return "이미 캘린더가 생성되어 있습니다. ";
            }

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
            mStatusText.setText(output);

            if ( mID == 3 )   mResultText.setText(TextUtils.join("\n\n", eventStrings));
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
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            com.example.hanchat.CalendarActivity.REQUEST_AUTHORIZATION);
                } else {
                    mStatusText.setText("MakeRequestTask The following error occurred:\n" + mLastError.getMessage());
                }
            } else {
                mStatusText.setText("요청 취소됨.");
            }
        }


        private String addEvent() {


            String calendarID = getCalendarID("Hanchat");

            if ( calendarID == null ){

                return "캘린더를 먼저 생성하세요.";

            }

            Event event = new Event()
                    .setSummary("구글 캘린더 테스트")                                  //#### 캘린더의 이름
                    .setLocation("서울시")                                             //#### 캘린더의 위치
                    .setDescription("캘린더에 이벤트 추가하는 것을 테스트합니다.");    //#### 캘린더의 메모


            java.util.Calendar calander;

            calander = java.util.Calendar.getInstance();
            SimpleDateFormat simpledateformat;
            simpledateformat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss+09:00", Locale.KOREA);    //#### 캘린더의 날짜 시간데이터
            String datetime = simpledateformat.format(calander.getTime());

            DateTime startDateTime = new DateTime(datetime);
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone("Asia/Seoul");
            event.setStart(start);

            Log.d( TAG, datetime );


            DateTime endDateTime = new  DateTime(datetime);
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



    // Google Play Services 최신버전 업데이트 유도
    private void acquireGooglePlayServices() {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {

            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    // 서비스 업데이트 다이얼로그
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode
    ) {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();

        Dialog dialog = apiAvailability.getErrorDialog(
                com.example.hanchat.CalendarActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES
        );
        dialog.show();
    }



    //구글 플레이 서비스 업데이트 다이얼로그, 구글 계정 선택 다이얼로그, 인증 다이얼로그에서 되돌아올때 호출된다.
    @Override
    protected void onActivityResult(
            int requestCode,  // onActivityResult가 호출되었을 때 요청 코드로 요청을 구분
            int resultCode,   // 요청에 대한 결과 코드
            Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {

            case REQUEST_GOOGLE_PLAY_SERVICES:

                if (resultCode != RESULT_OK) {

                    mStatusText.setText( " 앱을 실행시키려면 구글 플레이 서비스가 필요합니다."
                            + "구글 플레이 서비스를 설치 후 다시 실행하세요." );
                } else {

                    getResultsFromApi();
                }
                break;


            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
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

    // Android 6.0 (API 23) 이상에서 런타임 권한 요청시 결과를 리턴받음
    @Override
    public void onRequestPermissionsResult(
            int requestCode,  //requestPermissions(android.app.Activity, String, int, String[])에서 전달된 요청 코드
            @NonNull String[] permissions, // 요청한 퍼미션
            @NonNull int[] grantResults    // 퍼미션 처리 결과. PERMISSION_GRANTED 또는 PERMISSION_DENIED
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    // EasyPermissions 라이브러리를 사용하여 요청한 권한을 사용자가 승인한 경우 호출된다.
    @Override
    public void onPermissionsGranted(int requestCode, List<String> requestPermissionList) {
        // 아무일도 하지 않음
    }

    // EasyPermissions 라이브러리를 사용하여 요청한 권한을 사용자가 거부한 경우 호출된다.
    @Override
    public void onPermissionsDenied(int requestCode, List<String> requestPermissionList) {
        // 아무일도 하지 않음
    }


    // 캘린더 이름에 대응하는 캘린더 ID를 리턴
    private String getCalendarID(String calendarTitle){

        String id = null;

        // Iterate through entries in calendar list
        String pageToken = null;
        do {
            CalendarList calendarList = null;
            try {
                calendarList = mService.calendarList().list().setPageToken(pageToken).execute();
            } catch (UserRecoverableAuthIOException e) {
                startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
            }catch (IOException e) {
                e.printStackTrace();
            }
            List<CalendarListEntry> items = calendarList.getItems();


            for (CalendarListEntry calendarListEntry : items) {

                if ( calendarListEntry.getSummary().toString().equals(calendarTitle)) {

                    id = calendarListEntry.getId().toString();
                }
            }
            pageToken = ((CalendarList) calendarList).getNextPageToken();
        } while (pageToken != null);

        return id;
    }

}


 */