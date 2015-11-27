package it.andreale.mdatepicker.date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Andrea on 26/11/2015.
 */
public class MonthPageAdapter extends View implements ViewPager.OnPageChangeListener {

    private Controller mController;
    private ViewPager mViewPager;

    private Paint mPaint;
    private Rect mRect;

    private int mCurrentPosition;
    private int mLastPosition;
    private float mOffset;

    public MonthPageAdapter(Context context) {
        super(context);
        initialize();
    }

    public MonthPageAdapter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public MonthPageAdapter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mRect = new Rect();
    }

    public void setViewPager(Controller controller, ViewPager viewPager) {
        mController = controller;
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
        mPaint.setColor(mController.getMonthHeaderTextColor());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mCurrentPosition = position;
        mOffset = positionOffset;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        mLastPosition = position;
        mController.onPageChanged();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private float getTextWidth(String text) {
        return mPaint.measureText(text);
    }

    private float getTextHeight(String text) {
        mPaint.getTextBounds(text, 0, text.length(), mRect);
        return mRect.height();
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        // get view size
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (Math.min(width, height) == 0) {
            // skip drawing
            return;
        }
        if (mViewPager == null) {
            // skip drawing
            return;
        }
        PagerAdapter adapter = mViewPager.getAdapter();
        if (adapter == null) {
            // skip drawing
            return;
        }
        // set text size
        mPaint.setTextSize(height / 3);
        // get previous, current and next text
        CharSequence previous = adapter.getPageTitle(mLastPosition - 1);
        CharSequence current = adapter.getPageTitle(mLastPosition);
        CharSequence next = adapter.getPageTitle(mLastPosition + 1);
        // calculate view center
        float x = width / 2;
        float y = height / 2;
        float internalOffset = 0;
        if (mCurrentPosition < mLastPosition) {
            internalOffset = (1f - mOffset) * width;
        } else if (mCurrentPosition >= mLastPosition) {
            internalOffset = mOffset * -width;
        }
        // draw first text
        if (previous != null) {
            float cx = x - width + internalOffset;
            drawText(previous, cx, y, c);
        }
        // draw first text
        if (current != null) {
            float cx = x + internalOffset;
            drawText(current, cx, y, c);
        }
        // draw first text
        if (next != null) {
            float cx = x + width + internalOffset;
            drawText(next, cx, y, c);
        }
    }

    protected void drawText(CharSequence text, float x, float y, Canvas c) {
        String string = String.valueOf(text);
        float textX = x - (getTextWidth(string) / 2);
        float textY = y + (getTextHeight(string) / 2);
        c.drawText(string, textX, textY, mPaint);
    }

    public interface Controller {

        void onPageChanged();

        int getMonthHeaderTextColor();
    }
}
