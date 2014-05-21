package com.qihoo.xiaofanzhuo.mainactivity;


import android.app.Application;

/**
 * MyGlobalClass
 * @author hongxiaolong
 *
 */
public class MyGlobalClass extends Application{
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		this.setQueueStatus(false);
		this.setBusyStatus(false);
		this.setPraiseStatus(false);
	}

	private boolean myQueueStatus;
	private boolean myBusyStatus;
	private boolean myPraiseStatus;
	
	public void setQueueStatus(boolean statue)
	{
		this.myQueueStatus = statue;
		return;
	}
	
	public boolean getQueueStatus()
	{
		return this.myQueueStatus;
	}
	
	public void setBusyStatus(boolean statue)
	{
		this.myBusyStatus = statue;
		return;
	}
	
	public boolean getBusyStatus()
	{
		return this.myBusyStatus;
	}
	
	public void setPraiseStatus(boolean statue)
	{
		this.myPraiseStatus = statue;
		return;
	}
	
	public boolean getPraiseStatus()
	{
		return this.myPraiseStatus;
	}
}
