package com.qihoo.xiaofanzhuo.datafromserver;

import java.util.HashMap;

/*
 * ZoneListData：商家数据
 * by hongxiaolong
 */

public class BusinessData {

	private String ShopID = null;
	private String  ShopName = null;
	private String BusyState = null;
	private String ShopImgUrl = null;
	private String ShopInfo = null;
	private String ShopSite = null;
	private String PhoneNum = null;
	private String SendFoodOut = null;
	private String NumofPeopleWant2Eat = null;
	private String PraiseNum = null;
	private String ShopAverPrice = null;
	private String TasteScore = null;
	private String ServiceScore = null;
	private String EnvScore = null;
	private String ShopTag = null;
	private String Other1 = null;
	private String Other2 = null;
	private String Other3 = null;
	private String ShopMenu = null;
	
	private HashMap<String, String> bussinessMap = new HashMap<String, String>();
	public static final String[] keyArray= {
			"ShopID", "ShopName",  "BusyState",  "ShopImgUrl",  "ShopInfo",  "ShopSite",  "PhoneNum",  "SendFoodOut",  
			"NumofPeopleWant2Eat",  "PraiseNum",  "ShopAverPrice",  "TasteScore", "ServiceScore",  "EnvScore",  "ShopTag",  
			"Other1", "Other2",  "Other3",  
			"ShopMenu"
	};

	/*
	 * 构造函数
	 * @pram:服务端传入的字符串，，以"____"分隔
	 */
	BusinessData(String originString)
	{
		String[] listArray = splitFromString(originString);
		for (int i = 0; i < listArray.length; ++i)
			bussinessMap.put(keyArray[i], listArray[i]);
	}
	
	private String[] splitFromString(String orginString)
	{
		return orginString.split("____");
	}
	
	public String getShopID()
	{
		return this.ShopID = bussinessMap.get("ShopID");
	}
	public String getShopName()
	{
		return this.ShopName = bussinessMap.get("ShopName");
	}
	public String getBusyState()
	{
		return this.BusyState = bussinessMap.get("BusyState");
	}
	public String getShopImgUrl()
	{
		return this.ShopImgUrl = bussinessMap.get("ShopImgUrl");
	}
	public String getShopInfo()
	{
		return this.ShopInfo = bussinessMap.get("ShopInfo");
	}
	public String getShopSite()
	{
		return this.ShopSite = bussinessMap.get("ShopSite");
	}
	public String getPhoneNum()
	{
		return this.PhoneNum = bussinessMap.get("PhoneNum");
	}
	public String getSendFoodOut()
	{
		return this.SendFoodOut = bussinessMap.get("SendFoodOut");
	}
	public String getNumofPeopleWant2Eat()
	{
		return this.NumofPeopleWant2Eat = bussinessMap.get("NumofPeopleWant2Eat");
	}
	public String getPraiseNum()
	{
		return this.PraiseNum = bussinessMap.get("PraiseNum");
	}
	public String getTasteScore()
	{
		return this.TasteScore = bussinessMap.get("TasteScore");
	}
	public String getShopAverPrice()
	{
		return this.ShopAverPrice = bussinessMap.get("ShopAverPrice");
	}
	public String getServiceScore()
	{
		return this.ServiceScore = bussinessMap.get("ServiceScore");
	}
	public String getEnvScore()
	{
		return this.EnvScore = bussinessMap.get("EnvScore");
	}
	public String getShopTag()
	{
		return this.ShopTag = bussinessMap.get("ShopTag");
	}
	public String getOther1()
	{
		return this.Other1 = bussinessMap.get("Other1");
	}
	public String getOther2()
	{
		return this.Other2 = bussinessMap.get("Other2");
	}
	public String getOther3()
	{
		return this.Other3 = bussinessMap.get("Other3");
	}
	public String getShopMenu()
	{
		return this.ShopMenu = bussinessMap.get("ShopMenu");
	}
	
}
