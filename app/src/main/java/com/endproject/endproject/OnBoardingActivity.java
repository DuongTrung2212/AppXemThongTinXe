package com.endproject.endproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator3;

public class OnBoardingActivity extends AppCompatActivity {
    ViewPager2 view_pager;
    TextView tv_skip;
    RelativeLayout layout_bottom;
    CircleIndicator3 circle_indicator;
    LinearLayout layout_next;
    ViewPageAdapter viewPageAdapter;
    public static Context context;
    boolean logged=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        context=getApplicationContext();
        initUI();
        SharedPreferences preferences=getSharedPreferences("logged",MODE_PRIVATE);
        logged=preferences.getBoolean("logged",false);
        if(logged==true){
            Intent intent = new Intent(OnBoardingActivity.this,LoginActivity.class);
            startActivity(intent);
        }
        viewPageAdapter=new ViewPageAdapter(this);
        view_pager.setAdapter(viewPageAdapter);
        circle_indicator.setViewPager(view_pager);

        view_pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position==2){
                    tv_skip.setVisibility(View.GONE);
                    layout_bottom.setVisibility(View.GONE);
                }else{
                    tv_skip.setVisibility(View.VISIBLE);
                    layout_bottom.setVisibility(View.VISIBLE);
                }
            }
        });

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_pager.setCurrentItem(2);
            }
        });


        layout_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view_pager.getCurrentItem()<2){
                    view_pager.setCurrentItem(view_pager.getCurrentItem()+1);
                }
            }
        });

    }



    private  void  initUI(){
        view_pager=findViewById(R.id.view_pager);
        tv_skip=findViewById(R.id.tv_skip);
        layout_bottom=findViewById(R.id.layout_botom);
        circle_indicator=findViewById(R.id.circle_indicator);
        layout_next=findViewById(R.id.layout_next);
    }
}