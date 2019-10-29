package com.example.hanchat.ui.calendar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hanchat.MainActivity;
import com.example.hanchat.R;
import com.example.hanchat.data.calendar.MonthFragment;
import com.example.hanchat.data.group.GroupPost;
import com.example.hanchat.databinding.RecyclerCalendarItemBinding;
import com.example.hanchat.module.CalendarAPIManager;
import com.example.hanchat.module.ViewPagerAdapter;
import com.example.hanchat.ui.group.grouppostlist.GroupPostlistFragmentDirections;

import pub.devrel.easypermissions.EasyPermissions;

import static androidx.core.content.ContextCompat.getSystemService;

public class CalendarFragment extends Fragment {

    private CalendarViewModel mViewModel;
    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    CalendarAPIManager calendarAPIManager;
    Button bt_go_chat;
    Intent intent;
    View view;

    ViewPager viewPager;
    //ArrayList<Month> calendarList=new ArrayList<>();
    ViewPagerAdapter pagerAdapter;

    TextView tv_calendarBar;
    GridView gridView;
    Button btn_today;
    RecyclerCalendarItemBinding binding;


    ConstraintLayout dayItem;

    MonthFragment calendarFragment;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_calendar, container, false);
//        calendarAPIManager = new CalendarAPIManager(CalendarActivity.this);


//
//        intent = new Intent(getContext(), MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//
//        // 우측 상단 버튼 (캘린더 화면으로 이동)
//        bt_go_chat = view.findViewById(R.id.bt_go_chat);
//
//        /*NavSetting();
//        IntentProfileSetting(CalendarActivity.this);*/
//        ButtonSetting();

        tv_calendarBar=(TextView)view.findViewById(R.id.tv_calendarBar);
        gridView=(GridView)view.findViewById(R.id.gridView);
        viewPager=(ViewPager)view.findViewById(R.id.viewPager_month);
        pagerAdapter=new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);
        btn_today=(Button)view.findViewById(R.id.btn_today);
        dayItem=(ConstraintLayout)view.findViewById(R.id.dayItem);

//        dayItem.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view2) {
//
//                Navigation.findNavController(view).navigate(R.id.action_subnav_calendarFragment_To_subnav_scheduleFragment);
//            }
//        });


        for(int i=-25; i<25; i++){

            MonthFragment fragment=new MonthFragment(i);
            pagerAdapter.addItem(fragment);

        }

        pagerAdapter.notifyDataSetChanged();
        int year=((MonthFragment)pagerAdapter.getItem(25)).month.year;
        int month=((MonthFragment)pagerAdapter.getItem(25)).month.month;
        tv_calendarBar.setText(year+"년 "+(month+1)+"월");

        btn_today.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(25);
                int today= ((MonthFragment)pagerAdapter.getItem(25)).getDay();
                //  (((MonthFragment)pagerAdapter.getItem(25)).gridAdapter.getToday(today).getHolder()).dayItem.setBackgroundResource(R.drawable.border);//getToday에서 NullPointException 발생

            }
        });

        btn_today.performClick();//today 버튼 강제클릭 코드: 위치 바꾸지 마!!


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int year=((MonthFragment)pagerAdapter.getItem(position)).getYear();
                int month=((MonthFragment)pagerAdapter.getItem(position)).getMonth()+1;
                tv_calendarBar.setText(year+"년 "+month+"월");

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //   binding= DataBindingUtil.setContentView(this, R.layout.recycler_calendar_item);
        //  binding.setModelCalendar(this);

//        public void CalendarDay_onClick(View view) {//더블클릭하면 스케줄 입력창 띄우기
//
//
//        }





        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        // TODO: Use the ViewModel


    }


    //버튼 세팅들은 여기에
    private void ButtonSetting() {
//        bt_go_chat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(intent);
//            }
//        });

    }


    /* CalendarAPIManager 사용하는 액티비티에서 이 코드 써야함 */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        calendarAPIManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    /* CalendarAPIManager 사용하는 액티비티에서 이 코드 써야함 끝 */
}