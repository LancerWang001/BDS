package com.example.bds;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener {

    private EditText uerName;
    private EditText password;
    private Button btLogin;

    private RadioGroup mRadioGroup;
    private List<Fragment> fragments = new ArrayList<>();
    private Fragment fragment;
    private FragmentManager fm;
    private FragmentTransaction transaction;
//    private RadioButton rb_Home,rb_Message,rb_Find,rb_My;
    private RadioButton rb_set,rb_help,rb_support;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uerName = (EditText)findViewById(R.id.uerName);
        password = (EditText)findViewById(R.id.password);
        btLogin = (Button)findViewById(R.id.button_login);
        btLogin.setOnClickListener(this);

        initView(); //初始化组件
        mRadioGroup.setOnCheckedChangeListener(this); //点击事件
        fragments = getFragments(); //添加布局
        //添加默认布局
        normalFragment();
    }



    //默认布局
    private void normalFragment() {
        fm=getSupportFragmentManager();
        transaction=fm.beginTransaction();
        fragment=fragments.get(0);
        transaction.replace(R.id.fragment,fragment);
        transaction.commit();
    }

    private void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_main);
        rb_set= (RadioButton) findViewById(R.id.rb_set);
        rb_support= (RadioButton) findViewById(R.id.rb_support);
        rb_help= (RadioButton) findViewById(R.id.rb_help);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        fm=getSupportFragmentManager();
        transaction=fm.beginTransaction();
        switch (checkedId){
            case R.id.rb_set:
                fragment=fragments.get(0);
                transaction.replace(R.id.fragment,fragment);
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rb_support:
                fragment=fragments.get(1);
                transaction.replace(R.id.fragment,fragment);
                Toast.makeText(this, "Message", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rb_help:
                fragment=fragments.get(2);
                transaction.replace(R.id.fragment,fragment);
                Toast.makeText(this, "Find", Toast.LENGTH_SHORT).show();
                break;
        }
//        setTabState();
        transaction.commit();
    }

    //设置选中和未选择的状态
//    private void setTabState() {
//        setState();
//        setHelpState();
//        setSupportState();
//    }
//
//    private void setState() {
//        if (rb_set.isChecked()){
//            rb_set.setTextColor(ContextCompat.getColor(this,R.color.colorRadioButtonP));
//        }else{
//            rb_set.setTextColor(ContextCompat.getColor(this,R.color.colorRadioButtonN));
//        }
//    }
//
//    private void setHelpState() {
//        if (rb_help.isChecked()){
//            rb_help.setTextColor(ContextCompat.getColor(this,R.color.colorRadioButtonP));
//        }else{
//            rb_help.setTextColor(ContextCompat.getColor(this,R.color.colorRadioButtonN));
//        }
//    }
//
//    private void setSupportState() {
//        if (rb_support.isChecked()){
//            rb_support.setTextColor(ContextCompat.getColor(this,R.color.colorRadioButtonP));
//        }else{
//            rb_support.setTextColor(ContextCompat.getColor(this,R.color.colorRadioButtonN));
//        }
//    }

//    private void setHomeState() {
//        if (rb_Home.isChecked()){
//            rb_Home.setTextColor(ContextCompat.getColor(this,R.color.colorRadioButtonP));
//        }else{
//            rb_Home.setTextColor(ContextCompat.getColor(this,R.color.colorRadioButtonN));
//        }
//    }

    public List<Fragment> getFragments() {
        fragments.add(new SupportFragment());
        fragments.add(new ConfigFragment());
        fragments.add(new HelpFragment());
        return fragments;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
        startActivity(intent);
        MainActivity.this.finish();
    }
}