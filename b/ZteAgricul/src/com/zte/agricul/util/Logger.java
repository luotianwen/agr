package com.zte.agricul.util;


import android.util.Log;

import com.zte.agricul.app.Constants;



public class Logger {

	private static final boolean flag =Constants.LOG;

	
	public static void println(String msg){
		
		if (flag) {
			if (msg != null) {
				System.out.println(msg);
			}
		}
		
	}
	
	public static void i(String tag, String msg) {
		if (flag) {
			if (msg != null) {
				Log.i(tag, msg);
			}
		}
	}

	public static void d(String tag, String msg) {
		if (flag) {
			if (msg != null) {
				Log.d(tag, msg);
			}
		}
	}

	public static void v(String tag, String msg) {
		if (flag) {
			if (msg != null) {
				Log.v(tag, msg);
			}
		}
	}


	public static void e(String tag, String msg) {
		if (flag) {
			if (msg != null) {
				Log.i(tag, msg);
			}
		}
	}

}
