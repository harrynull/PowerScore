package com.github.hitgif.powerscore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SlideSwitchView extends View{
	

	private Bitmap mSwitchBottom;
	private Bitmap mSwitchThumb;
	private Bitmap mSwitchThumbNormal;
	private Bitmap mSwitchThumbPressed;
	private Bitmap mSwitchFrame;
	private Bitmap mSwitchMask;
	private float mCurrentX = 0;
	private boolean mSwitchOn = true;
	private int mMoveLength;
	private float mLastX = 0;
	private Rect mDest = null; 
	private Rect mSrc = null;
	private int mMoveDeltX = 0;
	private Paint mPaint = null;
	private OnSwitchChangedListener switchListener = null;
	private boolean mFlag = false;
	private boolean mEnabled = true;
	private final int MAX_ALPHA = 255;
	private int mAlpha = MAX_ALPHA;
	private boolean mIsScrolled =false;
	
	public SlideSwitchView(Context context) {
		this(context, null);
	}

	public SlideSwitchView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideSwitchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void init() {
		mSwitchThumbPressed = BitmapFactory.decodeResource(getResources(),R.drawable.checkswitch_btn_pressed);
		mSwitchThumbNormal = BitmapFactory.decodeResource(getResources(),R.drawable.checkswitch_btn_unpressed);
		mSwitchBottom = BitmapFactory.decodeResource(getResources(),R.drawable.checkswitch_bottom);
		mSwitchFrame = BitmapFactory.decodeResource(getResources(),R.drawable.checkswitch_frame);
		mSwitchMask = BitmapFactory.decodeResource(getResources(),R.drawable.checkswitch_mask);
		mSwitchThumb = mSwitchThumbNormal;
		mMoveLength = mSwitchBottom.getWidth() - mSwitchFrame.getWidth();
		mDest = new Rect(0, 0, mSwitchFrame.getWidth(),mSwitchFrame.getHeight());
		mSrc = new Rect();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setAlpha(255);
		mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension(mSwitchFrame.getWidth(), mSwitchFrame.getHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (mMoveDeltX > 0 || mMoveDeltX == 0 && mSwitchOn) {
			if (mSrc != null) {
				mSrc.set(mMoveLength - mMoveDeltX, 0, mSwitchBottom.getWidth()
						- mMoveDeltX, mSwitchFrame.getHeight());
			}
		} else if (mMoveDeltX < 0 || mMoveDeltX == 0 && !mSwitchOn) {
			if (mSrc != null) {
				mSrc.set(-mMoveDeltX, 0, mSwitchFrame.getWidth() - mMoveDeltX,
						mSwitchFrame.getHeight());
			}
		}
		//Log.d("mAlpha", "mAlpha:" + mAlpha);
		canvas.saveLayerAlpha(new RectF(mDest), mAlpha, Canvas.MATRIX_SAVE_FLAG
				| Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
				| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
				| Canvas.CLIP_TO_LAYER_SAVE_FLAG);
		canvas.drawBitmap(mSwitchBottom, mSrc, mDest, null);
		canvas.drawBitmap(mSwitchThumb, mSrc, mDest, null);
		canvas.drawBitmap(mSwitchFrame, 0, 0, null);
		canvas.drawBitmap(mSwitchMask, 0, 0, mPaint);
		canvas.restore();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(!mEnabled){
			return true;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mSwitchThumb = mSwitchThumbPressed;
			mLastX = event.getX();
			break;
		/*
		case MotionEvent.ACTION_MOVE:
			mCurrentX = event.getX();
			mMoveDeltX = (int) (mCurrentX - mLastX);
			if(mMoveDeltX > 10){

				mIsScrolled = true;
			}

			if ((mSwitchOn && mMoveDeltX < 0) || (!mSwitchOn && mMoveDeltX > 0)) {
				mFlag = true;
				mMoveDeltX = 0;
			}

			if (Math.abs(mMoveDeltX) > mMoveLength) {
				mMoveDeltX = mMoveDeltX > 0 ? mMoveLength : -mMoveLength;
			}
			invalidate();
			break;
			*/
		case MotionEvent.ACTION_UP:
			mSwitchThumb = mSwitchThumbNormal;
			if(!mIsScrolled){
				mMoveDeltX = mSwitchOn ? mMoveLength : -mMoveLength;
				mSwitchOn = !mSwitchOn;
				if (switchListener != null) {
					switchListener.onSwitchChange(this, mSwitchOn);
				}
				invalidate();
				mMoveDeltX = 0;
				break;
			}
			mIsScrolled = false;
			if (Math.abs(mMoveDeltX) > 0 && Math.abs(mMoveDeltX) < mMoveLength / 2) {
				mMoveDeltX = 0;
				invalidate();
			} else if (Math.abs(mMoveDeltX) > mMoveLength / 2
					&& Math.abs(mMoveDeltX) <= mMoveLength) {
				mMoveDeltX = mMoveDeltX > 0 ? mMoveLength : -mMoveLength;
				mSwitchOn = !mSwitchOn;
				if (switchListener != null) {
					switchListener.onSwitchChange(this, mSwitchOn);
				}
				invalidate();
				mMoveDeltX = 0;
			} else if (mMoveDeltX == 0 && mFlag) {
				mMoveDeltX = 0;
				mFlag = false;
			}
		default:
			break;
		}
		invalidate();
		return true;
	}
	public void setOnChangeListener(OnSwitchChangedListener listener) {
		switchListener = listener;
	}
	public interface OnSwitchChangedListener{
		public void onSwitchChange(SlideSwitchView switchView, boolean isChecked);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		mEnabled = enabled;
		mAlpha = enabled ? MAX_ALPHA : MAX_ALPHA/2;
		Log.d("enabled",enabled ? "true": "false");
		super.setEnabled(enabled);
		invalidate();
	}
	
	

	public void toggle() {
		setChecked(!mSwitchOn);
	}
	
    

    public void setChecked(boolean checked) {
    	mSwitchOn = checked;
        invalidate();
    }
}