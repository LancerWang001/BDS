package com.example.bds;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textuerName;
    private EditText textpassword;
    private Button btLogin;
    private HashMap accountList = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textuerName = (EditText) findViewById(R.id.uerName);
        textpassword = (EditText) findViewById(R.id.password);
        btLogin = (Button) findViewById(R.id.button_login);
        //btLogin.setOnClickListener(this);
        btLogin.setOnClickListener(v -> {
            String userName = textuerName.getText().toString().trim();
            String userPasswd = textpassword.getText().toString().trim();
            String info = checkAccount(userName, userPasswd);
            Toast.makeText(MainActivity.this, info, Toast.LENGTH_SHORT).show();
        });
        accountList.put("test1", "test1");
        accountList.put("test2", "test2");
        accountList.put("test3", "test3");
    }

    //登陆验证
    private String checkAccount(String userName, String userPasswd) {
        String loginInfo = "";
        String tmpPass = (String) accountList.get(userName);
        if (tmpPass != null && tmpPass.equals(userPasswd)) {
            loginInfo = "登陆成功";
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        } else if (tmpPass == null) {
            loginInfo = "用户名不存在";
            textuerName.requestFocus();
        } else if (!tmpPass.equals(userPasswd)) {
            loginInfo = "密码错误";
            textpassword.requestFocus();
        }
        return loginInfo;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        MainActivity.this.finish();
    }
}