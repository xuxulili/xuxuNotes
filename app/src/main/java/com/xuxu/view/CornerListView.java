package com.xuxu.view;

/**
 * Created by Administrator on 2015/8/14.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xuxu.xuxunotes.R;

/**
 * 选中时为圆角ListView,可设置背景被圆角
 */
public class CornerListView extends ListView {
    public CornerListView(Context context) {
        super(context);
    }

    public CornerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CornerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                int itemnum = pointToPosition(x, y);
                if (itemnum == AdapterView.INVALID_POSITION)
                    break;
                else {
                    if (itemnum == 0) {
                        if (itemnum == (getAdapter().getCount() - 1)) {
                            //只有一项
                            setSelector(R.drawable.app_list_corner_round);
                        } else {
                            //第一项
                            setSelector(R.drawable.app_list_corner_round_top);
                        }
                    } else if (itemnum == (getAdapter().getCount() - 1))
                        //最后一项
                        setSelector(R.drawable.app_list_corner_round_bottom);
                    else {
                        //中间项
                        setSelector(R.drawable.app_list_corner_round_center);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

}