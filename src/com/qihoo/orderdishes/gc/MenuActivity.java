package com.qihoo.orderdishes.gc;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.carrey.bitmapcachedemo.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;


public class MenuActivity extends Activity {
	ArrayList<String> infoList = new ArrayList<String>();
	ListView listView ;
	private Handler handler;
	SimpleAdapter adapter;
	String shopPhone ="18510655765";//这个可以在这个activity种从网上拉；
	int dishNumber;
	int totalPrice;
	String showInfo="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		infoList = getIntent().getStringArrayListExtra("keys");//接受到前一个activity的数据
		setContentView(R.layout.gc_menu);
		
		listView = (ListView) findViewById(R.id.ordered_dish_listview);
        TextView totalText=(TextView) findViewById(R.id.ordered_dish_text);
        Button dia=(Button) findViewById(R.id.ordered_dish_button);
        dia.setText("电话订餐");
        dia.setOnClickListener(new Button.OnClickListener(){ 
        	@Override
        	public void onClick(View v) 
        	{
        		Intent phoneIntent = new Intent("android.intent.action.CALL",Uri.parse("tel:" + shopPhone));
        		startActivity(phoneIntent);//这个activity要把通话界面隐藏以及相应的把菜品界面展示；
        	}
        	
        });
        new MyAsyncTask().execute(" ");
        handler = new Handler() {
    		@Override
    		public void handleMessage(Message msg) {
    			if (msg.what == 1) 
    			{
    				listView.setAdapter(adapter); 
    			}
    			}
        };
        
        Map<String,String> totalTmp =  getTotal(getData());
        showInfo = "总点菜数:"+totalTmp.get("dishNumber")+"  总价格:"+totalTmp.get("totalPrice");
        totalText.setText(showInfo);//这里进行一个展示；
        
	}
	
	Map<String,String> getTotal( List<Map<String, Object>> listMap){
		Map<String,String> total = new HashMap<String,String>();
		int count = listMap.size();
		total.put("dishNumber", count+"");
		int priceSum = 0;
		for(int i = 0;i<listMap.size();++i){
			String tmpPrice =listMap.get(i).get("price").toString();
			int index = tmpPrice.indexOf('￥');
			String priceNmber = tmpPrice.substring(index+1, tmpPrice.length()-1);
			priceSum += Integer.parseInt(priceNmber);
		}
		total.put("totalPrice", priceSum+"");
		return total;
	}//计算价钱
	
	class MyAsyncTask extends AsyncTask<String, Integer, String> {
		@Override
			protected String doInBackground(String... params) 
		{
			adapter = new SimpleAdapter(MenuActivity.this,getData(),R.layout.gc_visit,new String[]{"name","price","img"},new int[]{R.id.ordered_dish_textview_name,R.id.ordered_dish_textview_price,R.id.ordered_dish_img});        
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
			if(infoList.size()==0){
				return null;
			}else{
		    	return getitemdata(infoList);				
			}
		}
	    
	    public Bitmap getBitmap(String s_url)
	    {   //可以用于自己第一次打开菜单界面拉数据使用；多线程的拉数据；
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
	    
	    List<Map<String, Object>> getitemdata(ArrayList<String> infoList)
	    {
	    	List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
	    	ArrayList<String> stringListTmp = new ArrayList<String>();
	    	stringListTmp = infoList;
	    	if(stringListTmp.size()<=0){
	    		Toast.makeText(MenuActivity.this, String.format("亲，没有选菜哦！"),Toast.LENGTH_SHORT).show();
	    		return null;
	    	}else{
	    		for(int i=0;i<stringListTmp.size();++i){
	    			String singleDish = stringListTmp.get(i);
	    			int pos1 = singleDish.indexOf(':');
	    			int pos2 = singleDish.indexOf('￥', pos1);
	    			String dishImagePath = singleDish.substring(0, pos1);
	    			String dishName = singleDish.substring(pos1+1,pos2);
	    			String dishPrice = singleDish.substring(pos2+1, singleDish.length());
	    			Map<String,Object> map = new HashMap<String,Object>();
	    			map.put("name", dishName);
	    			map.put("price", "￥"+dishPrice);
	    			map.put("img", dishImagePath);
	    			listMap.add(map);
	    		}
		    	return listMap;
	    	}
	    	
//	    	List<Map<String, Object>> listobject = new LinkedList<Map<String, Object>>();
//			Map<String, Object> map = new HashMap<String, Object>();
//			String name="经典沙茶牛肉";
//			String price="价格：45.0";
//			map.put("name", name);
//			map.put("price", price);
//			map.put("img",getBitmap("http://i1.s1.dpfile.com/pc/ge/35e8464f28043414ba51b6ced682697b(600x1000)/thumb.jpg"));
//			listobject.add(map);
//			map = new HashMap<String, Object>();
//			map.put("name", "牛筋萝卜羹(红烧)");
//			map.put("price", "价格：58.0");
//			map.put("img",getBitmap("http://i2.s1.dpfile.com/pc/ge/c231bf55103916d2ca4ddfe5ffa5ff98(600x1000)/thumb.jpg"));
//			listobject.add(map);
//			map = new HashMap<String, Object>();
//			map.put("name", "极品糖醋小排");
//			map.put("price", "价格：55.0");
//			map.put("img",getBitmap("http://i3.s1.dpfile.com/pc/ge/99941393464ff2d9f1cf16497daa1066(600x1000)/thumb.jpg"));
//			listobject.add(map);
//			map = new HashMap<String, Object>();
//			map.put("name", "江南菌菇素食");
//			map.put("price", "价格：25.0");
//			map.put("img",getBitmap("http://i3.s1.dpfile.com/pc/ge/350a4ba1f7368e3a12809549d35fa467(600x1000)/thumb.jpg"));
//			listobject.add(map);
//			map = new HashMap<String, Object>();
//			map.put("name", "清炒生拆蟹粉");
//			map.put("price", "价格：70.0");
//			map.put("img",getBitmap("http://i1.s1.dpfile.com/pc/ge/3adc49bd8447de556916962420749719(600x1000)/thumb.jpg"));
//			listobject.add(map);		
//			return listobject;
		
	    }
	
}
