package com.yilong.todolist.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;

import com.yilong.todolist.MainActivity;
import com.yilong.todolist.R;
import com.yilong.todolist.dialog.ContentDialog;
import com.yilong.todolist.utils.DisplayUtil;


/**
 * @author wanyuyong
 * @version 4.0
 * @title:RefreshView.java
 * @description:
 * @company: 美丽说（北京）网络科技有限公司
 * @created Aug 30, 2013
 */
public class RefreshView extends ViewGroup implements OnGestureListener, ContentDialog.Listener {
    private final static int DEFAULT_VIEW_HEIGHT = 150;
    private final static int DEFAULT_TRIGGER_HEIGHT = 50;
    private final static int DEFAULT_MIN_REFRESH_DURING = 700;

    private View headView;
    private View middleView;
    private View slidablyView;
    private Scroller scroller;
    private GestureDetector mGestureDetector;
    private int back_duration_max = 1000;
    private int view_height;
    private int trigger_height;
    private float lastY;
    private int ignore_y = 5;
    private boolean firstDraw = true;

    private static final int NORMAL = 0;
    private static final int WAIT_ADD = 1;

    private int state = NORMAL;

    private long refreshTime;

    private Listener listener;
    private Context context;

    private static final String TAG = "RefreshView";
    private VelocityTracker vTracker = null;

    private boolean disAllowTouchEnvent = false;

    private TextView tip;
    private FrameLayout ff;

    private ContentDialog contentDialog;

    @Override
    public void resultContentSuccess(String content) {
        tip.setText("");
        MainActivity.add(content);
        scrollTo(0, view_height);
        state = NORMAL;
    }

    @Override
    public void resultNoContentError() {
        tip.setText("");
        close();
    }

    /**
     * 刷新接口
     */
    public static interface Listener {
        void onRefresh();

        void onClose();
    }

    public void setListener(Listener l) {
        this.listener = l;
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        this.context = context;
    }

    public RefreshView(Context context) {
        super(context);
        init(context);
        this.context = context;
    }

    private void init(Context context) {
        view_height = DisplayUtil.dipToPixels(context, DEFAULT_VIEW_HEIGHT);
        trigger_height = DisplayUtil.dipToPixels(context,
                DEFAULT_TRIGGER_HEIGHT);
        Activity activity = (Activity) context;
        headView = activity.getLayoutInflater().inflate(R.layout.refresh_head,
                null);
        tip = headView.findViewById(R.id.tip);
        ff = headView.findViewById(R.id.ff);
        ff.setBackgroundColor(Color.rgb(81, 57, 157));

        tip.setText("继续下拉将创建新任务");
        addView(headView);
        scroller = new Scroller(context);
        mGestureDetector = new GestureDetector(this);
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        middleView = child;
        super.addView(child, index, params);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        headView.layout(0, 0, headView.getMeasuredWidth(), view_height);
        middleView.layout(0, view_height, middleView.getMeasuredWidth(),
                view_height + middleView.getMeasuredHeight());
        if (firstDraw) {
            scrollTo(0, headView.getHeight());
            firstDraw = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        headView.measure(widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(view_height, MeasureSpec.EXACTLY));
        middleView.measure(widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));
        setMeasuredDimension(w, h);
    }

    /**
     * 回弹
     */
    private void back() {
        int distanceY = getScrollY();
        if (distanceY == trigger_height) {
            return;
        }
        if (state == WAIT_ADD) {
            refreshTime = System.currentTimeMillis();
            int dy = headView.getHeight() - distanceY - trigger_height;

            int duration = (int) (back_duration_max * Math.abs(dy) / (float) headView
                    .getHeight());
            scroller.startScroll(0, distanceY, 0, dy, duration);
          tip.setText("");
            invalidate();

            if (contentDialog == null) {
                contentDialog = new ContentDialog(getContext(), R.style.listDialog);
                contentDialog.setListener(this);
            }
            contentDialog.show();
            if (listener != null)
                listener.onRefresh();
        } else if (state == NORMAL) {
            int dy = headView.getHeight() - distanceY;
            int duration = (int) (back_duration_max * Math.abs(dy) / (float) headView
                    .getHeight());
            scroller.startScroll(0, distanceY, 0, dy, duration);
            invalidate();
        }
    }


