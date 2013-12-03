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
		Log.e(TAG, "����״̬�ı�");  
		boolean tryopen = false;
		  
		//����������ӷ���  
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
		// State state = connManager.getActiveNetworkInfo().getState();  
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState(); // ��ȡ��������״̬  
		if (State.CONNECTED == state) { // �ж��Ƿ�����ʹ��WIFI����  
			cutNet(context, NetState.WIFION);
			tryopen = true;
		}
		  
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState(); // ��ȡ��������״̬  
		if (State.CONNECTED == state) { // �ж��Ƿ�����ʹ��GPRS����  
			cutNet(context, NetState.GPRSON);
			tryopen = true; 
		} 
		
		if (tryopen)
		{
			Toast.makeText(context, "�ȿȣ�ע������ֻѧ�ԣ�ѧ�Ի�����ú������", Toast.LENGTH_LONG).show(); 
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

	// ����/�ر�GPRS
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