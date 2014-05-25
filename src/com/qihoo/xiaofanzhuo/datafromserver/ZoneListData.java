package com.qihoo.xiaofanzhuo.datafromserver;

import java.util.ArrayList;
import java.util.List;

/*
 * ZoneListData：区域商家列表数据
 * by hongxiaolong
 */
public class ZoneListData {
	
	private List<BusinessData> listArray = new ArrayList<BusinessData>();
	
	/*
	 * 构造函数
	 * @pram:服务端传入的字符串，以"/n"分隔
	 */
	ZoneListData(String originString)
	{
		String[] listZone = splitFromString(originString);
		for (int i = 0; i < listZone.length; ++i)
			listArray.add(new BusinessData(listZone[i]));
	}
	
	private String[] splitFromString(String orginString)
	{
		return orginString.split("/n");
	}
	
	public List<BusinessData> getZoneListData()
	{
		return this.listArray;
	}
}
