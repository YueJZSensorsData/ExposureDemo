package com.example.yuejz.exposuredemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button mButton1;
    Button mButton2;
    Button mButton3;
    ScrollView mScrollView;

    private final static  String  TAG="ScrollView";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton1 = findViewById(R.id.button1);
        mButton2 = findViewById(R.id.button2);
        mButton3 = findViewById(R.id.button3);
        mScrollView = findViewById(R.id.scrollView);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);


//        boolean temp = ViewUtil.isVisible(mButton3);
//        Log.d(TAG, String.valueOf(temp));
        Rect scrollBounds = new Rect();
        mScrollView.getHitRect(scrollBounds);
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
//                // 可以先判断ScrollView中的mView是不是在屏幕中可见
//                Rect scrollBounds = new Rect();
//                mScrollView.getHitRect(scrollBounds);
//                if (!mButton3.getLocalVisibleRect(scrollBounds)) {
//                    return;
//                }
//                if (isVisibleLocal(mButton3)){
//
////                    Log.d(TAG, "可见");
//                }
            }
        });
//        mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//            }
//        });

//        mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                boolean temp = ViewUtil.isVisible(mButton3);
//                Log.d(TAG, String.valueOf(temp));
//            }
//        });

        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        boolean temp = ViewUtil.isVisible(mButton3);
                        Log.d(TAG, String.valueOf(temp));
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                Intent intent = new Intent(MainActivity.this, ListViewActivity.class);
                startActivity(intent);
                break;
            case R.id.button2:
                Intent intent1 = new Intent(MainActivity.this, RecyclerViewActivity.class);
                startActivity(intent1);
                break;
        }
    }


}
