package com.github.hitgif.powerscore;

//import com.example.compoundbuttonview.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.widget.CompoundButton;

public class SwitchButton extends CompoundButton {
    private static final int TOUCH_MODE_IDLE = 0;
    private static final int TOUCH_MODE_DOWN = 1;
    private static final int TOUCH_MODE_DRAGGING = 2;

    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int MONOSPACE = 3;
    /** Switch���˶��켣 ���ȱ��� */
    private Drawable mThumbDrawable;
    /** Switch��=������ť�������ذ�ť*/
    private Drawable mTrackDrawable;
    /** Switch������ʵ�� on��off����������С */
    private int mThumbTextPadding;
    /** Switch�ؼ�����С��� */
    private int mSwitchMinWidth;
    /** Switch�ؼ���padding����ֵ */
    private int mSwitchPadding;
    /** Switch�ؼ���ѡ������µ��ַ���   */
    private CharSequence mTextOn;
    /** Switch�ؼ���δѡ������µ��ַ���   */
    private CharSequence mTextOff;
    /** Switch�ؼ��Ĵ���ʱ���ģʽ   */
    private int mTouchMode;
    private int mTouchSlop;
    private float mTouchX;
    private float mTouchY;
    private VelocityTracker mVelocityTracker = VelocityTracker.obtain();
    private int mMinFlingVelocity;

    private float mThumbPosition;
    private int mSwitchWidth;
    private int mSwitchHeight;
    private int mThumbWidth; 

    private int mSwitchLeft;
    private int mSwitchTop;
    private int mSwitchRight;
    private int mSwitchBottom;
    /** text�Ļ��ʹ��� */
    private TextPaint mTextPaint;
    private ColorStateList mTextColors;
    private Layout mOnLayout;
    private Layout mOffLayout;

    private Context mContext;

    @SuppressWarnings("hiding")
    private final Rect mTempRect = new Rect();

    private static final int[] CHECKED_STATE_SET = {
        android.R.attr.state_checked
    };


