package lzf.crash;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    List<Activity> list = new ArrayList<>();
    Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this.getApplicationContext();
        Log.e("lzf_onCreate",""+ System.currentTimeMillis());
    }

    public void init(){
        //设置该CrashHandler为程序的默认处理器    
        MyUncaughtExceptionHandler catchException = new MyUncaughtExceptionHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(catchException);
    }

    /**
     * Activity关闭时，删除Activity列表中的Activity对象*/
    public void removeActivity(Activity a){
        list.remove(a);
    }

    /**
     * 向Activity列表中添加Activity对象*/
    public void addActivity(Activity a){
        list.add(a);
    }

    /**
     * 关闭Activity列表中的所有Activity*/
    public void finishActivity(){
        for (Activity activity : list) {
            if (null != activity) {
                activity.finish();
            }
        }
        //杀死该应用进程  
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
