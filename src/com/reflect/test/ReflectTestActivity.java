package com.reflect.test;

import java.lang.reflect.Method;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ReflectTestActivity extends Activity {
    private static final String TAG = "ReflectTestActivity";
    private ConnectivityManager mConnectivityManager;
    private Button mMobileDataSwitch;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mMobileDataSwitch = (Button) findViewById(R.id.btnswitch);

        mConnectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        setSwitchBtnText(mMobileDataSwitch, getMobileDataState());

        mMobileDataSwitch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    invokeBooleanArgMethod("setMobileDataEnabled",
                            !getMobileDataState());
                    setSwitchBtnText(mMobileDataSwitch, !getMobileDataState());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean getMobileDataState() {
        boolean isMobileDataEnable = false;
        try {
            Object[] arg = null;
            isMobileDataEnable = (Boolean) invokeMethod("getMobileDataEnabled",
                    arg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isMobileDataEnable;
    }

    private void setSwitchBtnText(Button switchBtn, boolean isOpened) {
        switchBtn.setText(isOpened ? "close" : "open");
    }

    public Object invokeMethod(String methodName, Object[] arg)
            throws Exception {
        Class ownerClass = mConnectivityManager.getClass();
        Class[] argsClass = null;
        if (arg != null) {
            argsClass = new Class[1];
            argsClass[0] = arg.getClass();
        }

        Method method = ownerClass.getMethod(methodName, argsClass);

        return method.invoke(mConnectivityManager, arg);
    }

    public Object invokeBooleanArgMethod(String methodName, boolean value)
            throws Exception {
        Class ownerClass = mConnectivityManager.getClass();

        Class[] argsClass = new Class[1];
        argsClass[0] = boolean.class;

        Method method = ownerClass.getMethod(methodName, argsClass);

        return method.invoke(mConnectivityManager, value);
    }
}