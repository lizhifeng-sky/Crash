package lzf.crash.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;

import lzf.crash.IMyAidlInterface_2;

public class IMyAidlInterface2 extends Service {
	private String TAG = getClass().getName();
	private String Process_Name = "lzf.crash:IMyAidlInterface1";
	private IMyAidlInterface_2 service_2 = new IMyAidlInterface_2.Stub() {

		@Override
		public void stopService() throws RemoteException {
			Intent i = new Intent(IMyAidlInterface2.this, IMyAidlInterface1.class);
			IMyAidlInterface2.this.stopService(i);
		}

		@Override
		public void startService() throws RemoteException {
			Intent i = new Intent(IMyAidlInterface2.this, IMyAidlInterface1.class);
			IMyAidlInterface2.this.startService(i);
			
		}
	};


	public void onCreate() {
		Log.e("lzf_service 2","开启服务 2 ");
		new Thread() {
			public void run() {
				while (true) {
					boolean isRun = isProcessRunning(IMyAidlInterface2.this, Process_Name);
					if(!isRun){
						try {
							Log.i(TAG, "重新启动服务1: "+service_2);
							service_2.startService();
						} catch (RemoteException e) {
							e.printStackTrace();
						}	
					}
				}
			};
		}.start();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return (IBinder) service_2;
	}
	
	//进程是否运行
	public static boolean isProcessRunning(Context context, String proessName) {
		
		boolean isRunning = false;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningAppProcessInfo> lists = am.getRunningAppProcesses();
		for(RunningAppProcessInfo info : lists){
			Log.i("Service2  总进程", ""+info.processName);
			if(info.processName.equals(proessName)){
				Log.i("Service2进程", ""+info.processName);
				isRunning = true;
			}
		}
		
		return isRunning;
	}
}
