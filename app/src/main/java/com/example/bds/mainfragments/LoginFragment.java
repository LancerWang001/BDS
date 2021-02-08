package com.example.bds.mainfragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Constants;
import com.example.bds.HomeActivity;
import com.example.bds.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginFragment extends Fragment {
    private EditText textuerName;
    private EditText textpassword;
    private Button btLogin;
    private Button btExit;
    /* accounts map */
    Map<String, String> devOpsMap = new HashMap<>();
    Map<String, String> adminMap = new HashMap<>();
    Map<String, String> userMap = new HashMap<>();

    public LoginFragment() {
        devOpsMap.put("devops1", "devops1");
        adminMap.put("admin1", "admin1");
        userMap.put("user1", "user1");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textuerName = (EditText) getActivity().findViewById(R.id.uerName);
        textpassword = (EditText) getActivity().findViewById(R.id.password);
        btLogin = (Button) getActivity().findViewById(R.id.button_login);
        btExit = (Button) getActivity().findViewById(R.id.button_exit);
        reinitDevOps();
        btLogin.setOnClickListener(v -> {
            String userName = textuerName.getText().toString().trim();
            String userPasswd = textpassword.getText().toString().trim();
            String info = checkAccount(userName, userPasswd);
            Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
        });
        btExit.setOnClickListener(v -> {
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
            ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
            manager.killBackgroundProcesses(getActivity().getPackageName());
        });
    }

    //登陆验证
    private String checkAccount(String userName, String userPasswd) {
        String loginInfo = "";
        if (devOpsMap.containsKey(userName)) {
            if (userPasswd.equals(devOpsMap.get(userName))) {
                loginInfo = "开发人员登入";
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.preAuth, new DevOpsFragment())
                        .commit();
            }
        } else if (adminMap.containsKey(userName)) {
            if (userPasswd.equals(adminMap.get(userName))) loginInfo = "管理人员登入";
        } else if (userMap.containsKey(userName)) {
            if (userPasswd.equals(userMap.get(userName))) {
                loginInfo = "用户登入";
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        } else {
            loginInfo = "用户名不存在";
            textuerName.requestFocus();
        }
        if (loginInfo.equals("")) {
            loginInfo = "密码错误";
            textpassword.requestFocus();
        }
        return loginInfo;
    }

    private void reinitDevOps () {
        SharedPreferences sp = getContext().getSharedPreferences(Constants.DEVOPSCONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.ComntWay, "DT");
        editor.apply();
    }
}
