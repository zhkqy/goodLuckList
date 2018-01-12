package com.yilong.todolist.verticalviewpager.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Scroller;

import com.yilong.todolist.R;
import com.yilong.todolist.utils.DisplayUtil;


/**
 * 
 * @title:RefreshView.java
 * @description:
 * @company: 美丽说（北京）网络科技有限公司
 * @author wanyuyong
 * @version 4.0
 * @created Aug 30, 2013
 */
public class RefreshView extends ViewGroup implements OnGestureListener {
	private final static int DEFAULT_VIEW_HEIGHT = 130;
	private final static int DEFAULT_TRIGGER_HEIGHT = 50;
	private final static int DEFAULT_MIN_REFRESH_DURING = 700;

	private View headView;
	private View middleView;
	private View slidablyView;
	private ImageView img;
	private Scroller scroller;
	private GestureDetector mGestureDetector;
	private int back_duration_max = 1000;
	private int view_height;
	private int trigger_height;
	private float lastY;
	private int ignore_y = 5;
	private boolean firstDraw = true;

	private static final int NORMAL = 0;
	private static final int WILL_REFRESH = 1;
	private static final int REFRESHING = 2;

	private int state = NORMAL;

	private long refreshTime;

	private Listener listener;
	private Context context;
	private float percent;

	private static final String TAG = "RefreshView";
	private VelocityTracker vTracker = null;

	private boolean disAllowTouchEnvent = false;

	/**
	 * 刷新接口
	 * 
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
		addView(headView);
		img = findViewById(R.id.img);
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
		if (distanceY == headView.getHeight()) {
			return;
		}
		if (state == WILL_REFRESH) {
			state = REFRESHING;
			refreshTime = System.currentTimeMillis();
			int dy = trigger_height - distanceY;
			int duration = (int) (back_duration_max * Math.abs(dy) / (float) headView
					.getHeight());
			scroller.startScroll(0, distanceY, 0, dy, duration);
			invalidate();
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


	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (state == REFRESHING || getScrollY() >= view_height) {
			return;
		}

//		Drawable drawable = getCurrentDrawable();
//		if (drawable == null) {
//			return;
//		}
//		int left = (getWidth() - drawable.getIntrinsicWidth()) / 2;
//		int maxTop = progressbar.getTop();
//		int minTop = view_height - drawable.getIntrinsicHeight() / 2;
//		int top = (int) (maxTop + (minTop - maxTop) * (1 - percent));
//
//		drawable.setBounds(left, top, left + drawable.getIntrinsicWidth(), top
//				+ drawable.getIntrinsicHeight());
//		drawable.draw(canvas);
	}

	/**
	 * 获得每个图片的drawable对象
	 * 
	 * @return
	 */
	private Drawable getCurrentDrawable() {
		int index = (int) (15 * percent);
		index = Math.min(15, index);
		String name = String.valueOf(index).length() > 1 ? "loading_000"
				+ index : "loading_0000" + index;
		int indentify = getResources().getIdentifier(name, "drawable",
				context.getPackageName());
		return getResources().getDrawable(indentify);
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

	public void refresh() {
		scrollTo(0, trigger_height);
		state = WILL_REFRESH;
		back();
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			scrollTo(0, scroller.getCurrY());
			refreshPercent();
			invalidate();
		}
	}

	private void refreshPercent() {
		float scrollY = getScrollY();
		percent = (headView.getHeight() - scrollY)
				/ (headView.getHeight() - headView.getPaddingTop());
		percent = Math.min(percent, 1);
		percent = Math.max(percent, 0);
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

			if(position==0 &&view.getChildCount()==0){
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

		if (state == REFRESHING)
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
		float ratio = getScrollY() / (float) headView.getHeight();
		ratio *= 0.7;
		distanceY = distanceY * ratio;
		if (getScrollY() + distanceY >= headView.getHeight()) {
			distanceY = headView.getHeight() - getScrollY();
		}

		scrollBy(0, (int) distanceY);
		if (getScrollY() > trigger_height && state == WILL_REFRESH) {
			state = NORMAL;
		} else if (getScrollY() < trigger_height && state == NORMAL) {
			state = WILL_REFRESH;
		}
		refreshPercent();
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
