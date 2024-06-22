package com.wqz.allinone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;

import androidx.appcompat.app.AppCompatActivity;

import com.wqz.allinone.act_about.AboutAppActivity;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        findViewById(R.id.btn_about).setOnClickListener(this);
        findViewById(R.id.btn_about).setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_about) {
            startActivity(new Intent(this, AboutAppActivity.class));
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ScaleAnimation zoomInAnimation = new ScaleAnimation(1f, 0.95f, 1f, 0.95f, v.getPivotX(), v.getPivotY());
        zoomInAnimation.setDuration(150); // 设置动画持续时间
        zoomInAnimation.setFillAfter(true); // 保持动画结束后的状态

        ScaleAnimation zoomOutAnimation = new ScaleAnimation(0.95f, 1f, 0.95f, 1f, v.getPivotX(), v.getPivotY());
        zoomOutAnimation.setDuration(150); // 设置动画持续时间
        zoomOutAnimation.setFillAfter(true); // 保持动画结束后的状态

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (v.getId() == R.id.btn_about) {
                findViewById(R.id.rl_about).startAnimation(zoomInAnimation);
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            if (v.getId() == R.id.btn_about) {
                findViewById(R.id.rl_about).startAnimation(zoomOutAnimation);
            }
        }
        return false;
    }
}