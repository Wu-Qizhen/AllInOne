package com.wqz.allinone

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.MotionEvent
import android.view.View
import android.view.animation.ScaleAnimation
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wqz.allinone.act_bookmark.BookmarkFoldersActivity
import com.wqz.allinone.act_todo.TodoListActivity

class MainActivity : AppCompatActivity(), View.OnTouchListener, View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btn_setting).setOnClickListener(this)
        findViewById<View>(R.id.btn_setting).setOnTouchListener(this)
        findViewById<View>(R.id.btn_diary).setOnClickListener(this)
        findViewById<View>(R.id.btn_diary).setOnTouchListener(this)
        findViewById<View>(R.id.btn_note).setOnClickListener(this)
        findViewById<View>(R.id.btn_note).setOnTouchListener(this)
        findViewById<View>(R.id.btn_todo).setOnClickListener(this)
        findViewById<View>(R.id.btn_todo).setOnTouchListener(this)
        findViewById<View>(R.id.btn_clock_in).setOnClickListener(this)
        findViewById<View>(R.id.btn_clock_in).setOnTouchListener(this)
        findViewById<View>(R.id.btn_schedule).setOnClickListener(this)
        findViewById<View>(R.id.btn_schedule).setOnTouchListener(this)
        findViewById<View>(R.id.btn_bookmark).setOnClickListener(this)
        findViewById<View>(R.id.btn_bookmark).setOnTouchListener(this)
        findViewById<View>(R.id.btn_school_timetable).setOnClickListener(this)
        findViewById<View>(R.id.btn_school_timetable).setOnTouchListener(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val zoomInAnimation = ScaleAnimation(1f, 0.95f, 1f, 0.95f, v!!.pivotX, v.pivotY)
        zoomInAnimation.duration = 150 // 设置动画持续时间
        zoomInAnimation.fillAfter = true // 保持动画结束后的状态

        val zoomOutAnimation = ScaleAnimation(0.95f, 1f, 0.95f, 1f, v.pivotX, v.pivotY)
        zoomOutAnimation.duration = 150 // 设置动画持续时间
        zoomOutAnimation.fillAfter = true // 保持动画结束后的状态

        if (event!!.action == MotionEvent.ACTION_DOWN) {
            when (v.id) {
                R.id.btn_diary -> findViewById<View>(R.id.rl_diary).startAnimation(zoomInAnimation)
                R.id.btn_note -> findViewById<View>(R.id.rl_note).startAnimation(
                    zoomInAnimation
                )

                R.id.btn_todo -> findViewById<View>(R.id.rl_todo).startAnimation(
                    zoomInAnimation
                )

                R.id.btn_clock_in -> findViewById<View>(R.id.rl_clock_in).startAnimation(
                    zoomInAnimation
                )

                R.id.btn_schedule -> findViewById<View>(R.id.rl_schedule).startAnimation(
                    zoomInAnimation
                )

                R.id.btn_bookmark -> findViewById<View>(R.id.rl_bookmark).startAnimation(
                    zoomInAnimation
                )

                R.id.btn_school_timetable -> findViewById<View>(R.id.rl_school_timetable).startAnimation(
                    zoomInAnimation
                )

                else -> findViewById<View>(R.id.rl_setting).startAnimation(zoomInAnimation)
            }
        } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_OUTSIDE) {
            when (v.id) {
                R.id.btn_diary -> findViewById<View>(R.id.rl_diary).startAnimation(zoomOutAnimation)
                R.id.btn_note -> findViewById<View>(R.id.rl_note).startAnimation(
                    zoomOutAnimation
                )

                R.id.btn_todo -> findViewById<View>(R.id.rl_todo).startAnimation(
                    zoomOutAnimation
                )

                R.id.btn_clock_in -> findViewById<View>(R.id.rl_clock_in).startAnimation(
                    zoomOutAnimation
                )

                R.id.btn_schedule -> findViewById<View>(R.id.rl_schedule).startAnimation(
                    zoomOutAnimation
                )

                R.id.btn_bookmark -> findViewById<View>(R.id.rl_bookmark).startAnimation(
                    zoomOutAnimation
                )

                R.id.btn_school_timetable -> findViewById<View>(R.id.rl_school_timetable).startAnimation(
                    zoomOutAnimation
                )

                else -> findViewById<View>(R.id.rl_setting).startAnimation(zoomOutAnimation)
            }
        }

        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_bookmark -> startActivityWithAnim(
                R.id.tv_bookmark,
                "tv_bookmark",
                BookmarkFoldersActivity::class.java
            )

            R.id.btn_setting -> startActivityWithAnim(
                R.id.tv_setting,
                "tv_setting",
                SettingActivity::class.java
            )

            R.id.btn_todo -> startActivityWithAnim(
                R.id.iv_todo,
                "iv_todo",
                R.id.tv_todo,
                "tv_todo",
                TodoListActivity::class.java
            )

            else -> Toast.makeText(this, "该功能正在开发中", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startActivityWithAnim(viewId: Int, pairStr: String, targetAct: Class<*>) {
        val textView = findViewById<TextView>(viewId)
        val bundle = ActivityOptions.makeSceneTransitionAnimation(this, textView, pairStr)
            .toBundle()
        startActivity(Intent(this, targetAct), bundle)
    }

    private fun startActivityWithAnim(
        ivId: Int,
        ivPairStr: String,
        tvId: Int,
        tvPairStr: String,
        targetAct: Class<*>
    ) {
        val imagePair = Pair(findViewById<View>(ivId), ivPairStr)
        val textPair = Pair(findViewById<View>(tvId), tvPairStr)

        val bundle =
            ActivityOptions.makeSceneTransitionAnimation(this, imagePair, textPair).toBundle()

        val intent = Intent(this, targetAct)
        startActivity(intent, bundle)
    }
}