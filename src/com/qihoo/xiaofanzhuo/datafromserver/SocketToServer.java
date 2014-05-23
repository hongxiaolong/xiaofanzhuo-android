package com.qihoo.xiaofanzhuo.datafromserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

/*
 * SocketToServer：从服务器获取信息
 * by hongxiaolong
 */
public class SocketToServer
{
	
	private static final String TAG = "SocketToServer";
	
	//aliyun 182.92.80.201
	private static String webServer = "182.92.80.201";
	private static int port = 18026;
    
    static enum Location
    {
    	GetShopInfoByLocation____大山子,
    	GetShopInfoByLocation____798,
    	GetShopInfoByLocation____酒仙桥
    }
    
    public static String getInfoFromServerByLocation(String keyLocation) throws UnknownHostException, IOException
    {
        Socket socket = new Socket(webServer, port);
        // 向服务端程序发送数据
        OutputStream ops  = socket.getOutputStream();        
        OutputStreamWriter opsw = new OutputStreamWriter(ops);
        BufferedWriter bw = new BufferedWriter(opsw);
        
        bw.write(keyLocation);     
        bw.flush();
        
        // 从服务端程序接收数据
        InputStream ips = socket.getInputStream();
        InputStreamReader ipsr = new InputStreamReader(ips);
        BufferedReader br = new BufferedReader(ipsr);
        String ret = "";        
        while((ret = br.readLine()) != null)
        	Log.i(TAG, "成功收到服务器返回的数据!!!" + ret);   
        socket.close();
        return ret;
    }
    
    public static String getInfoFromServerByID(String keyLocation) throws UnknownHostException, IOException
    {
        Socket socket = new Socket(webServer, port);
        // 向服务端程序发送数据
        OutputStream ops  = socket.getOutputStream();        
        OutputStreamWriter opsw = new OutputStreamWriter(ops);
        BufferedWriter bw = new BufferedWriter(opsw);
        
        bw.write(keyLocation);     
        bw.flush();
        
        // 从服务端程序接收数据
        InputStream ips = socket.getInputStream();
        InputStreamReader ipsr = new InputStreamReader(ips);
        BufferedReader br = new BufferedReader(ipsr);
        String ret = "";        
        while((ret = br.readLine()) != null)
        	Log.i(TAG, "成功收到服务器返回的数据!!!" + ret);   
        socket.close();
        return ret;
    }
    
}
