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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
        startActivity(intent);
        MainActivity.this.finish();
    }
}