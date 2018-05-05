package com.example.mroot.selfdialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import util.GetEidtTextIml;
import util.SelfDialog;

public class MainActivity extends AppCompatActivity implements GetEidtTextIml{
    private SelfDialog selfDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.self_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selfDialog = new SelfDialog(MainActivity.this);
                selfDialog.setTitle("输入密码");
                selfDialog.setBackOnclickListener(new SelfDialog.onBackOnclickListener() {
                    @Override
                    public void onBack() {
                        Toast.makeText(MainActivity.this,"点击了返回按钮",Toast.LENGTH_LONG).show();
                        selfDialog.dismiss();
                    }
                });

                selfDialog.show();
//                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
//                startActivity(intent);
            }
        });

    }

    @Override
    public void getEditText(String str) {
        Log.i("hanhai",str);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
