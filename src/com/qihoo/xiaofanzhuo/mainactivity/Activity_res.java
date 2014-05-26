package com.qihoo.xiaofanzhuo.mainactivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.carrey.bitmapcacheapi.ImageHelper;
import com.carrey.bitmapcacheapi.ImageLruCacheApi;
import com.carrey.bitmapcachedemo.R;
import com.qihoo.xiaofanzhuo.restaurantdetailactivity.RestaurantDetailActivity;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;
import android.os.Build;

public class Activity_res extends Activity 
{

	ProgressDialog dialog;
	ListView list;
	private Handler handler;
	BaseAdapter adapter;
	PopMenu popMenu_type;
	PopMenu popMenu_dis;
	PopMenu popMenu_free;
	PopMenu popMenu_iswaimai;
	private ImageLruCacheApi lruCache;
	boolean isloading=false;
	ImageButton button1,button2,button3,button4;
	Boolean istouch=false;
	int currentcount;
	int last_item_position,firstVisibleItem , visibleItemCount,totalItemCount;  
	  
	
	
	LinkedList<LinkedList<Object>> data=new LinkedList<LinkedList<Object>>();
	LinkedList<LinkedList<Object>> databak=new LinkedList<LinkedList<Object>>();
	View loadingView,headview;
	
