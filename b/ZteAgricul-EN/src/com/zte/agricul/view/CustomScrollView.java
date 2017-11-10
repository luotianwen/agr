package com.zte.agricul.view;



import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView{
	 private GestureDetector mGestureDetector; 
	
	public CustomScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mGestureDetector = new GestureDetector(new VScrollDetector()); 
	    setFadingEdgeLength(0); 
	}
	
	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mGestureDetector = new GestureDetector(new VScrollDetector()); 
	    setFadingEdgeLength(0); 
	}
	
	public CustomScrollView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mGestureDetector = new GestureDetector(new VScrollDetector()); 
	    setFadingEdgeLength(0); 
	}

	
	 @Override
	 public boolean onInterceptTouchEvent(MotionEvent ev) { 
	     return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev); 
	 } 
	  
	    // Return false if we're scrolling in the x direction   
	 class VScrollDetector extends SimpleOnGestureListener { 
	        @Override
	    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {   	
	         if(Math.abs(distanceY) > Math.abs(distanceX)) { 
	                return true; 
	         } 
	        	
	         return false; 
	    } 
	 } 
}
