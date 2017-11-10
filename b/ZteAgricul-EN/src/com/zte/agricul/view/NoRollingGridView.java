package com.zte.agricul.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class NoRollingGridView extends  GridView{
	public boolean hasScrollBar = true;
	 
    /**
     * @param context
     */
    public NoRollingGridView(Context context) {
        this(context, null);
    }
 
    public NoRollingGridView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }
 
    public NoRollingGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
 
        int expandSpec = heightMeasureSpec;
        if (hasScrollBar) {
            expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);// 注意这里,这里的意思是直接测量出GridView的高�?
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}