package com.jiajia.essayjoke;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jiajia.baselibrary.CheckNet;
import com.jiajia.baselibrary.OnClick;
import com.jiajia.baselibrary.ViewById;
import com.jiajia.baselibrary.ViewUtils;

public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.test_tv)
    private TextView mTestTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewUtils.inject(this);

        mTestTv.setText("通过注解设置的值");

    }

    @OnClick({R.id.test_tv, R.id.test_iv})
    @CheckNet   // 此方法需要check网络连接状态
    private void onClick(View view) {
        Toast.makeText(this, "点击了我", Toast.LENGTH_SHORT).show();
    }
}