	class MyAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) 
		{
			if(arg1 == null)
            {
                arg1 = LayoutInflater.from(Activity_res.this).inflate(  
    	                R.layout.vlist, null);;
           }
		   
			 String imageURL = ImageHelper.getImageUrlFromUrlList(
						lruCache.getUrlList(), arg0);
			
            ImageView img = (ImageView)arg1.findViewById(R.id.img); 
            //img.setImageResource(R.drawable.coffe0);
            img.setImageBitmap(lruCache.getBitmap(imageURL, lruCache.viewCallback));
            TextView name=(TextView) arg1.findViewById(R.id.name);
            TextView info=(TextView) arg1.findViewById(R.id.info);
            name.setText((String) data.get(arg0).get(1));
            info.setText((String) data.get(arg0).get(2));
            TextView prasenum=(TextView) arg1.findViewById(R.id.praisnum);
            TextView eatnum=(TextView) arg1.findViewById(R.id.eatnum);
            prasenum.setText("点赞人数:"+(String)data.get(arg0).get(3));
            eatnum.setText("想吃人数:"+(String) data.get(arg0).get(4));
            ImageView isfree = (ImageView)arg1.findViewById(R.id.isfree); 
            ImageView iswai = (ImageView)arg1.findViewById(R.id.iswai); 
            if((Boolean)data.get(arg0).get(5)==true)
             isfree.setImageResource(R.drawable.idle1_s);
            else
            	isfree.setImageResource(R.drawable.busy);
            
            if((Boolean)data.get(arg0).get(6)==true)
                iswai.setImageResource(R.drawable.waimai_s);
               else
               	iswai.setImageBitmap(null);;
               	
               	
            return arg1;
		
			// TODO Auto-generated method stub
		}
		
	}
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);
        
        
        dialog = new ProgressDialog(this); 
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		dialog.setIndeterminate(false); 
		dialog.setMessage("加载中...");
		dialog.setCancelable(false); 
		dialog.show();
		
        
        
        loadingView=LayoutInflater.from(this).inflate(  
                R.layout.list_page_load, null); 
        
        headview=LayoutInflater.from(this).inflate(  
                R.layout.list_page_load, null); 
        
        list=(ListView) findViewById(R.id.listView1);
        list.addFooterView(loadingView);
       // list.addHeaderView(headview);
       // list.setSelection(1);
       //list.setOnScrollListener(this);
        
       
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view,
        	int position, long id) 
        	{				
				Intent intent = new Intent(Activity_res.this, RestaurantDetailActivity.class); 
				
				intent.putExtra("name", "那家小馆");
				intent.putExtra("price", "人均价格:80");
				intent.putExtra("type", "鲁菜，聚会");
				intent.putExtra("location", "酒仙桥路6号院");
				intent.putExtra("phone", "10086");
				startActivity(intent);
        	// TODO Auto-generated method stub
        	}
        	});
       
        
        
        
        lruCache = new ImageLruCacheApi();
		lruCache.LruCacheInit();
        
        
        popMenu_type = new PopMenu(this);
		popMenu_type.addItems(new String[] { "面食", "盖饭", "小吃", "快餐", "炒菜","全部"});
		// 菜单项点击监听器
		popMenu_type.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				System.out.println("下拉菜单点击" + position);
				popMenu_type.dismiss();
			}
		});
		
		popMenu_dis = new PopMenu(this);
		popMenu_dis.addItems(new String[] { "10-20", "20-40", "40以上", "全部" });
		// 菜单项点击监听器
		popMenu_dis.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				System.out.println("下拉菜单点击" + position);
				popMenu_dis.dismiss();
			}
		});
		
		
		popMenu_free = new PopMenu(this);
		popMenu_free.addItems(new String[] { "闲", "忙", "全部" });
		// 菜单项点击监听器
		popMenu_free.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) 
			{
				System.out.println("下拉菜单点击" + position);
			if(position==0)
			{
				//button3.setBackgroundResource(R.drawable.idle1_s);
				button3.setImageResource(R.drawable.is_idle);
				data.clear();
				data.addAll(databak);
				Iterator it=data.iterator();
				while(it.hasNext())
				{
					LinkedList<Object> temp=(LinkedList<Object>) it.next();
					if(!(Boolean)temp.get(5))
					{
						it.remove();
					}
				}
				
				Message msg = new Message();
				msg.what = 2;
				handler.sendMessage(msg);
				
			}
			
			else
				if(position==1)
				{
					button3.setImageResource(R.drawable.is_busy);
					data.clear();
					data.addAll(databak);
					Iterator it=data.iterator();
					while(it.hasNext())
					{
						LinkedList<Object> temp=(LinkedList<Object>) it.next();
						if((Boolean)temp.get(5))
						{
							it.remove();
						}
					}
					
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				}
				else
					if(position==2)
					{
						button3.setImageResource(R.drawable.idlebusy_s);
						data.clear();
						data.addAll(databak);
						Message msg = new Message();
						msg.what = 2;
						handler.sendMessage(msg);
					}
				popMenu_free.dismiss();
			}
		});
		
		
		
		 popMenu_iswaimai = new PopMenu(this);
			popMenu_iswaimai.addItems(new String[] { "送外卖", "无外卖","全部"});
			// 菜单项点击监听器
			popMenu_iswaimai.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					System.out.println("下拉菜单点击" + position);
					
					
					if(position==0)
					{
						//button3.setBackgroundResource(R.drawable.idle1_s);
						button4.setImageResource(R.drawable.is_deliverable);
						data.clear();
						data.addAll(databak);
						Iterator it=data.iterator();
						while(it.hasNext())
						{
							LinkedList<Object> temp=(LinkedList<Object>) it.next();
							if(!(Boolean)temp.get(6))
							{
								it.remove();
							}
						}
						
						Message msg = new Message();
						msg.what = 2;
						handler.sendMessage(msg);
						
					}
					
					else
						if(position==1)
						{
							button4.setImageResource(R.drawable.is_undeliverable);
							data.clear();
							data.addAll(databak);
							Iterator it=data.iterator();
							while(it.hasNext())
							{
								LinkedList<Object> temp=(LinkedList<Object>) it.next();
								if((Boolean)temp.get(6))
								{
									it.remove();
								}
							}
							
							Message msg = new Message();
							msg.what = 2;
							handler.sendMessage(msg);
						}
						else
							if(position==2)
							{
								button4.setImageResource(R.drawable.deleverable);
								data.clear();
								data.addAll(databak);
								Message msg = new Message();
								msg.what = 2;
								handler.sendMessage(msg);
							}
					
					
					popMenu_iswaimai.dismiss();
				}
			});

		button1 = (ImageButton) findViewById(R.id.type);
		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				popMenu_type.showAsDropDown(v);
			}
		});
        
		button2 = (ImageButton) findViewById(R.id.dis);
		button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				popMenu_dis.showAsDropDown(v);
			}
		});
		
		
		button3 = (ImageButton) findViewById(R.id.free_or_not);
		button3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				popMenu_free.showAsDropDown(v);
			}
		});
		
		button4 = (ImageButton) findViewById(R.id.iswaimai);
		button4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				popMenu_iswaimai.showAsDropDown(v);
			}
		});
		
        new MyAsyncTask().execute(" ");    
        handler = new Handler() {
    		@Override
    		public void handleMessage(Message msg) {
    			if (msg.what == 1) 
    			{
    				//list.setAdapter(adapter);
    				 adapter=new MyAdapter();
    			        list.setAdapter(adapter);
    				dialog.cancel();
    				if(list.getFooterViewsCount()==0)
    					list.addFooterView(loadingView);
    			    new MyAsyncTask_add().execute(" ");
    			}
    			else
    				if(msg.what==2)
    				{
    					
    					
    					//adapter.notifyDataSetInvalidated();
    					//adapter.notifyDataSetChanged();
    					
//    					list.setVisibility(View.GONE);
//    					adapter.notifyDataSetChanged();
//    					list.setVisibility(View.VISIBLE);
//    					isloading=false;
    					if(list.getFooterViewsCount()==1)
    						list.removeFooterView(loadingView);
    					adapter.notifyDataSetChanged();
    				}
    			}
        };
        
        //adapter.notifyDataSetChanged();
    }
    
    
    @Override
	protected void onDestroy() {
		lruCache.poolManager.stop();
		super.onDestroy();
	}
    
    class MyAsyncTask extends AsyncTask<String, Integer, String> {
		@Override
			protected String doInBackground(String... params) 
		{
				getdata();
	        	Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
				return null;
			}


			@Override
			protected void onPostExecute(String result) {

			}
			@Override
			protected void onProgressUpdate(Integer... values) {
			}	
		}
    
    
    public void getdata()
    {
    	data.clear();
    	data.addAll(databak);
    	String url;
    	
    	LinkedList<Object> temp=new LinkedList<Object>();
    	//temp.add(getBitmap("http://i1.s1.dpfile.com/pc/ge/35e8464f28043414ba51b6ced682697b(600x1000)/thumb.jpg"));
    	temp.add(null);
    	url="http://i1.s1.dpfile.com/pc/ge/35e8464f28043414ba51b6ced682697b(600x1000)/thumb.jpg";
    	lruCache.getUrlList().add(url);
    	lruCache.getBitmap(url, lruCache.viewCallback);
    	temp.add("那家小馆");
    	temp.add("鲁菜");
    	temp.add("15");
    	temp.add("10");
    	temp.add(true);
    	temp.add(false);
    	data.add(temp);
    	
    	temp=new LinkedList<Object>();
    	url="http://i2.s1.dpfile.com/pc/ge/c231bf55103916d2ca4ddfe5ffa5ff98(600x1000)/thumb.jpg";
    	//temp.add(getBitmap("http://i2.s1.dpfile.com/pc/ge/c231bf55103916d2ca4ddfe5ffa5ff98(600x1000)/thumb.jpg"));
    	temp.add(null);
    	lruCache.getUrlList().add(url);
    	lruCache.getBitmap(url, lruCache.viewCallback);
    	
    	
    	temp.add("盛福楼");
    	temp.add("自助餐");
    	temp.add("12");
    	temp.add("48");
    	temp.add(false);
    	temp.add(true);
    	data.add(temp);
    	
    	temp=new LinkedList<Object>();
    	//temp.add(getBitmap("http://i3.s1.dpfile.com/pc/ge/99941393464ff2d9f1cf16497daa1066(600x1000)/thumb.jpg"));
    	temp.add(null);
    	url="http://i3.s1.dpfile.com/pc/ge/99941393464ff2d9f1cf16497daa1066(600x1000)/thumb.jpg";
    	lruCache.getUrlList().add(url);
    	lruCache.getBitmap(url, lruCache.viewCallback);
    	
    	
    	temp.add("嘉祥一品");
    	temp.add("贵州菜");
    	temp.add("98");
    	temp.add("67");
    	temp.add(true);
    	temp.add(false);
    	data.add(temp);
    	 
    	temp=new LinkedList<Object>();
    	//temp.add(getBitmap("http://i3.s1.dpfile.com/pc/ge/350a4ba1f7368e3a12809549d35fa467(600x1000)/thumb.jpg"));
    	temp.add(null);
    	url="http://i3.s1.dpfile.com/pc/ge/350a4ba1f7368e3a12809549d35fa467(600x1000)/thumb.jpg";
    	lruCache.getUrlList().add(url);
    	lruCache.getBitmap(url, lruCache.viewCallback);
    	
    	temp.add("国宾大厦");
    	temp.add("日本菜");
    	temp.add("86");
    	temp.add("73");
    	temp.add(false);
    	temp.add(false);
    	data.add(temp);
    	
    	
    	temp=new LinkedList<Object>();
    	//temp.add(getBitmap("http://i1.s1.dpfile.com/pc/ge/3adc49bd8447de556916962420749719(600x1000)/thumb.jpg"));
    	temp.add(null);
    	url="http://i1.s1.dpfile.com/pc/ge/3adc49bd8447de556916962420749719(600x1000)/thumb.jpg";
    	lruCache.getUrlList().add(url);
    	lruCache.getBitmap(url, lruCache.viewCallback);
    	
    	temp.add("砂锅居");
    	temp.add("海鲜");
    	temp.add("16");
    	temp.add("44");
    	temp.add(true);
    	temp.add(false);
    	data.add(temp);
     
    	temp=new LinkedList<Object>();
    	//temp.add(getBitmap("http://i3.s1.dpfile.com/pc/ge/f5bbfcb0f63bf58d9e5c765bc7b8521e(600x1000)/thumb.jpg"));
    	temp.add(null);
    	url="http://i3.s1.dpfile.com/pc/ge/f5bbfcb0f63bf58d9e5c765bc7b8521e(600x1000)/thumb.jpg";
    	lruCache.getUrlList().add(url);
    	lruCache.getBitmap(url, lruCache.viewCallback);
    
    	temp.add("江边城外");
    	temp.add("海鲜");
    	temp.add("95");
    	temp.add("31");
    	temp.add(true);
    	temp.add(false);
    	data.add(temp);
	
    	temp=new LinkedList<Object>();
    	//temp.add(getBitmap("http://i1.s1.dpfile.com/pc/ge/53e7bfdbb6cd0dba17e6d8d3d749e205(600x1000)/thumb.jpg"));
    	temp.add(null);
    	url="http://i1.s1.dpfile.com/pc/ge/53e7bfdbb6cd0dba17e6d8d3d749e205(600x1000)/thumb.jpg";
    	lruCache.getUrlList().add(url);
    	lruCache.getBitmap(url, lruCache.viewCallback);
    	
    	
    	temp.add("重庆堂子");
    	temp.add("烧烤");
    	temp.add("30");
    	temp.add("31");
    	temp.add(true);
    	temp.add(true);
    	data.add(temp);
		
    	temp=new LinkedList<Object>();
    	//temp.add(getBitmap("http://i2.s1.dpfile.com/pc/ge/5a7979f280af883c0a09bfac0587c9bc(600x1000)/thumb.jpg"));
    	temp.add(null);
    	url="http://i2.s1.dpfile.com/pc/ge/5a7979f280af883c0a09bfac0587c9bc(600x1000)/thumb.jpg";
    	lruCache.getUrlList().add(url);
    	lruCache.getBitmap(url, lruCache.viewCallback);
    	
    	
    	temp.add("伍加壹烤");
    	temp.add("粤菜");
    	temp.add("30");
    	temp.add("31");
    	temp.add(true);
    	temp.add(true);
    	data.add(temp);
    	databak.clear();
    	databak.addAll(data);
    	
    	currentcount=data.size();
//    	try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
 
    	
    }
    
    
    public Bitmap getBitmap(String s_url)
    {   
    	Bitmap mBitmap = null;   
    	try {   
    	URL url = new URL(s_url);   
    	HttpURLConnection conn = (HttpURLConnection) url.openConnection();   
    	InputStream is = conn.getInputStream();   
    	mBitmap = BitmapFactory.decodeStream(is);   
    	} catch (MalformedURLException e) {   
    	e.printStackTrace();   
    	} catch (IOException e) {   
    	e.printStackTrace();   
    	}   
    	return mBitmap;   	
    }


    class MyAsyncTask_add extends AsyncTask<String, Integer, String> {
		@Override
			protected String doInBackground(String... params) 
		{
			    try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getdata();
	        	Message msg = new Message();
				msg.what = 2;
				handler.sendMessage(msg);
				//handler.removeMessages(2);
				//handler.sendEmptyMessageDelayed(2, 500);
				return null;
			}


			@Override
			protected void onPostExecute(String result) {

			}
			@Override
			protected void onProgressUpdate(Integer... values) {
			}	
		}
    
    
	
   
    
}
    
