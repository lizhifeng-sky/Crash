package lzf.crash;

import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.lang.Thread.UncaughtExceptionHandler;

import lzf.crash.service.IMyAidlInterface1;

public class MyUncaughtExceptionHandler implements UncaughtExceptionHandler {

    private MyApplication myApplication;
    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;

    public MyUncaughtExceptionHandler(MyApplication myApplication) {
        this.myApplication = myApplication;
        mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的异常处理器
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e("lzf_crash", "" + System.currentTimeMillis() + ex.getMessage());
        if (!handleException(ex) && mUncaughtExceptionHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mUncaughtExceptionHandler.uncaughtException(thread, ex);
        } else {
            Intent i1 = new Intent(myApplication.getApplicationContext(), IMyAidlInterface1.class);
            i1.putExtra("crash",true);
            myApplication.startService(i1);
            myApplication.finishActivity();
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息    
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(myApplication.getApplicationContext(), "很抱歉,程序出现异常,一秒钟后重启.",
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        return true;
    }
}