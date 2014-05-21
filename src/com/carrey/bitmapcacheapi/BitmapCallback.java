package com.carrey.bitmapcacheapi;

import android.graphics.Bitmap;

public interface BitmapCallback {
	public void onReady(String key, Bitmap bitmap);
}
