package com.example.hanchat;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.hanchat.ui.calendar.CalendarFragment;
import com.example.hanchat.ui.chatbot.ChatbotFragment;
import com.example.hanchat.ui.group.GroupMainFragment;
import com.example.hanchat.ui.more.MoreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/*완료*/
public class MainActivity extends AppCompatActivity {
//    FrameLayout frame;
    Fragment frag_chat = ChatbotFragment.newInstance();
    Fragment frag_calendar = CalendarFragment.newInstance();
    Fragment frag_group = GroupMainFragment.newInstance();
    Fragment frag_more = MoreFragment.newInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.bottom_nav);
//        NavController navController = Navigation.findNavController(this, R.id.nav_main_fragment);
//        NavigationUI.setupWithNavController(navView, navController);


        if(savedInstanceState == null){
            replaceFragment(frag_chat);
        }

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @IdRes int id = 0;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(id == menuItem.getItemId())
                    return false;
                id = menuItem.getItemId();
                switch (menuItem.getItemId()){
                    case R.id.navigation_chat:
                        replaceFragment(frag_chat);
                        break;

                    case R.id.navigation_calendar:
                        replaceFragment(frag_calendar);
                        break;

                    case R.id.navigation_group:
                        replaceFragment(frag_group);
                        break;

                    case R.id.navigation_more:
                        replaceFragment(frag_more);
                        break;
                }
                return true;
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout, fragment).commit();
    }
}