    public SwitchButton(Context context) {
        this(context, null);
        mContext = context;
    }


    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.switchStyle);
        mContext = context;
    }


    public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        Resources res = getResources();
        mTextPaint.density = res.getDisplayMetrics().density;
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.SwitchButton, defStyle, 0);//�������ļ��л�ȡ�������
        mThumbDrawable = a.getDrawable(R.styleable.SwitchButton_thumb);//��ȡ���õĹ켣��Դ
        mTrackDrawable = a.getDrawable(R.styleable.SwitchButton_track);//��ȡ���õĿ�����Դ
        mTextOn = a.getText(R.styleable.SwitchButton_textOn);//��ȡ������ ѡ������µ���ʾ����  on
        mTextOff = a.getText(R.styleable.SwitchButton_textOff);//��ȡ���õ� Ϊѡ������µ���ʾ���� off
        mThumbTextPadding = a.getDimensionPixelSize( R.styleable.SwitchButton_thumbTextPadding, 0);//��ȡ���õİ�ť�����С
        mSwitchMinWidth = a.getDimensionPixelSize(R.styleable.SwitchButton_switchMinWidth, 0);//��ȡ���õ���С���
        mSwitchPadding = a.getDimensionPixelSize(R.styleable.SwitchButton_switchPadding, 0);//��ȡ���õ�padding����

        int appearance = a.getResourceId(R.styleable.SwitchButton_switchTextAppearance, 0);//��ȡ�����ļ��л�ȡ��ʾ�����ʽ
        //�ж��趨��ʽ�Ƿ���ڣ����ڵĻ��͸�ֵ
        if (appearance != 0) {
            setSwitchTextAppearance(context, appearance);
        }
        a.recycle();//���������ļ���Դ
        ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledTouchSlop();
        mMinFlingVelocity = config.getScaledMinimumFlingVelocity();
        refreshDrawableState();
        setChecked(isChecked());
    }
    
    /** 
     * ����on,off��ʾ�������С����ʽ
     * */
    public void setSwitchTextAppearance(Context context, int resid) {
        mContext = context;
        TypedArray appearance = context.obtainStyledAttributes(resid,R.styleable.TextAppearance);
        ColorStateList colors;
        int ts;
        colors = appearance.getColorStateList(R.styleable.TextAppearance_textColor);
        if (colors != null) {
            mTextColors = colors;
        } else {
            mTextColors = getTextColors();
        }
        ts = appearance.getDimensionPixelSize(R.styleable.
                TextAppearance_textSize, 0);
        if (ts != 0) {
            if (ts != mTextPaint.getTextSize()) {
                mTextPaint.setTextSize(ts);
                requestLayout();
            }
        }
        int typefaceIndex, styleIndex;
        typefaceIndex = appearance.getInt(R.styleable.TextAppearance_typeface, -1);
        styleIndex = appearance.getInt(R.styleable.TextAppearance_textStyle, -1);
        setSwitchTypefaceByIndex(typefaceIndex, styleIndex);
        appearance.recycle();//���������ļ���Դ
    }
    /** �����Զ�����������ԣ�����������*/
    private void setSwitchTypefaceByIndex(int typefaceIndex, int styleIndex) {
        Typeface tf = null;
        switch (typefaceIndex) {
            case SANS:
                tf = Typeface.SANS_SERIF;//Ĭ�ϵ��޳���������
                break;
            case SERIF:
                tf = Typeface.SERIF;//Ĭ�ϵĳ���������
                break;
            case MONOSPACE:
                tf = Typeface.MONOSPACE;//Ĭ�ϵĵȿ�����������
                break;
        }
        setSwitchTypeface(tf, styleIndex);
    }
    /** ����������*/
    public void setSwitchTypeface(Typeface tf, int style) {
        if (style > 0) {
            if (tf == null) {
                tf = Typeface.defaultFromStyle(style);
            } else {
                tf = Typeface.create(tf, style);
            }
            setSwitchTypeface(tf);
            int typefaceStyle = tf != null ? tf.getStyle() : 0;
            int need = style & ~typefaceStyle;
            mTextPaint.setFakeBoldText((need & Typeface.BOLD) != 0);
            mTextPaint.setTextSkewX((need & Typeface.ITALIC) != 0 ? -0.25f : 0);
        } else {
            mTextPaint.setFakeBoldText(false);
            mTextPaint.setTextSkewX(0);
            setSwitchTypeface(tf);
        }
    }

    public void setSwitchTypeface(Typeface tf) {
        if (mTextPaint.getTypeface() != tf) {
            mTextPaint.setTypeface(tf);
            requestLayout();
            invalidate();
        }
    }
    /** ��ȡѡ��״̬�µ���ʾ���� */
    public CharSequence getTextOn() {
        return mTextOn;
    }
    /** �趨ѡ��״̬�µ���ʾ���� */
    public void setTextOn(CharSequence textOn) {
        mTextOn = textOn;
        requestLayout();
    }
    /** ��ȡ��ѡ��״̬�µ���ʾ���� */
    public CharSequence getTextOff() {
        return mTextOff;
    }

    /** �趨��ѡ��״̬�µ���ʾ���� */
    public void setTextOff(CharSequence textOff) {
        mTextOff = textOff;
        requestLayout();
    }
    
    /** �����ؼ���ߣ�����ͼʱʹ�á� */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (mOnLayout == null) {
            mOnLayout = makeLayout(mTextOn);
        }
        if (mOffLayout == null) {
            mOffLayout = makeLayout(mTextOff);
        }
        mTrackDrawable.getPadding(mTempRect);
        final int maxTextWidth = Math.max(mOnLayout.getWidth(), mOffLayout.getWidth());
        final int switchWidth = Math.max(mSwitchMinWidth,
                maxTextWidth * 2 + mThumbTextPadding * 4 + mTempRect.left + mTempRect.right);
        final int switchHeight = mTrackDrawable.getIntrinsicHeight();
        mThumbWidth = maxTextWidth + mThumbTextPadding * 2;
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                widthSize = Math.min(widthSize, switchWidth);
                break;

            case MeasureSpec.UNSPECIFIED:
                widthSize = switchWidth;
                break;

            case MeasureSpec.EXACTLY:
                break;
        }
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                heightSize = Math.min(heightSize, switchHeight);
                break;
            case MeasureSpec.UNSPECIFIED:
                heightSize = switchHeight;
                break;

            case MeasureSpec.EXACTLY:
                break;
        }
        mSwitchWidth = switchWidth;
        mSwitchHeight = switchHeight;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int measuredHeight = getMeasuredHeight();
        if (measuredHeight < switchHeight) {
            setMeasuredDimension(getMeasuredWidth(), switchHeight);
        }
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        populateAccessibilityEvent(event);
        return false;
    }

    public void populateAccessibilityEvent(AccessibilityEvent event) {
        if (isChecked()) {
            CharSequence text = mOnLayout.getText();
            if (TextUtils.isEmpty(text)) {
                text = mContext.getString(R.string.switch_on);
                if (isChecked()) {
                    Log.d("222333", "ON!!!");
                } else {
                    Log.d("222333","OFF!!!");
                }
            }
            event.getText().add(text);
        } else {
            CharSequence text = mOffLayout.getText();
            if (TextUtils.isEmpty(text)) {
                text = mContext.getString(R.string.switch_off);
                if (isChecked()) {
                    Log.d("222333","ON!!!");
                } else {
                    Log.d("222333","OFF!!!");
                }
            }
            event.getText().add(text);
        }
    }

    private Layout makeLayout(CharSequence text) {
        return new StaticLayout(text, mTextPaint,(int) Math.ceil(Layout.getDesiredWidth(text, mTextPaint)),Layout.Alignment.ALIGN_NORMAL, 1.f, 0, true);
    }

    /**
     * @������return true ˵��������(x,y)Ŀ��������
     */
    private boolean hitThumb(float x, float y) {
        mThumbDrawable.getPadding(mTempRect);
        final int thumbTop = mSwitchTop - mTouchSlop;
        final int thumbLeft = mSwitchLeft + (int) (mThumbPosition + 0.5f) - mTouchSlop;
        final int thumbRight = thumbLeft + mThumbWidth +
                mTempRect.left + mTempRect.right + mTouchSlop;
        final int thumbBottom = mSwitchBottom + mTouchSlop;
        return x > thumbLeft && x < thumbRight && y > thumbTop && y < thumbBottom;
    }
    
    /** ���ݴ����������¼� */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mVelocityTracker.addMovement(ev);
        final int action = ev.getActionMasked();
      
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();
                if (isEnabled() && hitThumb(x, y)) {
                    mTouchMode = TOUCH_MODE_DOWN;
                    mTouchX = x;
                    mTouchY = y;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                switch (mTouchMode) {
                    case TOUCH_MODE_IDLE:
                    	return true;
                    case TOUCH_MODE_DOWN: {
                        final float x = ev.getX();
                        final float y = ev.getY();
                        if (Math.abs(x - mTouchX) > mTouchSlop ||
                                Math.abs(y - mTouchY) > mTouchSlop) {
                            mTouchMode = TOUCH_MODE_DRAGGING;
                            getParent().requestDisallowInterceptTouchEvent(true);
                            mTouchX = x;
                            mTouchY = y;
                            return true;
                        }
                        break;
                    }
                    case TOUCH_MODE_DRAGGING: {
                        final float x = ev.getX();
                        final float dx = x - mTouchX;
                        float newPos = Math.max(0,
                                Math.min(mThumbPosition + dx, getThumbScrollRange()));
                        if (newPos != mThumbPosition) {
                            mThumbPosition = newPos;
                            mTouchX = x;
                            invalidate();
                        }
                        return true;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mTouchMode == TOUCH_MODE_DRAGGING) {
                    stopDrag(ev);
                    return true;
                }
                mTouchMode = TOUCH_MODE_IDLE;
                mVelocityTracker.clear();
                break;
            }
        }
        return super.onTouchEvent(ev);
    }

    private void cancelSuperTouch(MotionEvent ev) {
        MotionEvent cancel = MotionEvent.obtain(ev);
        cancel.setAction(MotionEvent.ACTION_CANCEL);
        super.onTouchEvent(cancel);
        cancel.recycle();
    }


    private void stopDrag(MotionEvent ev) {
        mTouchMode = TOUCH_MODE_IDLE;
     
        boolean commitChange = ev.getAction() == MotionEvent.ACTION_UP && isEnabled();

        cancelSuperTouch(ev);

        if (commitChange) {
            boolean newState;
            mVelocityTracker.computeCurrentVelocity(1000);
            float xvel = mVelocityTracker.getXVelocity();
            if (Math.abs(xvel) > mMinFlingVelocity) {
                newState = xvel > 0;
            } else {
                newState = getTargetCheckedState();
            }
            animateThumbToCheckedState(newState);
        } else {
            animateThumbToCheckedState(isChecked());
        }
    }

    private void animateThumbToCheckedState(boolean newCheckedState) {
        
        setChecked(newCheckedState);
    }

    private boolean getTargetCheckedState() {
        return mThumbPosition >= getThumbScrollRange() / 2;
    }
    //����ѡ�е�״̬��ѡ��:true   ��ѡ��: false��
    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        mThumbPosition = checked ? getThumbScrollRange() : 0;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mThumbPosition = isChecked() ? getThumbScrollRange() : 0;

        int switchRight = getWidth() - getPaddingRight();
        int switchLeft = switchRight - mSwitchWidth;
        int switchTop = 0;
        int switchBottom = 0;
        switch (getGravity() & Gravity.VERTICAL_GRAVITY_MASK) {
            default:
            case Gravity.TOP:
                switchTop = getPaddingTop();
                switchBottom = switchTop + mSwitchHeight;
                break;

            case Gravity.CENTER_VERTICAL:
                switchTop = (getPaddingTop() + getHeight() - getPaddingBottom()) / 2 -
                        mSwitchHeight / 2;
                switchBottom = switchTop + mSwitchHeight;
                break;

            case Gravity.BOTTOM:
                switchBottom = getHeight() - getPaddingBottom();
                switchTop = switchBottom - mSwitchHeight;
                break;
        }

        mSwitchLeft = switchLeft;
        mSwitchTop = switchTop;
        mSwitchBottom = switchBottom;
        mSwitchRight = switchRight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int switchLeft = mSwitchLeft;
        int switchTop = mSwitchTop;
        int switchRight = mSwitchRight;
        int switchBottom = mSwitchBottom;
        mTrackDrawable.setBounds(switchLeft, switchTop, switchRight, switchBottom);
        mTrackDrawable.draw(canvas);

        canvas.save();

        mTrackDrawable.getPadding(mTempRect);
        int switchInnerLeft = switchLeft + mTempRect.left;
        int switchInnerTop = switchTop + mTempRect.top;
        int switchInnerRight = switchRight - mTempRect.right;
        int switchInnerBottom = switchBottom - mTempRect.bottom;
        canvas.clipRect(switchInnerLeft, switchTop, switchInnerRight, switchBottom);


        mThumbDrawable.getPadding(mTempRect);
        final int thumbPos = (int) (mThumbPosition + 0.5f);
        int thumbLeft = switchInnerLeft - mTempRect.left + thumbPos;
        int thumbRight = switchInnerLeft + thumbPos + mThumbWidth + mTempRect.right;

        mThumbDrawable.setBounds(thumbLeft, switchTop, thumbRight, switchBottom);
        mThumbDrawable.draw(canvas);

        if (mTextColors != null) {
            mTextPaint.setColor(mTextColors.getColorForState(getDrawableState(),
                    mTextColors.getDefaultColor()));
        }
        mTextPaint.drawableState = getDrawableState();

        Layout switchText = getTargetCheckedState() ? mOnLayout : mOffLayout;

        canvas.translate((thumbLeft + thumbRight) / 2 - switchText.getWidth() / 2,
                (switchInnerTop + switchInnerBottom) / 2 - switchText.getHeight() / 2);
        switchText.draw(canvas);

        canvas.restore();
    }

    @Override
    public int getCompoundPaddingRight() {
        int padding = super.getCompoundPaddingRight() + mSwitchWidth;
        if (!TextUtils.isEmpty(getText())) {
            padding += mSwitchPadding;
        }
        return padding;
    }

    private int getThumbScrollRange() {
        if (mTrackDrawable == null) {
            return 0;
        }
        mTrackDrawable.getPadding(mTempRect);
        return mSwitchWidth - mThumbWidth - mTempRect.left - mTempRect.right;
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] myDrawableState = getDrawableState();
        if (mThumbDrawable != null) mThumbDrawable.setState(myDrawableState);
        if (mTrackDrawable != null) mTrackDrawable.setState(myDrawableState);
        invalidate();
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == mThumbDrawable || who == mTrackDrawable;
    }

}
