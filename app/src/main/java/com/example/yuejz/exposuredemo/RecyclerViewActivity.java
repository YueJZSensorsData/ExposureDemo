package com.example.yuejz.exposuredemo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private HomeAdapter mAdapter;


    private int mStart;
    private int mEnd;
    //是否第一次进入
    private boolean mFirstIn = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        initData();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new HomeAdapter());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager mLinearLayoutManager = ((LinearLayoutManager)mRecyclerView.getLayoutManager());
                mStart = mLinearLayoutManager.findFirstVisibleItemPosition();
                mEnd = mLinearLayoutManager.findLastVisibleItemPosition();
                //第一次显示的时候调用
                int visibleItemCount = mEnd - mStart;
                if (mFirstIn && visibleItemCount > 0) {
                    Log.d("first", String.valueOf(mStart));
                    Log.d("end", String.valueOf(mEnd));
                    mFirstIn = false;
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch(newState) {
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
        });

    }

    protected void initData()
    {
        mDatas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++)
        {
            mDatas.add("" + (char) i);
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    RecyclerViewActivity.this).inflate(R.layout.item_recycler, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            holder.tv.setText(mDatas.get(position));
        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            TextView tv;

            MyViewHolder(View view)
            {
                super(view);
                tv = view.findViewById(R.id.id_num);
            }
        }
    }
}
