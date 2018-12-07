package com.example.yuejz.exposuredemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ListViewActivity extends AppCompatActivity {

    private String[] data ;
    private ListView mListView;
    private int mStart;
    private int mEnd;
    //是否第一次进入
    private boolean mFirstIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        data = new String[20];
        for (int i = 0; i < 20; i++) {
            data[i] = String.valueOf(i);
        }

        mFirstIn = true;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ListViewActivity.this, android.R.layout.simple_list_item_1,data);
        mListView = findViewById(R.id.list_view);
        mListView.setAdapter(adapter);

        //这里还需要注意第一次打开 ListView 时，onScrollStateChanged 函数不会调用，
        // 但是 onScroll 会调用
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch(scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        // 手指触屏拉动准备滚动，只触发一次
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        // 持续滚动开始，只触发一次
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 整个滚动事件结束，只触发一次
                        Log.d("first", String.valueOf(mStart));
                        Log.d("end", String.valueOf(mEnd));
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                mStart = mListView.getFirstVisiblePosition();
                mEnd = firstVisibleItem + visibleItemCount;
                //第一次显示的时候调用 ，visibleItemCount > 0 表示ListView已经绘制出来，因为刚开始的时候 visibleItemCount 为0
                if (mFirstIn && visibleItemCount > 0) {
                    Log.d("first", String.valueOf(mStart));
                    Log.d("end", String.valueOf(mEnd));
                    mFirstIn = false;
                }

            }

        });

    }

}
