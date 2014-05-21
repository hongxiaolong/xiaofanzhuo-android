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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.carrey.bitmapcachedemo.R;
import com.qihoo.xiaofanzhuo.restaurantdetailactivity.RestaurantDetailActivity;

import android.support.v4.app.Fragment;
import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;
import android.os.Build;

public class Activity_res extends Activity 
{

	ListView list;
	private Handler handler;
	SimpleAdapter adapter;
	PopMenu popMenu_type;
	PopMenu popMenu_dis;
	PopMenu popMenu_free;
	
	LinkedList<LinkedList<String>> data=new LinkedList<LinkedList<String>>();
	
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);
        
        LinkedList<String> temp=new LinkedList<String>();
        temp.add("那家小馆");
        temp.add("人均：80");
        temp.add("鲁菜");
        temp.add("大山子北里301号");
        temp.add("64736868");
        data.add(temp);
        
        temp=new LinkedList<String>();
        temp.add("盛福楼");
        temp.add("人均：80");
        temp.add("自助餐");
        temp.add("大山子北里301号");
        temp.add("64736868");
        data.add(temp);
        
        temp=new LinkedList<String>();
        temp.add("嘉祥一品");
        temp.add("人均：80");
        temp.add("贵州菜");
        temp.add("大山子北里301号");
        temp.add("64736868");
        data.add(temp);
        
        temp=new LinkedList<String>();
        temp.add("国宾大厦");
        temp.add("人均：80");
        temp.add("日本菜");
        temp.add("大山子北里301号");
        temp.add("64736868");
        data.add(temp);
        
        temp=new LinkedList<String>();
        temp.add("砂锅居");
        temp.add("人均：80");
        temp.add("海鲜");
        temp.add("大山子北里301号");
        temp.add("64736868");
        data.add(temp);
 
        temp=new LinkedList<String>();
        temp.add("江边城外");
        temp.add("人均：80");
        temp.add("海鲜");
        temp.add("大山子北里301号");
        temp.add("64736868");
        data.add(temp);
        
        temp=new LinkedList<String>();
        temp.add("重庆堂私");
        temp.add("人均：80");
        temp.add("烧烤");
        temp.add("大山子北里301号");
        temp.add("64736868");
        data.add(temp);
        
        temp=new LinkedList<String>();
        temp.add("伍加壹烤");
        temp.add("人均：80");
        temp.add("粤菜");
        temp.add("大山子北里301号");
        temp.add("64736868");
        data.add(temp);
        
        list=(ListView) findViewById(R.id.listView1);
        
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view,
        	int position, long id) 
        	{				
				Intent intent = new Intent(Activity_res.this, RestaurantDetailActivity.class); 
				intent.putExtra("name", data.get(position).get(0)); 
				intent.putExtra("price",data.get(position).get(1)); 
				intent.putExtra("type",data.get(position).get(2)); 
				intent.putExtra("location",data.get(position).get(3));  
				intent.putExtra("phone",data.get(position).get(4)); 
				startActivity(intent);
        	// TODO Auto-generated method stub
        	}
        	});
       
        popMenu_type = new PopMenu(this);
		popMenu_type.addItems(new String[] { "川菜", "粤菜", "湘菜", "港式", "广式","全部"});
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
		popMenu_dis.addItems(new String[] { "100m", "500m", "1000m", "2000m", "全部" });
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
					long id) {
				
				if(position==0)
				{
					
					 class MyAsyncTask2 extends AsyncTask<String, Integer, String> {
							@Override
								protected String doInBackground(String... params) 
							{
								adapter = new SimpleAdapter(Activity_res.this,getfreeData(),R.layout.vlist,new String[]{"name","isfree","info","iswai","praisenum","eatnum","img"},new int[]{R.id.name,R.id.isfree,R.id.info,R.id.iswai,R.id.praisnum,R.id.eatnum,R.id.img});        
						        adapter.setViewBinder(new ViewBinder() {   
						        	public boolean setViewValue(View view, Object data,   
						        	String textRepresentation) {   
						        	//判断是否为我们要处理的对象   
						        	if(view instanceof ImageView && data instanceof Bitmap){   
						        	ImageView iv = (ImageView) view;   
						        	iv.setImageBitmap((Bitmap) data);   
						        	return true;   
						        	}else   
						        	return false;   
						        	}   
						        	});          
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
					
				        new MyAsyncTask2().execute(" ");   
					 
				}
				
				else
					if(position==1)
						{
							
							 class MyAsyncTask3 extends AsyncTask<String, Integer, String> {
									@Override
										protected String doInBackground(String... params) 
									{
										adapter = new SimpleAdapter(Activity_res.this,getbusyData(),R.layout.vlist,new String[]{"name","isfree","info","iswai","praisenum","eatnum","img"},new int[]{R.id.name,R.id.isfree,R.id.info,R.id.iswai,R.id.praisnum,R.id.eatnum,R.id.img});        
								        adapter.setViewBinder(new ViewBinder() {   
								        	public boolean setViewValue(View view, Object data,   
								        	String textRepresentation) {   
								        	//判断是否为我们要处理的对象   
								        	if(view instanceof ImageView && data instanceof Bitmap){   
								        	ImageView iv = (ImageView) view;   
								        	iv.setImageBitmap((Bitmap) data);   
								        	return true;   
								        	}else   
								        	return false;   
								        	}   
								        	});          
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
							
						        new MyAsyncTask3().execute(" ");   
							 
						}
					else
						{
							
						        new MyAsyncTask().execute(" ");   
							 
						}
				System.out.println("下拉菜单点击" + position);
				popMenu_free.dismiss();
			}
		});
		
		

		ImageButton button1 = (ImageButton) findViewById(R.id.type);
		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				popMenu_type.showAsDropDown(v);
			}
		});
        
		ImageButton button2 = (ImageButton) findViewById(R.id.dis);
		button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				popMenu_dis.showAsDropDown(v);
			}
		});
		
		
		ImageButton button3 = (ImageButton) findViewById(R.id.free_or_not);
		button3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				popMenu_free.showAsDropDown(v);
			}
		});
		
        new MyAsyncTask().execute(" ");    
        handler = new Handler() {
    		@Override
    		public void handleMessage(Message msg) {
    			if (msg.what == 1) 
    			{
    				list.setAdapter(adapter); 
    			}
    			}
        };
        
        
    }
    
    class MyAsyncTask extends AsyncTask<String, Integer, String> {
		@Override
			protected String doInBackground(String... params) 
		{
			adapter = new SimpleAdapter(Activity_res.this,getData(),R.layout.vlist,new String[]{"name","isfree","info","iswai","praisenum","eatnum","img"},new int[]{R.id.name,R.id.isfree,R.id.info,R.id.iswai,R.id.praisnum,R.id.eatnum,R.id.img});        
	        adapter.setViewBinder(new ViewBinder() {   
	        	public boolean setViewValue(View view, Object data,   
	        	String textRepresentation) {   
	        	//判断是否为我们要处理的对象   
	        	if(view instanceof ImageView && data instanceof Bitmap){   
	        	ImageView iv = (ImageView) view;   
	        	iv.setImageBitmap((Bitmap) data);   
	        	return true;   
	        	}else   
	        	return false;   
	        	}   
	        	});          
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
    private List<Map<String, Object>> getData()
    {
//    	List<Map<String, Object>> listobject = new LinkedList<Map<String, Object>>();
//		Map<String, Object> map = new HashMap<String, Object>();
//		String name="那家小馆";
//		String info="鲁菜";
//		String isfree="忙";
//		String iswai="不外卖";
//		String praisenum="点赞人数：24";
//		String eatnum="想吃人数：62";
//		map.put("name", name);
//		map.put("info", info);
//		map.put("isfree", isfree);
//		map.put("iswai", iswai);
//		map.put("praisenum", praisenum);
//		map.put("eatnum", eatnum);
//		map.put("img",getBitmap());
//		listobject.add(map);
//		return listobject;
    	return getitemdata(1);
	}
    
    public List<Map<String, Object>> getbusyData()
    {
    	List<Map<String, Object>> listobject = new LinkedList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		String name="那家小馆";
		String info="鲁菜";
		String isfree="忙";
		String iswai="R.drawable.deliverable_m";
		String praisenum="点赞人数：24";
		String eatnum="想吃人数：62";
		map.put("name", name);
		map.put("info", info);
		map.put("isfree", R.drawable.busy);
		map.put("iswai", R.drawable.waimai_s);
		map.put("praisenum", praisenum);
		map.put("eatnum", eatnum);
		map.put("img",getBitmap("http://i1.s1.dpfile.com/pc/ge/35e8464f28043414ba51b6ced682697b(600x1000)/thumb.jpg"));
		listobject.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "盛福楼");
		map.put("info", "自助餐");
		map.put("isfree", R.drawable.busy);
		map.put("iswai", R.drawable.waimai_s);
		map.put("praisenum", "点赞人数：12");
		map.put("eatnum", "想吃人数：48");
		map.put("img",getBitmap("http://i2.s1.dpfile.com/pc/ge/c231bf55103916d2ca4ddfe5ffa5ff98(600x1000)/thumb.jpg"));
		listobject.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "国宾大厦");
		map.put("info", "日本菜");
		map.put("isfree", R.drawable.busy);
		map.put("iswai", null);
		map.put("praisenum", "点赞人数：86");
		map.put("eatnum", "想吃人数：73");
		map.put("img",getBitmap("http://i3.s1.dpfile.com/pc/ge/350a4ba1f7368e3a12809549d35fa467(600x1000)/thumb.jpg"));
		listobject.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "重庆堂私");
		map.put("info", "烧烤");
		map.put("isfree", R.drawable.busy);
		map.put("iswai", null);
		map.put("praisenum", "点赞人数：30");
		map.put("eatnum", "想吃人数：30");
		map.put("img",getBitmap("http://i1.s1.dpfile.com/pc/ge/53e7bfdbb6cd0dba17e6d8d3d749e205(600x1000)/thumb.jpg"));
		listobject.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "伍加壹烤");
		map.put("info", "粤菜");
		map.put("isfree", R.drawable.busy);
		map.put("iswai", null);
		map.put("praisenum", "点赞人数：94");
		map.put("eatnum", "想吃人数：6");
		map.put("img",getBitmap("http://i2.s1.dpfile.com/pc/ge/5a7979f280af883c0a09bfac0587c9bc(600x1000)/thumb.jpg"));
		listobject.add(map);
		
		return listobject;
    }
    
    
    public List<Map<String, Object>> getfreeData()
    {
    	List<Map<String, Object>> listobject = new LinkedList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map = new HashMap<String, Object>();
		map.put("name", "嘉祥一品");
		map.put("info", "贵州菜");
		map.put("isfree", R.drawable.idle1_s);
		map.put("iswai", null);
		map.put("praisenum", "点赞人数：98");
		map.put("eatnum", "想吃人数：67");
		map.put("img",getBitmap("http://i3.s1.dpfile.com/pc/ge/99941393464ff2d9f1cf16497daa1066(600x1000)/thumb.jpg"));
		listobject.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "砂锅居");
		map.put("info", "海鲜");
		map.put("isfree", R.drawable.idle1_s);
		map.put("iswai", R.drawable.waimai_s);
		map.put("praisenum", "点赞人数：16");
		map.put("eatnum", "想吃人数：44");
		map.put("img",getBitmap("http://i1.s1.dpfile.com/pc/ge/3adc49bd8447de556916962420749719(600x1000)/thumb.jpg"));
		listobject.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "江边城外");
		map.put("info", "海鲜");
		map.put("isfree", R.drawable.idle1_s);
		map.put("iswai", R.drawable.waimai_s);
		map.put("praisenum", "点赞人数：95");
		map.put("eatnum", "想吃人数：31");
		map.put("img",getBitmap("http://i3.s1.dpfile.com/pc/ge/f5bbfcb0f63bf58d9e5c765bc7b8521e(600x1000)/thumb.jpg"));
		listobject.add(map);
		listobject.add(map);
		
		return listobject;
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
    
    List<Map<String, Object>> getitemdata(int num)
    {
    	List<Map<String, Object>> listobject = new LinkedList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		String name="那家小馆";
		String info="鲁菜";
		String isfree="忙";
		String iswai="R.drawable.deliverable_m";
		String praisenum="点赞人数：24";
		String eatnum="想吃人数：62";
		map.put("name", name);
		map.put("info", info);
		map.put("isfree", R.drawable.busy);
		map.put("iswai", R.drawable.waimai_s);
		map.put("praisenum", praisenum);
		map.put("eatnum", eatnum);
		map.put("img",getBitmap("http://i1.s1.dpfile.com/pc/ge/35e8464f28043414ba51b6ced682697b(600x1000)/thumb.jpg"));
		listobject.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "盛福楼");
		map.put("info", "自助餐");
		map.put("isfree", R.drawable.busy);
		map.put("iswai", R.drawable.waimai_s);
		map.put("praisenum", "点赞人数：12");
		map.put("eatnum", "想吃人数：48");
		map.put("img",getBitmap("http://i2.s1.dpfile.com/pc/ge/c231bf55103916d2ca4ddfe5ffa5ff98(600x1000)/thumb.jpg"));
		listobject.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "嘉祥一品");
		map.put("info", "贵州菜");
		map.put("isfree", R.drawable.idle1_s);
		map.put("iswai", null);
		map.put("praisenum", "点赞人数：98");
		map.put("eatnum", "想吃人数：67");
		map.put("img",getBitmap("http://i3.s1.dpfile.com/pc/ge/99941393464ff2d9f1cf16497daa1066(600x1000)/thumb.jpg"));
		listobject.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "国宾大厦");
		map.put("info", "日本菜");
		map.put("isfree", R.drawable.busy);
		map.put("iswai", null);
		map.put("praisenum", "点赞人数：86");
		map.put("eatnum", "想吃人数：73");
		map.put("img",getBitmap("http://i3.s1.dpfile.com/pc/ge/350a4ba1f7368e3a12809549d35fa467(600x1000)/thumb.jpg"));
		listobject.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "砂锅居");
		map.put("info", "海鲜");
		map.put("isfree", R.drawable.idle1_s);
		map.put("iswai", R.drawable.waimai_s);
		map.put("praisenum", "点赞人数：16");
		map.put("eatnum", "想吃人数：44");
		map.put("img",getBitmap("http://i1.s1.dpfile.com/pc/ge/3adc49bd8447de556916962420749719(600x1000)/thumb.jpg"));
		listobject.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "江边城外");
		map.put("info", "海鲜");
		map.put("isfree", R.drawable.idle1_s);
		map.put("iswai", R.drawable.waimai_s);
		map.put("praisenum", "点赞人数：95");
		map.put("eatnum", "想吃人数：31");
		map.put("img",getBitmap("http://i3.s1.dpfile.com/pc/ge/f5bbfcb0f63bf58d9e5c765bc7b8521e(600x1000)/thumb.jpg"));
		listobject.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "重庆堂私");
		map.put("info", "烧烤");
		map.put("isfree", R.drawable.busy);
		map.put("iswai", null);
		map.put("praisenum", "点赞人数：30");
		map.put("eatnum", "想吃人数：30");
		map.put("img",getBitmap("http://i1.s1.dpfile.com/pc/ge/53e7bfdbb6cd0dba17e6d8d3d749e205(600x1000)/thumb.jpg"));
		listobject.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "伍加壹烤");
		map.put("info", "粤菜");
		map.put("isfree", R.drawable.busy);
		map.put("iswai", null);
		map.put("praisenum", "点赞人数：94");
		map.put("eatnum", "想吃人数：6");
		map.put("img",getBitmap("http://i2.s1.dpfile.com/pc/ge/5a7979f280af883c0a09bfac0587c9bc(600x1000)/thumb.jpg"));
		listobject.add(map);
		
		return listobject;
	
    }
    
}
    
