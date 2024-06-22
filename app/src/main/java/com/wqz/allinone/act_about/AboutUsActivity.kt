package com.wqz.allinone.act_about

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.ScaleAnimation
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wqz.allinone.R

class AboutUsActivity : AppCompatActivity(), View.OnTouchListener, View.OnClickListener {
    private var rlUpdateLog: RelativeLayout? = null
    private var rlDevelopMethod: RelativeLayout? = null
    private var rlLicense: RelativeLayout? = null
    private var rlOpenSource: RelativeLayout? = null
    private var rlStudio: RelativeLayout? = null
    private var rlWqz: RelativeLayout? = null
    private var rlCodegeex: RelativeLayout? = null
    private var rlChatglm: RelativeLayout? = null
    private var rlSd: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        rlUpdateLog = findViewById(R.id.rl_update_log)
        rlDevelopMethod = findViewById(R.id.rl_develop_method)
        rlLicense = findViewById(R.id.rl_license)
        rlOpenSource = findViewById(R.id.rl_open_source)
        rlStudio = findViewById(R.id.rl_studio)
        rlWqz = findViewById(R.id.rl_wqz)
        rlCodegeex = findViewById(R.id.rl_codegeex)
        rlChatglm = findViewById(R.id.rl_chatglm)
        rlSd = findViewById(R.id.rl_sd)

        findViewById<View>(R.id.btn_update_log).setOnClickListener(this)
        findViewById<View>(R.id.btn_update_log).setOnTouchListener(this)
        findViewById<View>(R.id.btn_develop_method).setOnClickListener(this)
        findViewById<View>(R.id.btn_develop_method).setOnTouchListener(this)
        findViewById<View>(R.id.btn_license).setOnClickListener(this)
        findViewById<View>(R.id.btn_license).setOnTouchListener(this)
        findViewById<View>(R.id.btn_github).setOnClickListener(this)
        findViewById<View>(R.id.btn_github).setOnTouchListener(this)

        findViewById<View>(R.id.btn_studio).setOnClickListener(this)
        findViewById<View>(R.id.btn_studio).setOnTouchListener(this)

        findViewById<View>(R.id.btn_wqz).setOnClickListener(this)
        findViewById<View>(R.id.btn_wqz).setOnTouchListener(this)
        findViewById<View>(R.id.btn_codegeex).setOnClickListener(this)
        findViewById<View>(R.id.btn_codegeex).setOnTouchListener(this)
        findViewById<View>(R.id.btn_chatglm).setOnClickListener(this)
        findViewById<View>(R.id.btn_chatglm).setOnTouchListener(this)
        findViewById<View>(R.id.btn_sd).setOnClickListener(this)
        findViewById<View>(R.id.btn_sd).setOnTouchListener(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val zoomInAnimation = ScaleAnimation(
            1f, 0.95f,
            1f, 0.95f,
            v.pivotX,
            v.pivotY
        )
        zoomInAnimation.duration = 150 // 设置动画持续时间
        zoomInAnimation.fillAfter = true // 保持动画结束后的状态

        val zoomOutAnimation = ScaleAnimation(
            0.95f, 1f,
            0.95f, 1f,
            v.pivotX,
            v.pivotY
        )
        zoomOutAnimation.duration = 150 // 设置动画持续时间
        zoomOutAnimation.fillAfter = true // 保持动画结束后的状态

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                when (v.id) {
                    R.id.btn_update_log -> rlUpdateLog?.startAnimation(zoomInAnimation)
                    R.id.btn_develop_method -> rlDevelopMethod?.startAnimation(zoomInAnimation)
                    R.id.btn_license -> rlLicense?.startAnimation(zoomInAnimation)
                    R.id.btn_github -> rlOpenSource?.startAnimation(zoomInAnimation)
                    R.id.btn_studio -> rlStudio?.startAnimation(zoomInAnimation)
                    R.id.btn_wqz -> rlWqz?.startAnimation(zoomInAnimation)
                    R.id.btn_codegeex -> rlCodegeex?.startAnimation(zoomInAnimation)
                    R.id.btn_chatglm -> rlChatglm?.startAnimation(zoomInAnimation)
                    else -> rlSd?.startAnimation(zoomInAnimation)
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                when (v.id) {
                    R.id.btn_update_log -> rlUpdateLog?.startAnimation(zoomOutAnimation)
                    R.id.btn_develop_method -> rlDevelopMethod?.startAnimation(zoomOutAnimation)
                    R.id.btn_license -> rlLicense?.startAnimation(zoomOutAnimation)
                    R.id.btn_github -> rlOpenSource?.startAnimation(zoomOutAnimation)
                    R.id.btn_studio -> rlStudio?.startAnimation(zoomOutAnimation)
                    R.id.btn_wqz -> rlWqz?.startAnimation(zoomOutAnimation)
                    R.id.btn_codegeex -> rlCodegeex?.startAnimation(zoomOutAnimation)
                    R.id.btn_chatglm -> rlChatglm?.startAnimation(zoomOutAnimation)
                    else -> rlSd?.startAnimation(zoomOutAnimation)
                }
            }
        }

        return false
    }

    override fun onClick(v: View?) {
        Toast.makeText(this, "该功能正在完善中", Toast.LENGTH_SHORT).show()
    }
}