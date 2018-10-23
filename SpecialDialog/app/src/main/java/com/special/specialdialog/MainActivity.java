package com.special.specialdialog;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.special.wheelview.SpcialDialog;
import com.special.wheelview.util.DateUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvClick;
    private SpcialDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        tvClick = findViewById(R.id.tv_click);
        tvClick.setOnClickListener(this);

    }

    private void showDialog(){

        mDialog = new SpcialDialog(this);

        mDialog.setTitle("")
                .setItems(1970,2050)
                .setButtonText("确定")
                .setCancleButtonText("清空")
                .setDialogStyle(Color.parseColor("#895D13"))
                .setCount(3)
                .showDays(false)
                .setSelection("2018-10")
                .show();

        mDialog.setOnDialogItemClickListener(new SpcialDialog.OnDialogItemClickListener() {
            @Override
            public void onItemClick(int positionYear, Object textYear, int positionMonth, Object textMonth, int positionDay, Object textDay, boolean clickType) {
                Toast.makeText(MainActivity.this, textYear+"年"+textMonth+"月"+textDay+"日", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        showDialog();
    }
}
