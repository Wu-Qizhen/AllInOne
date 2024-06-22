package com.wqz.allinone

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Pair
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        val ivPen = findViewById<View>(R.id.iv_pen)
        val ivTextWxgy = findViewById<View>(R.id.iv_text_wxgy)

        /*// 设置共享元素的 transitionName
        ivPen.transitionName = "logo_pen"
        ivTextWxgy.transitionName = "logo_text"
        Handler().postDelayed({
            // 创建共享元素对
            val imagePair = Pair.create(ivPen, "logo_pen")
            val textPair = Pair.create(ivTextWxgy, "logo_text")
            // 创建 ActivityOptions 对象，包含共享元素过渡动画
            val options =
                ActivityOptions.makeSceneTransitionAnimation(this, *arrayOf(imagePair, textPair))
            // 创建 Intent 来启动新的 Activity
            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            // 启动新的 Activity，并传递 ActivityOptions
            startActivity(intent, options.toBundle())
        }, 1500)*/

        Handler().postDelayed({
            val imagePair = Pair(ivPen, "logo_pen")
            val textPair = Pair(ivTextWxgy, "logo_text")

            val bundle =
                ActivityOptions.makeSceneTransitionAnimation(this, imagePair, textPair).toBundle()

            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent, bundle)
        }, 1500)
    }
}