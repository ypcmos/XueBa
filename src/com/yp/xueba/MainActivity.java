package com.yp.xueba;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Properties;


import android.os.Bundle;
import android.os.SystemClock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	Button m_button1;
	final String xbServiceClassName = "com.yp.xueba.XBService"; 
	final String xbServiceName = "com.yp.xueba.XB"; 
	GifView view;
	BroadcastReceiver netReveiver = new NetStateReceiver();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		m_button1 = (Button)this.findViewById(R.id.button1);
		view = (GifView)findViewById(R.id.gifView1);
		view.setStart();
		
		boolean serviceon = isServiceOn(xbServiceClassName, 250);
		
		
		if (!serviceon)
		{
			m_button1.setBackgroundColor(Color.GREEN);
			m_button1.setText("开启学霸模式！");
			m_button1.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					setState(0);
					startService(new Intent(xbServiceName));
					Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);  
			        intent.setAction("arui.alarm.action");  
			        PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0,  
			                intent, 0);  
			        long firstime = SystemClock.elapsedRealtime();  
			        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);  

			        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime,  
			                5 * 1000, sender);  
					MainActivity.this.finish();
				}	
			});
		}else{
			m_button1.setBackgroundColor(Color.RED);
			m_button1.setText("关闭学霸模式");
			m_button1.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					LayoutInflater factory = LayoutInflater.from(MainActivity.this);
					final View dialogView = factory.inflate(R.layout.pass, null);
					AlertDialog dlg = new AlertDialog.Builder(MainActivity.this).setTitle("退出").setView(dialogView)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									final EditText editView = (EditText)dialogView.findViewById(R.id.editText1);
									
									if (editView.getText().toString().trim().equals("我爱学长"))
									{
										setState(1);
										Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);  
								        intent.setAction("arui.alarm.action");  
								        PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0,  
								                intent, 0); 
								        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);  
								        am.cancel(sender);
										stopService(new Intent(xbServiceName));
										//android.os.Process.killProcess(android.os.Process.myPid()); //会导致Service的onDestory无法调用
										MainActivity.this.finish();
									} else {
										Dialog dlg2 = new AlertDialog.Builder(MainActivity.this).setTitle("失败").setMessage("密码错误")
												.setPositiveButton("确定", new DialogInterface.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														// TODO Auto-generated method stub
														
													}
												}).create();
										dlg2.show();
									}
									
								}
							}).create();
					dlg.show();
					
				}	
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public boolean isServiceOn(String className, int maxNum)
	{
		boolean on = false;
		ActivityManager mActivityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE); 
		List<ActivityManager.RunningServiceInfo> mServiceList = mActivityManager.getRunningServices(maxNum);
		
		for(int i = 0; i < mServiceList.size(); i++)
		{ 	
			if(className.equals(mServiceList.get(i).service.getClassName()))
			{ 
				
				on = true; 
			}
		}
		return on;
	}
	
	@SuppressLint("WorldWriteableFiles")
	public boolean setState(int state)
	{
		Properties properties = new Properties();
		properties.put("xbstate", String.valueOf(state));
		try
		{
			FileOutputStream stream = this.openFileOutput("xbstate", Context.MODE_WORLD_WRITEABLE);
			properties.store(stream, "");
		}
		catch(Exception ex)
		{
			return false;
		}
		return true;
		
	}

}
