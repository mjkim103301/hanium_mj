package com.example.hanchat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.hanchat.module.AccountManager;
import com.example.hanchat.ui.calendar.CalendarFragment;
import com.example.hanchat.ui.chatbot.ChatbotFragment;
import com.example.hanchat.ui.group.GroupMainFragment;
import com.example.hanchat.ui.more.MoreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

/*완료*/
public class MainActivity extends AppCompatActivity {
//    FrameLayout frame;
    Fragment frag_chat = ChatbotFragment.newInstance();
    Fragment frag_calendar = CalendarFragment.newInstance();
    Fragment frag_group = GroupMainFragment.newInstance();
    Fragment frag_more = MoreFragment.newInstance();

    Fragment currentFrag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appInitialize();

        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.bottom_nav);
        NavController navController = Navigation.findNavController(this, R.id.main_framelayout);
        NavigationUI.setupWithNavController(navView, navController);
//        if(savedInstanceState == null){
//            replaceFragment(frag_chat);
//        }
//
//        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @IdRes int id = 0;
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                if(id == menuItem.getItemId())
//                    return false;
//                id = menuItem.getItemId();
//                switch (menuItem.getItemId()){
//                    case R.id.navigation_chat:
//                        replaceFragment(frag_chat);
//                        break;
//
//                    case R.id.navigation_calendar:
//                        replaceFragment(frag_calendar);
//                        break;
//
//                    case R.id.navigation_group:
//                        replaceFragment(frag_group);
//                        break;
//
//                    case R.id.navigation_more:
//                        replaceFragment(frag_more);
//                        break;
//                }
//                return true;
//            }
//        });

    }


    private void replaceFragment(Fragment fragment){
        if(currentFrag != null)
            getSupportFragmentManager().beginTransaction().hide(currentFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout, fragment).commit();
        currentFrag = fragment;
    }

    private void appInitialize(){
        AccountManager am = AccountManager.getInstance(this);
        am.autoLogin(this, new AccountManager.Callback() {
            @Override
            public void setAccount(JSONObject json, int Resultno) {
                switch(Resultno){
                    case AccountManager.ACCOUNT_CREATE_SUCCESS:
                    case AccountManager.ACCOUNT_CREATE_FAILED:
                        appFirstSetting(json);
                    case AccountManager.LOGIN_SUCCESS:
                        try {
                            fetchFromServer();
                        }
                        catch (Exception e){

                        }
                        break;

                }
            }
        });

    }

    private void appFirstSetting(JSONObject json){
        //DB 세팅
        try {
            Toast.makeText(this, json.getString("pid"), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void fetchFromServer(){
        Toast.makeText(this, "fetch : " + AccountManager.getInstance(this).getLoginToken(), Toast.LENGTH_SHORT).show();
    }
}

