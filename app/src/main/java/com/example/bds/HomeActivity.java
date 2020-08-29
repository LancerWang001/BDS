package com.example.bds;

import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.events.BDError;
import com.example.events.configparams.SendConfigParamsEvent;
import com.example.service.BDSService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    /* communicate way : BD / WIFI */
    public static int COMMUNICATE_WAY = R.string.card_cmnt_bd;
    private static String TAG = "HomeActivity";
    /* Data service */
    BDSService bdsService;
    ServiceConnection conn = new AppServiceConnection();

    private RadioGroup mRadioGroup;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private RadioButton rb_set, rb_help, rb_support, rb_main;
    private RadioButton[] btns;


    SharedPreferences context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView(); //初始化组件

        // Start to invoke BDS service bind
        final Intent intent = new Intent(this, BDSService.class);
        bindService(intent, conn, Service.BIND_AUTO_CREATE);

        // register EventBus
        EventBus.getDefault().register(this);

        //添加默认布局
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, new MainFragment())
                .commit();
    }

    private void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_main);
        rb_set = (RadioButton) findViewById(R.id.rb_set);
        rb_support = (RadioButton) findViewById(R.id.rb_support);
        rb_help = (RadioButton) findViewById(R.id.rb_help);
        rb_main = (RadioButton) findViewById(R.id.card_mainpage);

        ((TextView) findViewById(R.id.title_text)).setText("主页");
        rb_set.setOnClickListener(this);
        rb_support.setOnClickListener(this);
        rb_help.setOnClickListener(this);
        rb_main.setOnClickListener(this);
        btns = new RadioButton[]{rb_set, rb_help, rb_main, rb_support};
    }

    @Override
    public void onClick(View v) {
        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        Fragment f = new MainFragment();
        String toastText = "主页";

        switch (v.getId()) {
            case R.id.rb_set:
                f = new ConfigFragment();
                toastText = "系统设置";
                break;
            case R.id.rb_support:
                f = new SupportFragment();
                toastText = "系统维护";
                break;
            case R.id.rb_help:
                f = new HelpFragment();
                toastText = "帮助选项";
                break;
            case R.id.card_mainpage:
                f = new MainFragment();
                toastText = "主页";
                break;
        }
        transaction.replace(R.id.fragment, f);
        transaction.commit();
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
        setTabState((RadioButton) v);

        // for test service
        EventBus.getDefault().post(new SendConfigParamsEvent("100", "120", "255"));
    }

    //设置选中和未选择的状态
    private void setTabState(RadioButton btn) {
        for (RadioButton child : btns) {
            int color = R.color.colorunChecked;
            if (btn.getId() == child.getId()) {
                color = R.color.colorChecked;
            }
            child.setTextColor(ContextCompat.getColor(this, color));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            bdsService.startLocationService(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onError(BDError error) {
        // handle BD error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(error.errorCode);
        builder.create().show();
    }

    private class AppServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: BDSService");
            BDSService.BDSBinder binder = (BDSService.BDSBinder) iBinder;
            bdsService = binder.getService();
            bdsService.bindContext = HomeActivity.this;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bdsService = null;
        }
    }
}