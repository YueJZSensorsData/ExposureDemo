# 关于 Android 曝光采集的初步方案 

| 时间 | 版本号 | 修改 | 作者 |
|:----:|:----:|:----:|:----:|
|  2019-12-02  | 2.0 | 初版 | 岳剑钟|

注：本文档及代码为示例，不适用于任何线上环境，读者可结合实际需求进行开发

## 1. 普通 View 是否可见

**普通 View 的判断流程如下：**

1. 判断 view 是否为 null。

2. 通过 view 的宽度、高度，及 View.getLocalVisibleRect(Rect) 的返回值进行判断，当 View 可见的时候返回 true，不可见时返回 false。

3. 根据 View.isShown 判断 View 是否显示,这个方法检查了 View 的所有父 view 的的 Visibility 属性。

4. 通过 View.visibility 、动画等场景下，判断 view 是否可见。

5. 判断 view 的 visibility 是否为 View.VISIBLE

   ```
   fun isVisible(view: View):Boolean {
   
       if (view == null) {
           return false
       }
   
       if (view.width <= 0 || view.height <= 0 || view.alpha <= 0.0f || !view.getLocalVisibleRect(Rect())) {
           return false
       }
   
       if (!view.isShown) {
           return false
       }
   
       if (view.visibility != View.VISIBLE && view.animation != null && view.animation.fillAfter) {
           return true
       }
   
       return view.visibility == View.VISIBLE
   }
   
   ```



   ## 2. ListView 中 Item 是否可见

   判断 ListView 中 Item 是否可见，可通过监听 ListView 的 OnScrollListener 接口进行判断。通过判断 Item 是否在可见的 Item 范围内进行判断 Item 是否可见。

   > 注意 onScrollStateChanged 与 onScroll 方法调用的时机。

   以下为示例代码：

   ```
   private int mStart;
   private int mEnd;
   //是否第一次进入
   private boolean mFirstIn = true;
   ...
   
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
   
   ```



   ## 3. RecyclerView 中 Item 是否可见

   与 ListView 类似判断机制类似。

   以下为示例代码：

   ```
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
   ```

   ## 4. ScrollView 中 View 是否可见

   通过监听 OnTouchListener 接口，获取当前触摸的 View，然后针对 View 进行单独判断该 View 是否可见。

   以下为示例代码：

   ```
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
   ```

   ## 5. 参考链接

   [Android View的可见性检查方法（上）](http://unclechen.github.io/2016/10/17/Android%20View%E7%9A%84%E5%8F%AF%E8%A7%81%E6%80%A7%E6%A3%80%E6%9F%A5%E6%96%B9%E6%B3%95-%E4%B8%8A%E7%AF%87/)

   [示例代码 GitHub 地址](https://github.com/YueJZSensorsData/ExposureDemo)

以下为示例代码：
```kotlin
fun isVisible(view: View):Boolean {

    if (view == null) {
        return false
    }

    if (view.width <= 0 || view.height <= 0 || view.alpha <= 0.0f || !view.getLocalVisibleRect(Rect())) {
        return false
    }

    if (!view.isShown) {
        return false
    }

    if (view.visibility != View.VISIBLE && view.animation != null && view.animation.fillAfter) {
        return true
    }

    return view.visibility == View.VISIBLE
}

```



## 2. ListView 中 Item 是否可见

判断 ListView 中 Item 是否可见，可通过监听 ListView 的 OnScrollListener 接口进行判断。通过判断 Item 是否在可见的 Item 范围内进行判断 Item 是否可见。

> 注意 onScrollStateChanged 与 onScroll 方法调用的时机。

以下为示例代码：

```java
private int mStart;
private int mEnd;
//是否第一次进入
private boolean mFirstIn = true;
...

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

```



## 3. RecyclerView 中 Item 是否可见

与 ListView 类似判断机制类似。

以下为示例代码：
```java
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
```


## 4. ScrollView 中 View 是否可见

通过监听 OnTouchListener 接口，获取当前触摸的 View，然后针对 View 进行单独判断该 View 是否可见。

以下为示例代码：
```java
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
```


## 5. 参考链接

[Android View的可见性检查方法（上）](http://unclechen.github.io/2016/10/17/Android%20View%E7%9A%84%E5%8F%AF%E8%A7%81%E6%80%A7%E6%A3%80%E6%9F%A5%E6%96%B9%E6%B3%95-%E4%B8%8A%E7%AF%87/)

[示例代码 GitHub 地址](https://github.com/YueJZSensorsData/ExposureDemo)

