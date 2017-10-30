package com.example.mymenu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by 潘硕 on 2017/10/30.
 */

public class MainUI extends RelativeLayout {
    private Context context;
    private FrameLayout leftmenu;
    private FrameLayout middlemenu;
    private FrameLayout rightmenu;
    private Scroller mScroller;

    public MainUI(Context context) {
        super(context);
    }
    public MainUI(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    private void initView(Context context){
        this.context = context;
        mScroller = new Scroller(context,new DecelerateInterpolator());
        leftmenu = new FrameLayout(context);
        middlemenu = new FrameLayout(context);
        rightmenu = new FrameLayout(context);
        leftmenu.setBackgroundColor(Color.RED);
        middlemenu.setBackgroundColor(Color.GREEN);
        rightmenu.setBackgroundColor(Color.RED);
        addView(leftmenu);
        addView(middlemenu);
        addView(rightmenu);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        middlemenu.measure(widthMeasureSpec,heightMeasureSpec);
        int realWidth = MeasureSpec.getSize(widthMeasureSpec);
        int tempWidthMeasure = MeasureSpec.makeMeasureSpec((int)(realWidth*0.8f),MeasureSpec.EXACTLY);
        leftmenu.measure(tempWidthMeasure,heightMeasureSpec);
        rightmenu.measure(tempWidthMeasure,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        middlemenu.layout(l, t, r, b);
        leftmenu.layout(l-leftmenu.getMeasuredWidth(), t, r, b);
        rightmenu.layout(l+middlemenu.getMeasuredWidth(), t, r+middlemenu.getMeasuredWidth()+rightmenu.getMeasuredWidth(), b);
    }

    private boolean isTestCompete;
    private boolean isleftrightEvent;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isTestCompete){
            getEventType(ev);
            return true;
        }
        if (isleftrightEvent){
            switch (ev.getActionMasked()){
                case MotionEvent.ACTION_MOVE:
                    int curScrollx = getScrollX();
                    int dis_x = (int) (ev.getX() - point.x);
                    int expectX = -dis_x+curScrollx;
                    int finalX = 0;
                    if (expectX < 0){
                        finalX = Math.max(expectX,-leftmenu.getMeasuredWidth());
                    }else{
                        finalX = Math.min(expectX,rightmenu.getMeasuredWidth());
                    }
                    scrollTo(finalX,0);
                    point.x = (int) ev.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_CANCEL:
                    curScrollx = getScrollX();
                    if (Math.abs(curScrollx)>leftmenu.getMeasuredWidth()>>1){
                        if (curScrollx<0){
                            mScroller.startScroll(curScrollx,0,-leftmenu.getMeasuredWidth()-curScrollx,0,200);
                        }else {
                            mScroller.startScroll(curScrollx,0,leftmenu.getMeasuredWidth()-curScrollx,0,200);//这里的200是动画的执行时间为200ms
                        }
                    }else {
                        mScroller.startScroll(curScrollx,0,-curScrollx,0,200);
                    }
                    invalidate();
                    isleftrightEvent = false;
                    isTestCompete = false;
                    break;
            }
        }else {
            switch (ev.getActionMasked()){
                case MotionEvent.ACTION_UP:
                    isleftrightEvent = false;
                    isTestCompete = false;
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (!mScroller.computeScrollOffset()){
            return;
        }
        int tempX = mScroller.getCurrX();
        scrollTo(tempX,0);
    }

    private Point point = new Point();
    private static final int TEST_DIS = 20;
    private void getEventType(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                point.x = (int) ev.getX();
                point.y = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dX = (int) Math.abs(ev.getX() - point.x);
                int dY = (int) Math.abs(ev.getY() - point.y);
                if (dX>=TEST_DIS && dX>dY){
                    isleftrightEvent = true;
                    isTestCompete = true;
                    point.x = (int) ev.getX();
                    point.y = (int) ev.getY();
                }else if (dY>=TEST_DIS && dX<dY){
                    isleftrightEvent = false;
                    isTestCompete = true;
                    point.x = (int) ev.getX();
                    point.y = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
    }
}
