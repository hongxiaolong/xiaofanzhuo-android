package com.qihoo.xiaofanzhuo.datafromserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

public class HttpToServer {

	public static String webServer = "http://182.92.80.201/2.php?content=";

	private String mResult = "";

	private ZoneListData mDatas;
	private List<BusinessData> mBussinessList;

	public HttpToServer(Context context, String requeseCode)
			throws InterruptedException, Throwable {
		mBussinessList = new ArrayList<BusinessData>();
		requestFromServer(context, requeseCode);
		mDatas = new ZoneListData(mResult);
		mBussinessList = mDatas.getZoneListData();
	}

	public ZoneListData getZoneData() {
		return mDatas;
	}

	public List<BusinessData> getBusinessDataList() {
		return mBussinessList;
	}

	public void requestFromServer(Context context, String requeseCode)
			throws InterruptedException, Throwable {
		httpAsyncTask task = new httpAsyncTask(context, requeseCode);
		mResult = task.execute(webServer).get();
		return;
	}

	public static String sendToServer(String httpServer, String httpCode) {
		String ret = "";
		String finalServer = httpServer + URLEncoder.encode(httpCode);
		URL url = null;
		try {
			url = new URL(finalServer);
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection(); // 创建一个HTTP连接
			InputStreamReader in = new InputStreamReader(
					urlConn.getInputStream()); // 获得读取的内容
			BufferedReader buffer = new BufferedReader(in); // 获取输入流对象
			String inputLine = null;
			// 通过循环逐行读取输入流中的内容
			while ((inputLine = buffer.readLine()) != null) {
				ret += inputLine + "\n";
			}
			in.close(); // 关闭字符输入流对象
			urlConn.disconnect(); // 断开连接
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	private class httpAsyncTask extends AsyncTask<String, Integer, String> {

		private Context mContext;
		private String mCode;

		public httpAsyncTask(Context context, String code) {
			super();
			mContext = context;
			mCode = code;
		}

		@Override
		protected String doInBackground(String... params) {
			String ret = "";
			try {
				ret = sendToServer(params[0], mCode);
				return ret;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	};

}