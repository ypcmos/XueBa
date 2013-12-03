package com.yp.xueba;
import java.io.FileInputStream;
import java.util.Properties;

import android.content.BroadcastReceiver;  
import android.content.Context;  
import android.content.Intent;  
  
public class AlarmReceiver extends BroadcastReceiver {  
    @Override  
    public void onReceive(Context context, Intent intent) {  
  
        if (intent.getAction().equals("arui.alarm.action")) {  
            Intent i = new Intent();  
            i.setClass(context, XBService.class);  
            // ����service   
            // ��ε���startService�������������service ���ǻ��ε���onStart 
            if (0 == readState(context))
            {
            	context.startService(i);  
            }
        }  
    } 
    
    private int readState(Context context)
    {
    	Properties properties = new Properties();
		try
		{
			FileInputStream stream = context.openFileInput("xbstate");
			properties.load(stream);
		}
		catch(Exception ex)
		{
			return 1;
		}
		return Integer.valueOf(properties.getProperty("xbstate").toString());
    }
}  