package com.qihoo.xiaofanzhuo.restaurantdetailactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class PreferencesService {

    private Context mContext; 
    private String mPreferences;
    private List<String> mPreferencesNames;
    private List<String> mPreferencesUrls;
    private List<String> mPreferencesPrices;
 
    public PreferencesService(Context context, String preferences) { 
        this.mContext = context; 
        this.mPreferences = preferences;
        this.mPreferencesNames = new ArrayList<String>();
        this.mPreferencesUrls = new ArrayList<String>();
        this.mPreferencesPrices = new ArrayList<String>();
    } 
 
    /**
     * 保存参数
     * @param name  菜品名称
     * @param url   菜品图片  
     * @param price   菜品价格
     */ 
    public void saveToPerferences(String name, String url, String price) { 
        //获得SharedPreferences对象  
        SharedPreferences preferences = mContext.getSharedPreferences(mPreferences, mContext.MODE_PRIVATE); 
        Editor editor = preferences.edit(); 
        editor.putString("name", name); 
        editor.putString("url", url); 
        editor.putString("price", price); 
        editor.commit(); 
    } 
    
    /**
     * 检测参数是否存在
     * @param key
     * @return Returns true if the preference exists in the preferences, otherwise false. 
     */ 
    public boolean isKeyContains (String key)
    { 	
    	SharedPreferences preferences = mContext.getSharedPreferences(mPreferences, mContext.MODE_PRIVATE); 
		return preferences.contains(key);
    	
    }
    
    /**
     * 追加参数，在函数中已经执行了getPerferences()，直接在后面追加即可
     * @param name  菜品名称
     * @param url   菜品图片  
     * @param price   菜品价格
     */ 
    public void addToPerferences(String name, String url, String price) { 
        //获得SharedPreferences对象  
        SharedPreferences preferences = mContext.getSharedPreferences(mPreferences, mContext.MODE_PRIVATE); 
        Map<String, String> params = getPerferences();
        Editor editor = preferences.edit(); 
        editor.putString("name", params.get("name")+ "_" + name); 
        editor.putString("url", params.get("url")+ "_" + url); 
        editor.putString("price", params.get("price")+ "_" + price); 
        editor.commit(); 
    } 
 
    /**
     * 获取各项参数
     * @return 参数存在Map<String, String>中
     */ 
    public Map<String, String> getPerferences() { 
        Map<String, String> params = new HashMap<String, String>(); 
        SharedPreferences preferences = mContext.getSharedPreferences(mPreferences, Context.MODE_PRIVATE ); 
        params.put("name", preferences.getString("name", "")); 
        params.put("url", preferences.getString("url", "")); 
        params.put("price", preferences.getString("price", "")); 
        return params; 
    } 
    
    /**
     * @param 存放参数的Map<String, String>，需先调用getPerferences()
     * @return void, 将Map中对应数据切割后存放于this.mPreferencesNames,this.mPreferencesUrls,	this.mPreferencesPrices
     */ 
    public void getPerferencesDatas(Map<String, String> datas) { 
    	
        String params = datas.get("name");
        String[] ret = splitFromStringBySymbol(params, "_");
        for (int i = 1; i < ret.length; ++i)
			this.mPreferencesNames.add(ret[i]);
        params = datas.get("url");
        ret = splitFromStringBySymbol(params, "_");
        for (int i = 1; i < ret.length; ++i)
			this.mPreferencesUrls.add(ret[i]);
        params = datas.get("price");
        ret = splitFromStringBySymbol(params, "_");
        for (int i = 1; i < ret.length; ++i)
			this.mPreferencesPrices.add(ret[i]);	

    } 
    
    public static String[] splitFromStringBySymbol(String orginString, String symbol){
		return orginString.split(symbol);
	}
     
    /**
     * @param 需要的key类型
     * @return List<String>, 返回结果是最新的数据
     */ 
    public List<String> getPerferencesByKey(String key)
    {  	
    	getPerferencesDatas(getPerferences());
    	if (key == "name")
    		return this.mPreferencesNames;
    	if (key == "url")
    		return this.mPreferencesUrls;
    	if (key == "price")
    		return this.mPreferencesPrices;
    	return null;
    }
     
} 