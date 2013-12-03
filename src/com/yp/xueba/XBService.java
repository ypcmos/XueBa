package com.yp.xueba;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class XBService extends Service
{

	BroadcastReceiver netReveiver = null;
	CountTask countTask = new CountTask();
	final String TAG = "XBService";
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onStart(Intent intent, int startId)
	{ 
		if (netReveiver == null)
		{		
			registNetReceiver();
		}
		//Toast.makeText(this, "1", Toast.LENGTH_LONG).show(); 
	}
	
	@Override
	public void onDestroy()
	{
		if (netReveiver != null)
		{
			unregistNetReceiver();
			Log.i(TAG, "destory");
			String text = "学霸时间：" + countTask.getCount();
			Toast.makeText(this, text, Toast.LENGTH_LONG).show(); 
			countTask.cancel();	
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage("15852940621", null, text, null, null);
		}
		
	}
	
	public void registNetReceiver()
	{
		netReveiver = new NetStateReceiver();
		IntentFilter filter = new IntentFilter();   
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);  
		registerReceiver(netReveiver, filter);  
	}
	
	public void unregistNetReceiver()
	{ 
		unregisterReceiver(netReveiver);  
		netReveiver = null;
	}
	
}


class CountTask{  
	  
    private int recLen = 0;  
    Timer timer = new Timer();  
  
    public CountTask(){           
        timer.schedule(task, 1000L, 1000L); 
    }     

	TimerTask task = new TimerTask() {  
        @Override  
        public void run() {  
        	recLen++;
        	if(recLen < 0){  
        		timer.cancel();    
        	}	
        }
	};
	
	public String getCount()
	{
		StringBuilder sb = new StringBuilder();
		String second = String.valueOf(recLen % 60);
		String minute = String.valueOf(recLen / 60 % 60);
		String hour = String.valueOf(recLen / 3600);
		if (!hour.equals("0"))
			sb.append(hour + "小时");
		if (!minute.equals("0"))
			sb.append(minute + "分");
		sb.append(second + "秒");
		return sb.toString();
	}
	
	public void cancel()
	{
		recLen = 0;
		timer.cancel();
	}
	
}  