    /**
     * 关闭此View
     */
    public void close() {
        if (System.currentTimeMillis() - refreshTime < DEFAULT_MIN_REFRESH_DURING) {
            long delay = DEFAULT_MIN_REFRESH_DURING
                    - (System.currentTimeMillis() - refreshTime) + 1;
            delayClose(delay);
        } else {
            state = NORMAL;
            back();
            if (listener != null) {
                listener.onClose();
            }
        }
    }

    /**
     * 关闭此View
     */
    public void closeRightNow() {
        state = NORMAL;
        back();
        if (listener != null) {
            listener.onClose();
        }
    }

    private void delayClose(long delay) {
        postDelayed(new Runnable() {

            @Override
            public void run() {
                close();
            }
        }, delay);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(0, scroller.getCurrY());
            invalidate();
        }
    }

    /**
     * 设置需要刷新的目标View,如果目标view为直接子View，则不需要设置。
     *
     * @param slidablyView
     */
    public void setSlidablyView(View slidablyView) {
        this.slidablyView = slidablyView;
    }

    private boolean isTop() {
        View target = slidablyView != null ? slidablyView : middleView;
        if (target instanceof ListView) {
            ListView view = (ListView) target;
            int position = view.getFirstVisiblePosition();
            if (position == 0 && view.getChildCount() != 0) {
                View child = (View) view.getChildAt(0);
                Rect outRect = new Rect();
                child.getHitRect(outRect);
                if (outRect.top == view.getPaddingTop()) {
                    return true;
                }
            }

            if (position == 0 && view.getChildCount() == 0) {
                return true;
            }
            return false;
        }
        return target.getScrollY() == 0;
    }

    public void disAllowTouchEvent(boolean disAllowTouchEvent) {
        this.disAllowTouchEnvent = disAllowTouchEvent;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (disAllowTouchEnvent) {
            return super.onInterceptTouchEvent(ev);
        }

        if (state == WAIT_ADD)
            return false;
        float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (vTracker == null) {
                    vTracker = VelocityTracker.obtain();
                } else {
                    vTracker.clear();
                }
                vTracker.addMovement(ev);
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                vTracker.addMovement(ev);
                vTracker.computeCurrentVelocity(1000);
                if (Math.abs(vTracker.getYVelocity()) > Math.abs(vTracker
                        .getXVelocity()) && y > lastY + ignore_y && isTop()) {
                    ev.setAction(MotionEvent.ACTION_DOWN);
                    onTouchEvent(ev);
                    ((ViewGroup) getParent())
                            .requestDisallowInterceptTouchEvent(true);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (vTracker != null) {
                    vTracker.clear();
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_CANCEL) {
            back();
            return true;
        }
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
//        ff.setVisibility(View.VISIBLE);
        float ratio = getScrollY() / (float) headView.getHeight();
        ratio *= 0.7;
        distanceY = distanceY * ratio;
        if (getScrollY() + distanceY >= headView.getHeight()) {
            distanceY = headView.getHeight() - getScrollY();
        }

        int d = headView.getHeight() - getScrollY();
        if (d > trigger_height) {
            d = trigger_height;
        }

        Log.i("zzzzzzz", "distanceY = " + distanceY + "    getScrollY()  =  " + getScrollY() + "     " +
                "headView.getHeight() = " + headView.getHeight() + "    " +
                "d = " + d);
        headView.setPivotX(headView.getMeasuredWidth() / 2);
        headView.setPivotY(headView.getMeasuredHeight());
        headView.setRotationX(90 - 90 * (d / (float) trigger_height));

        scrollBy(0, (int) distanceY);

        if (d >= trigger_height && state == NORMAL) {
            tip.setText("松手将创建新任务");
            state = WAIT_ADD;
        } else if (d < trigger_height && state == WAIT_ADD) {
            state = NORMAL;
            tip.setText("继续下拉将创建新任务");
        } else if (d < trigger_height && state == NORMAL) {
            state = NORMAL;
            tip.setText("继续下拉将创建新任务");
        }
        invalidate();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

}
