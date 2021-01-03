package com.example.bds;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText textuerName;
    private EditText textpassword;
    private Button btLogin;
    private Button btExit;
    private HashMap accountList = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textuerName = (EditText) findViewById(R.id.uerName);
        textpassword = (EditText) findViewById(R.id.password);
        btLogin = (Button) findViewById(R.id.button_login);
        btExit = (Button) findViewById(R.id.button_exit);
        btLogin.setOnClickListener(v -> {
            String userName = textuerName.getText().toString().trim();
            String userPasswd = textpassword.getText().toString().trim();
            String info = checkAccount(userName, userPasswd);
            Toast.makeText(MainActivity.this, info, Toast.LENGTH_SHORT).show();
        });
        btExit.setOnClickListener(v -> {
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
            ActivityManager manager = (ActivityManager) MainActivity.this.getSystemService(Context.ACTIVITY_SERVICE);
            manager.killBackgroundProcesses(MainActivity.this.getPackageName());
        });
        accountList.put("admin1", "admin1");
        accountList.put("admin2", "admin2");
        accountList.put("admin3", "admin3");
    }

    //登陆验证
    private String checkAccount(String userName, String userPasswd) {
        String loginInfo = "";
        String tmpPass = (String) accountList.get(userName);
        if (tmpPass != null && tmpPass.equals(userPasswd)) {
            loginInfo = "登陆成功";
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (tmpPass == null) {
            loginInfo = "用户名不存在";
            textuerName.requestFocus();
        } else if (!tmpPass.equals(userPasswd)) {
            loginInfo = "密码错误";
            textpassword.requestFocus();
        }
        return loginInfo;
    }
}