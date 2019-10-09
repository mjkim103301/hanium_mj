package com.example.hanchat.module;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.hanchat.R;

import java.util.ArrayList;

public class ViewPagerAdapter<T extends ViewPagerAdapter.ViewPagerItem>  extends PagerAdapter {
    // LayoutInflater 서비스 사용을 위한 Context 참조 저장.
    private Context mContext = null ;
    private ArrayList<T>  items;


    //이 어댑터에 사용할 아이템에 상속받아야 할 인터페이스
    public interface ViewPagerItem {
       // int getViewType();

        void setRecyclerContent(final View itemView);
    }

    @Override
    public int getCount() {//전체 페이지 수
        return 50;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }

    public ViewPagerAdapter() {

    }

    // Context를 전달받아 mContext에 저장하는 생성자 추가.
    public ViewPagerAdapter(Context context) {
        super();
        mContext = context ;
        items = new ArrayList<>();

    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View view = null ;

        if (true) {
            // LayoutInflater를 통해 "/res/layout/recycler_recycler_calendar.xml"을 뷰로 생성.
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.recycler_recycler_calendar, container, false);
//            RecyclerView recyclerDay=(RecyclerView)view.findViewById(R.id.recycler_Day);

            items.get(position).setRecyclerContent(view);
        }

        // 뷰페이저에 추가.
        container.addView(view) ;
        ((ViewPager)container).addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                startUpdate(container);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view ;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 뷰페이저에서 삭제
        container.removeView((View) object);
    }
    public void addItem(ArrayList<T> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }


//    public int getItemViewType(int position) {
//
//        return items.get(position).getViewType();
//    }
}
