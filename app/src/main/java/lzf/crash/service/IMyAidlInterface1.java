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

import lzf.crash.IMyAidlInterface_1;
import lzf.crash.MainActivity;

public class IMyAidlInterface1 extends Service {

	private String TAG = getClass().getName();
	//用于判断进程是否运行
	private String Process_Name = "lzf.crash:IMyAidlInterface2";
	private boolean isCrash;
	private IMyAidlInterface_1 service_1 = new IMyAidlInterface_1.Stub() {

		@Override
		public void stopService() throws RemoteException {
			Intent i = new Intent(IMyAidlInterface1.this, IMyAidlInterface2.class);
			IMyAidlInterface1.this.stopService(i);
		}

		@Override
		public void startService() throws RemoteException {
			Intent i = new Intent(IMyAidlInterface1.this, IMyAidlInterface2.class);
			IMyAidlInterface1.this.startService(i);
		}
	};


	public void onCreate() {
		Log.e("lzf_service 1","开启服务 1 ");
		new Thread() {
			public void run() {
				while (true) {
					boolean isRun = isProcessRunning(IMyAidlInterface1.this, Process_Name);
					if (!isRun) {
						try {
							Log.i(TAG, "重新启动服务2: "+service_1);
							service_1.startService();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
					Log.e("crash service1", isCrash+" ");
					if (isCrash){
						Log.e(TAG, " 重新启动 activity_main ");
						Intent intent=new Intent(IMyAidlInterface1.this,MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						isCrash=false;
					}
				}
			}
		}.start();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		isCrash=intent.getBooleanExtra("crash",false);
		return START_STICKY;
	}


	@Override
	public IBinder onBind(Intent intent) {
		return (IBinder) service_1;
	}

	// 进程是否运行
	public static boolean isProcessRunning(Context context, String processName) {

		boolean isRunning = false;
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningAppProcessInfo> lists = am.getRunningAppProcesses();
		for (RunningAppProcessInfo info : lists) {
			if (info.processName.equals(processName)) {
				Log.i("Service1进程", "" + info.processName);
				isRunning = true;
			}
		}
		return isRunning;
	}
}
