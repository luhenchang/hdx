package com.accuvally.hdtui.ui;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 * 联合ViewPager使用 banner 底部画圆
 * 
 * @author wan
 *
 */
public class CirclePageIndicator extends View {

	private float mRadius = 3;
    private ViewPager mViewPager;
    private int mCurrentPage;
    private float mPageOffset;
    
    private OnPageChangeListener mListener;
    
    private final Paint mPaintPageFill = new Paint(ANTI_ALIAS_FLAG);
    private Paint mPaintSelectedFill = new Paint(ANTI_ALIAS_FLAG);
    
    private int colorPageFill = 0x7FFFFFFF;//其他颜色
    private int colorSelected = 0xFF61b754;// 选中的颜色

    public CirclePageIndicator(Context context, AttributeSet attrs) {
    	super(context, attrs);
    	
    	DisplayMetrics dm = getResources().getDisplayMetrics();
    	mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mRadius, dm);
    	
    	mPaintPageFill.setColor(colorPageFill);
		mPaintSelectedFill.setColor(colorSelected);
    }

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mViewPager == null) {
			return;
		}
		final int count = mViewPager.getAdapter().getCount();
		if (count == 0) {
			return;
		}
		if (mCurrentPage >= count) {
			setCurrentItem(count - 1);
			return;
		}

		float dX = 0;
		float dY = 0;

		int longSize = getWidth();
		int longPaddingBefore = getPaddingLeft();
		int longPaddingAfter = getPaddingRight();
		int shortPaddingBefore = getPaddingTop();

		final float threeRadius = mRadius * 3;

		final float shortOffset = shortPaddingBefore + mRadius;
		float longOffset = ((longSize - longPaddingBefore - longPaddingAfter) / 2.0f) - ((count * threeRadius) / 2.0f);

		float pageFillRadius = mRadius;

		for (int iLoop = 0; iLoop < count; iLoop++) {
			dX = longOffset + (iLoop * threeRadius);
			dY = shortOffset;
			canvas.drawCircle(dX, dY, pageFillRadius, mPaintPageFill);
		}

		float cx = mCurrentPage * threeRadius;
		cx += mPageOffset * threeRadius;
		dX = longOffset + cx;
		dY = shortOffset;
		canvas.drawCircle(dX, dY, mRadius, mPaintSelectedFill);

//		for (int i = 1; i <= count; i++) {
//			dX = longOffset + ((i - 1) * threeRadius);
//			dY = shortOffset;
//			String num = i + "";
//			float textWidth = mPaintText.measureText(num);
//			canvas.drawText(num, dX - textWidth / 2, dY + textWidth / 2, mPaintText);
//		}
	}
	
	
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mViewPager.setCurrentItem(item);
        mCurrentPage = item;
        invalidate();
    }
    
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        if (view.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        mViewPager.setOnPageChangeListener(new PageListener());
        invalidate();
    }
    
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }
    
    private class PageListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {
			if (mListener != null) {
	            mListener.onPageScrollStateChanged(state);
	        }
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			mCurrentPage = position;
	        mPageOffset = positionOffset;
	        invalidate();
	        
	        if (mListener != null) {
	            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
	        }
		}

		@Override
		public void onPageSelected(int position) {
			if (mListener != null) {
	            mListener.onPageSelected(position);
	        }
		}
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec));
    }
    
	private int measureLong(int measureSpec) {
		int result;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if ((specMode == MeasureSpec.EXACTLY) || (mViewPager == null)) {
			result = specSize;
		} else {
			final int count = mViewPager.getAdapter().getCount();
			result = (int) (getPaddingLeft() + getPaddingRight() + (count * 2 * mRadius) + (count - 1) * mRadius + 1);
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}
    
    private int measureShort(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int)(2 * mRadius + getPaddingTop() + getPaddingBottom() + 1);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

	public float getRadius() {
		return mRadius;
	}

	public void setRadius(float radius) {
		this.mRadius = radius;
		invalidate();
	}

	public int getColorPageFill() {
		return colorPageFill;
	}

	public void setColorPageFill(int colorPageFill) {
		mPaintPageFill.setColor(colorPageFill);
		invalidate();
	}

	public int getColorSelected() {
		return colorSelected;
	}

	public void setColorSelected(int colorSelected) {
		mPaintSelectedFill.setColor(colorSelected);
		invalidate();
	}

}
