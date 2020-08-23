package com.example.bds;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomeActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private static String TAG = "HomeActivity";

    private EditText uerName;
    private EditText password;
    private Button btLogin;

    private RadioGroup mRadioGroup;
    private Fragment fragment;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private RadioButton rb_set,rb_help,rb_support,rb_main;

    EditText speed;
//    DTServiceThread ct;

    /* communicate way : BD / WIFI */
    public static int COMMUNICATE_WAY = R.string.card_cmnt_dt;

    /* Data service */
    BDSService bdsService;
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: BDSService");
            BDSService.BDSBinder binder = (BDSService.BDSBinder) iBinder;
            bdsService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bdsService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView(); //初始化组件
        ((TextView)findViewById(R.id.title_text)).setText("主页");
        rb_set.setOnClickListener(this);
        rb_support.setOnClickListener(this);
        rb_help.setOnClickListener(this);
        rb_main.setOnClickListener(this);

        // Start to invoke BDS service bind
        final Intent intent = new Intent(this, BDSService.class);
        bindService(intent, conn, Service.BIND_AUTO_CREATE);

        //添加默认布局
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, new MainFragment())
                .commit();
    }
    private void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_main);
        rb_set= (RadioButton) findViewById(R.id.rb_set);
        rb_support= (RadioButton) findViewById(R.id.rb_support);
        rb_help= (RadioButton) findViewById(R.id.rb_help);
        rb_main = (RadioButton)findViewById(R.id.card_mainpage);
    }
    @Override
//    public void onClick(View v) {
//        fm=getSupportFragmentManager();
//        transaction=fm.beginTransaction();
//        switch (v.getId()) {
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        fm=getSupportFragmentManager();
        transaction=fm.beginTransaction();
        switch (checkedId){
            case R.id.rb_set:
                transaction.replace(R.id.fragment, new ConfigFragment());
                Toast.makeText(this, "Config", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rb_support:
                transaction.replace(R.id.fragment, new SupportFragment());
                Toast.makeText(this, "Message", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rb_help:
                transaction.replace(R.id.fragment, new HelpFragment());
                Toast.makeText(this, "Find", Toast.LENGTH_SHORT).show();
                break;
        }
        setTabState();
        transaction.commit();
    }
    @Override
    public void onClick(View v) {
        fm=getSupportFragmentManager();
        transaction=fm.beginTransaction();
        switch (v.getId()) {
//    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//        fm=getSupportFragmentManager();
//        transaction=fm.beginTransaction();
//        switch (checkedId){
            case R.id.rb_set:
                transaction.replace(R.id.fragment, new ConfigFragment());
                Toast.makeText(this, "系统设置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rb_support:
                transaction.replace(R.id.fragment, new SupportFragment());
                Toast.makeText(this, "系统维护", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rb_help:
                transaction.replace(R.id.fragment, new HelpFragment());
                Toast.makeText(this, "帮助选项", Toast.LENGTH_SHORT).show();
                break;
            case R.id.card_mainpage:
                transaction.replace(R.id.fragment, new MainFragment());
                Toast.makeText(this, "主页", Toast.LENGTH_SHORT).show();
                break;
        }
        bdsService.sendByDT("Test");
        setTabState();
            transaction.commit();
        }
    //设置选中和未选择的状态
    private void setTabState() {
        setState();
        setHelpState();
        setSupportState();
        setMain();
    }

    private void setState() {
        if (rb_set.isChecked()){
            rb_set.setTextColor(ContextCompat.getColor(this,R.color.colorChecked));
        }else{
            rb_set.setTextColor(ContextCompat.getColor(this,R.color.colorunChecked));
        }
    }

    private void setHelpState() {
        if (rb_help.isChecked()){
            rb_help.setTextColor(ContextCompat.getColor(this,R.color.colorChecked));
        }else{
            rb_help.setTextColor(ContextCompat.getColor(this,R.color.colorunChecked));
        }
    }

    private void setSupportState() {
        if (rb_support.isChecked()){
            rb_support.setTextColor(ContextCompat.getColor(this,R.color.colorChecked));
        }else{
            rb_support.setTextColor(ContextCompat.getColor(this,R.color.colorunChecked));
        }
    }

    private void setMain(){
        if (rb_main.isChecked()){
            rb_main.setTextColor(ContextCompat.getColor(this,R.color.colorChecked));
        }else{
            rb_main.setTextColor(ContextCompat.getColor(this,R.color.colorunChecked));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(int eventNum) {
        switch (eventNum) {
            case R.string.card_cmnt_bd:
            case R.string.card_cmnt_dt:
                changeCmntWay(eventNum);
        }
    }

    private void changeCmntWay (int cmntWay) {
        HomeActivity.COMMUNICATE_WAY = cmntWay;
        TextView tv = (TextView) findViewById(R.id.cmnt_way);
        tv.setText(HomeActivity.COMMUNICATE_WAY);
    }
}