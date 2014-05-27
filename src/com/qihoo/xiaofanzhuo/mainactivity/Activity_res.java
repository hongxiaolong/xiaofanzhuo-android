package com.qihoo.xiaofanzhuo.mainactivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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

public class Activity_res extends Activity implements OnScrollListener 
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
	int page=1;
	
	
	LinkedList<String> dataforres=new LinkedList<String>();
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
       list.setOnScrollListener(this);
        
       
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view,
        	int position, long id) 
        	{				
				Intent intent = new Intent(Activity_res.this, RestaurantDetailActivity.class); 
				
				intent.putExtra("data",dataforres.get(position));
				startActivity(intent);
        	// TODO Auto-generated method stub
        	}
        	});
       
        
        
        
        lruCache = new ImageLruCacheApi();
		lruCache.LruCacheInit();
        
        
        popMenu_type = new PopMenu(this);
		popMenu_type.addItems(new String[] { "烧烤", "贵州菜", "西餐", "鲁菜", "清真菜","全部"});
		// 菜单项点击监听器
		popMenu_type.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				System.out.println("下拉菜单点击" + position);
				
				if(position==0)
				{
					//button3.setBackgroundResource(R.drawable.idle1_s);
					//button3.setImageResource(R.drawable.is_idle);
					data.clear();
					data.addAll(databak);
					Iterator it=data.iterator();
					while(it.hasNext())
					{
						LinkedList<Object> temp=(LinkedList<Object>) it.next();
						if(!((String)temp.get(2)).equals("烧烤"))
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
						//button3.setBackgroundResource(R.drawable.idle1_s);
						//button3.setImageResource(R.drawable.is_idle);
						data.clear();
						data.addAll(databak);
						Iterator it=data.iterator();
						while(it.hasNext())
						{
							LinkedList<Object> temp=(LinkedList<Object>) it.next();
							if(!((String)temp.get(2)).equals("贵州菜"))
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
							//button3.setBackgroundResource(R.drawable.idle1_s);
							//button3.setImageResource(R.drawable.is_idle);
							data.clear();
							data.addAll(databak);
							Iterator it=data.iterator();
							while(it.hasNext())
							{
								LinkedList<Object> temp=(LinkedList<Object>) it.next();
								if(!((String)temp.get(2)).equals("西餐"))
								{
									it.remove();
								}
							}
							
							Message msg = new Message();
							msg.what = 2;
							handler.sendMessage(msg);
							
						}
						else
							if(position==3)
							{
								//button3.setBackgroundResource(R.drawable.idle1_s);
								//button3.setImageResource(R.drawable.is_idle);
								data.clear();
								data.addAll(databak);
								Iterator it=data.iterator();
								while(it.hasNext())
								{
									LinkedList<Object> temp=(LinkedList<Object>) it.next();
									if(!((String)temp.get(2)).equals("鲁菜"))
									{
										it.remove();
									}
								}
								
								Message msg = new Message();
								msg.what = 2;
								handler.sendMessage(msg);
								
							}
							else
								if(position==4)
								{
									//button3.setBackgroundResource(R.drawable.idle1_s);
									//button3.setImageResource(R.drawable.is_idle);
									data.clear();
									data.addAll(databak);
									Iterator it=data.iterator();
									while(it.hasNext())
									{
										LinkedList<Object> temp=(LinkedList<Object>) it.next();
										if(!((String)temp.get(2)).equals("清真菜"))
										{
											it.remove();
										}
									}
									
									Message msg = new Message();
									msg.what = 2;
									handler.sendMessage(msg);
									
								}
								else
								if(position==5)
								{
									data.clear();
									data.addAll(databak);
									Message msg = new Message();
									msg.what = 2;
									handler.sendMessage(msg);
								}
				
				
				popMenu_type.dismiss();
			}
		});
		
		popMenu_dis = new PopMenu(this);
		popMenu_dis.addItems(new String[] { "<50", "50-100", ">100", "全部" });
		// 菜单项点击监听器
		popMenu_dis.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				if(position==0)
				{
					//button3.setBackgroundResource(R.drawable.idle1_s);
					//button3.setImageResource(R.drawable.is_idle);
					data.clear();
					data.addAll(databak);
					Iterator it=data.iterator();
					while(it.hasNext())
					{
						LinkedList<Object> temp=(LinkedList<Object>) it.next();
						if(Integer.parseInt((String) temp.get(8))>=50)
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
						//button3.setBackgroundResource(R.drawable.idle1_s);
						//button3.setImageResource(R.drawable.is_idle);
						data.clear();
						data.addAll(databak);
						Iterator it=data.iterator();
						while(it.hasNext())
						{
							LinkedList<Object> temp=(LinkedList<Object>) it.next();
							if(Integer.parseInt((String) temp.get(8))<50||Integer.parseInt((String) temp.get(8))>100)
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
							//button3.setBackgroundResource(R.drawable.idle1_s);
							//button3.setImageResource(R.drawable.is_idle);
							data.clear();
							data.addAll(databak);
							Iterator it=data.iterator();
							while(it.hasNext())
							{
								LinkedList<Object> temp=(LinkedList<Object>) it.next();
								if(Integer.parseInt((String) temp.get(8))<100)
								{
									it.remove();
								}
							}
							
							Message msg = new Message();
							msg.what = 2;
							handler.sendMessage(msg);
							
						}
						else
							if(position==3)
							{
							
								data.clear();
								data.addAll(databak);
								Message msg = new Message();
								msg.what = 2;
								handler.sendMessage(msg);
							}
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
    				//new MyAsyncTask_add().execute(" ");
    			}
    			else
    				if(msg.what==2)
    				{
    					
    					
    					//adapter.notifyDataSetInvalidated();
    					//adapter.notifyDataSetChanged();
    					
//    					list.setVisibility(View.GONE);
//    					adapter.notifyDataSetChanged();
//    					list.setVisibility(View.VISIBLE);
    					adapter=new MyAdapter();
    					list.setAdapter(adapter);
    					isloading=false;
    					if(list.getFooterViewsCount()==1)
    						list.removeFooterView(loadingView);
    					//adapter.notifyDataSetChanged();
    				}
    				else
    					if(msg.what==4)
    					{
    						dialog.cancel();
    						AlertDialog.Builder builder = new Builder(Activity_res.this);
    						builder.setMessage("网络无法连接");
    						builder.setTitle("提示");
    						builder.setPositiveButton("确认", new OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) 
								{

		    						dialog.dismiss();
		    						Activity_res.this.finish();	
								}
    						
    						}
    						);

    					}
    			}
        };
        
        
    }
    
    
    @Override
	protected void onDestroy() {
    	try
    	{
		lruCache.poolManager.stop();
    	}
    	catch(Exception e)
    	{
    		
    	}
    	finally
    	{
		super.onDestroy();
    	}
	}
    
    class MyAsyncTask extends AsyncTask<String, Integer, String> {
		@Override
			protected String doInBackground(String... params) 
		{
				boolean ok=getShowdata();
				//if(ok)
				//{
	        	Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
				//}
				//else
				//{
//					Message msg = new Message();
//					msg.what = 4;
//					handler.sendMessage(msg);
				//}
				return null;
			}


			@Override
			protected void onPostExecute(String result) {

			}
			@Override
			protected void onProgressUpdate(Integer... values) {
			}	
		}
    
    public boolean getShowdata()
    {
    	data.clear();
    	data.addAll(databak);
 
    	String target="";
		target = "http://182.92.80.201/2.php?content="+URLEncoder.encode("GetShopInfoByShopIDs____1_10");	//要访问的URL地址
		//target = "http://182.92.80.201/2.php?content="
			//	+base64(content.getText().toString().trim());	//要访问的URL地址
		URL url;
		try {
			url = new URL(target);
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();	//创建一个HTTP连接
			InputStreamReader in = new InputStreamReader(
					urlConn.getInputStream()); // 获得读取的内容
			BufferedReader buffer = new BufferedReader(in); // 获取输入流对象
			String inputLine = null;
			//通过循环逐行读取输入流中的内容
			while ((inputLine = buffer.readLine()) != null) 
			{
				dataforres.add(inputLine);
				String[] tokens=inputLine.split("	");
				LinkedList<Object> temp=new LinkedList<Object>();
		    	//temp.add(getBitmap("http://i1.s1.dpfile.com/pc/ge/35e8464f28043414ba51b6ced682697b(600x1000)/thumb.jpg"));
		    	
				boolean isfree=false;
				if(tokens[2].equals("空闲"))
					isfree=true;
				System.out.println("是否空闲："+isfree);
				System.out.println("图片地址："+tokens[3].substring(1, tokens[3].length()-1));
				String phone=tokens[6];
				if(phone.length()==0)
					phone="没有";
				else
				if(phone.contains("没有"))
				System.out.println("电话空");
				else
					System.out.println("电话："+phone.substring(2,phone.length()-1));
				boolean iswai=false;
				if(!(tokens[7].contains("不")))
					iswai=true;
					System.out.println("是否外卖："+iswai);
					System.out.println("想吃人数："+tokens[8]);
					System.out.println("点赞人数"+tokens[9]);
					System.out.println("人均价格："+tokens[10]);
					System.out.println("餐厅类型："+tokens[14]);
				
				temp.add(null);
		    	lruCache.getUrlList().add(tokens[3].substring(1, tokens[3].length()-1));
		    	lruCache.getBitmap(tokens[3].substring(1, tokens[3].length()-1), lruCache.viewCallback);
		    	temp.add(tokens[1].substring(1, tokens[1].length()-1));
		    	temp.add(tokens[14]); //类型
		    	temp.add(tokens[8]);  //想吃 
		    	temp.add(tokens[9]);  //点赞
		    	temp.add(isfree);     
		    	temp.add(iswai);
		    	temp.add(phone);
		    	if(tokens[10].startsWith("'"))
		    		temp.add(tokens[10].subSequence(1, tokens[10].length()-1));
		    	else		    		
		    	temp.add(tokens[10]); //价格
		    	data.add(temp);
						
			}
			in.close();	//关闭字符输入流对象
			urlConn.disconnect();	//断开连接
			
		} catch (MalformedURLException e) 
		{
			Log.d("sdfsafsdafsdafsadfsda","sdfsdfsdfsdafsdfsafsdaf");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			Log.d("sdfsafsdafsdafsadfsda","sdfsdfsdfsdafsdfsafsdaf");
			e.printStackTrace();
			return false;
		}
		
		
		databak.clear();
    	databak.addAll(data);
    	
    	currentcount=data.size();
    	return true;
		
    }
    
    
    public void getalldata()
    {
    	try {
			Thread.sleep(1500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	data.clear();
    	data.addAll(databak);
 
    	String target="";
    	if(page>10)
    		return;
		target = "http://182.92.80.201/2.php?content="+URLEncoder.encode("GetShopInfoByShopIDs____"+page*10+"_"+(page+1)*10);	//要访问的URL地址
		page++;
		//target = "http://182.92.80.201/2.php?content="
			//	+base64(content.getText().toString().trim());	//要访问的URL地址
		URL url;
		try {
			url = new URL(target);
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();	//创建一个HTTP连接
			InputStreamReader in = new InputStreamReader(
					urlConn.getInputStream()); // 获得读取的内容
			BufferedReader buffer = new BufferedReader(in); // 获取输入流对象
			String inputLine = null;
			//通过循环逐行读取输入流中的内容
			while ((inputLine = buffer.readLine()) != null) 
			{
				dataforres.add(inputLine);
				String[] tokens=inputLine.split("	");
				LinkedList<Object> temp=new LinkedList<Object>();
		    	//temp.add(getBitmap("http://i1.s1.dpfile.com/pc/ge/35e8464f28043414ba51b6ced682697b(600x1000)/thumb.jpg"));
		    	System.out.println(tokens[1].substring(1, tokens[1].length()-1));
				boolean isfree=false;
				if(tokens[2].equals("空闲"))
					isfree=true;
				System.out.println("是否空闲："+isfree);
				System.out.println("图片地址："+tokens[3].substring(1, tokens[3].length()-1));
				String phone=tokens[6];
				if(phone.length()==0)
					phone="没有";
				else
				if(phone.contains("没有"))
				System.out.println("电话空");
				else
					try{
					
						System.out.println("电话："+phone.substring(2,phone.length()-1));
					}
				catch(Exception e)
				{
					
				}
				boolean iswai=false;
				if(!(tokens[7].contains("不")))
					iswai=true;
					System.out.println("是否外卖："+iswai);
					System.out.println("想吃人数："+tokens[8]);
					System.out.println("点赞人数"+tokens[9]);
					System.out.println("人均价格："+tokens[10]);
					System.out.println("餐厅类型："+tokens[14]);
				
				temp.add(null);
		    	lruCache.getUrlList().add(tokens[3].substring(1, tokens[3].length()-1));
		    	lruCache.getBitmap(tokens[3].substring(1, tokens[3].length()-1), lruCache.viewCallback);
		    	temp.add(tokens[1].substring(1, tokens[1].length()-1));
		    	temp.add(tokens[14]);
		    	temp.add(tokens[8]);
		    	temp.add(tokens[9]);
		    	temp.add(isfree);
		    	temp.add(iswai);
		    	temp.add(phone);
		    	if(tokens[10].startsWith("'"))
		    		temp.add(tokens[10].subSequence(1, tokens[10].length()-1));
		    	else		    		
		    	temp.add(tokens[10]);
		    	data.add(temp);
						
			}
			in.close();	//关闭字符输入流对象
			urlConn.disconnect();	//断开连接
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Log.d("lsdfjlsdfj","sdfsdflksjadlfkjsdakf");
		
		
		databak.clear();
    	databak.addAll(data);
    	
    	currentcount=data.size();
    }
   
    class MyAsyncTask_add extends AsyncTask<String, Integer, String> {
		@Override
			protected String doInBackground(String... params) 
		{
				getalldata();
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
    
    
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,  
            int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
		if(page>10)
			return;
		this.last_item_position=firstVisibleItem + visibleItemCount - 1;
		this.firstVisibleItem=firstVisibleItem;
		this.visibleItemCount=visibleItemCount;
		this.totalItemCount=totalItemCount;
		
		 if (last_item_position == totalItemCount - 2) 
	        {
	        	if(!isloading)
	        	{
	        		isloading=true;
	        		if(list.getFooterViewsCount()==0)
	        		list.addFooterView(loadingView);
	        		new MyAsyncTask_add().execute("");
	        	}
	        }
		
		
	}


	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) 
	{
		// TODO Auto-generated method stub
		
		
	}
    
   
    
}
    
