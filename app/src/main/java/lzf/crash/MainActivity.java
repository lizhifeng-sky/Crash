package lzf.crash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import lzf.crash.service.IMyAidlInterface1;
import lzf.crash.service.IMyAidlInterface2;

public class MainActivity extends Activity {

    private Button mButton;
    private TextView mTextView;
    private MyApplication myApplication;
    private long mExitTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myApplication = (MyApplication) getApplication();
        myApplication.init();
        myApplication.addActivity(this);

        mButton = (Button) findViewById(R.id.btn);
        mTextView = (TextView) findViewById(R.id.show);

        Intent i2 = new Intent(MainActivity.this,IMyAidlInterface2.class);
        startService(i2);

        mButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pressed();
            }

        });
    }
    //人工制造异常崩溃
    private void pressed() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mTextView.setText("beng...");
            }
        }).start();

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                //杀死该应用进程
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}