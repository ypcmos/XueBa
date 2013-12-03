package com.yp.xueba;
import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo.State;
import android.net.*;
import android.util.Log;
import android.widget.Toast;
import android.net.wifi.WifiManager;  

public class NetStateReceiver extends BroadcastReceiver{  

	String TAG = "tag";  
	enum NetState{WIFION, GPRSON};
	@Override  
	public void onReceive(Context context, Intent intent) {  
		Log.e(TAG, "网络状态改变");  
		boolean tryopen = false;
		  
		//获得网络连接服务  
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
		// State state = connManager.getActiveNetworkInfo().getState();  
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState(); // 获取网络连接状态  
		if (State.CONNECTED == state) { // 判断是否正在使用WIFI网络  
			cutNet(context, NetState.WIFION);
			tryopen = true;
		}
		  
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState(); // 获取网络连接状态  
		if (State.CONNECTED == state) { // 判断是否正在使用GPRS网络  
			cutNet(context, NetState.GPRSON);
			tryopen = true; 
		} 
		
		if (tryopen)
		{
			Toast.makeText(context, "咳咳，注意你是只学霸，学霸还上泥煤的网！", Toast.LENGTH_LONG).show(); 
		}
	}  
	public void cutNet(Context context, NetState state)
	{
		switch (state)
		{
		case WIFION:
			WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE); 
			wifiManager.setWifiEnabled(false);
			break;
		case GPRSON:
			ConnectivityManager mCM = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE); 
			gprsEnabled(mCM, false);
			break;
		default:
			break;
		}	 
	}
	
	private boolean gprsEnabled(ConnectivityManager mCM, boolean bEnable)  
    {  
        Object[] argObjects = null;  
                  
        boolean isOpen = gprsIsOpenMethod(mCM, "getMobileDataEnabled");  
        if(isOpen == !bEnable)  
        {  
            setGprsEnabled(mCM, "setMobileDataEnabled", bEnable);  
        }  
          
        return isOpen;    
    }

	private boolean gprsIsOpenMethod(ConnectivityManager mCM, String methodName) {
		Class<? extends ConnectivityManager> cmClass = mCM.getClass();
		Class[] argClasses = null;
		Object[] argObject = null;

		Boolean isOpen = false;
		try {
			Method method = cmClass.getMethod(methodName, argClasses);

			isOpen = (Boolean) method.invoke(mCM, argObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isOpen;
	}

	// 开启/关闭GPRS
	private void setGprsEnabled(ConnectivityManager mCM, String methodName, boolean isEnable) 
	{
		Class<? extends ConnectivityManager> cmClass = mCM.getClass();
		Class[] argClasses = new Class[1];
		argClasses[0] = boolean.class;

		try {
			Method method = cmClass.getMethod(methodName, argClasses);
			method.invoke(mCM, isEnable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}