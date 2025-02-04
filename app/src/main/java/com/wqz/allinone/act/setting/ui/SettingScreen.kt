package com.wqz.allinone.act.setting.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wqz.allinone.R
import com.wqz.allinone.act.about.AboutAppActivity
import com.wqz.allinone.act.password.PasswordCheckActivity
import com.wqz.allinone.preference.LayoutPreferencesManager
import com.wqz.allinone.ui.XItem

/**
 * 设置
 * Created by Wu Qizhen on 2024.6.29
 * Refactored by Wu Qizhen on 2024.11.30
 */
@Composable
fun SettingScreen() {
    val context = LocalContext.current
    val layoutPrefs = remember { LayoutPreferencesManager(context = context) }
    val isGirdLayout = remember { mutableStateOf(layoutPrefs.isGridLayout()) }

    XItem.Capsule(
        icon = R.drawable.ic_about,
        text = stringResource(R.string.about)
    ) {
        context.startActivity(Intent(context, AboutAppActivity::class.java))
    }

    Spacer(modifier = Modifier.height(5.dp))

    XItem.Capsule(
        icon = R.drawable.ic_privacy_security,
        text = stringResource(R.string.security)
    ) {
        val intent = Intent(context, PasswordCheckActivity::class.java)
        intent.putExtra("REQUEST_CODE", 0)
        context.startActivity(intent)
    }

    Spacer(modifier = Modifier.height(5.dp))

    XItem.Capsule(
        icon = R.drawable.ic_backup,
        text = stringResource(R.string.backup)
    ) {
        val intent = Intent(context, PasswordCheckActivity::class.java)
        intent.putExtra("REQUEST_CODE", 2)
        context.startActivity(intent)
    }

    Spacer(modifier = Modifier.height(5.dp))

    XItem.Capsule(
        icon = R.drawable.ic_recovery,
        text = stringResource(R.string.recovery)
    ) {
        val intent = Intent(context, PasswordCheckActivity::class.java)
        intent.putExtra("REQUEST_CODE", 3)
        context.startActivity(intent)
    }

    Spacer(modifier = Modifier.height(5.dp))

    XItem.Switch(
        icon = R.drawable.ic_layout,
        text = stringResource(R.string.gird_layout),
        subText = stringResource(R.string.homepage_gird_layout),
        status = isGirdLayout
    ) {
        isGirdLayout.value = !isGirdLayout.value
        layoutPrefs.setLayoutType(if (isGirdLayout.value) LayoutPreferencesManager.LAYOUT_TYPE_GRID else LayoutPreferencesManager.LAYOUT_TYPE_LIST)
        Toast.makeText(context, "设置将在重启应用后生效", Toast.LENGTH_SHORT).show()
    }

    Spacer(modifier = Modifier.height(50.dp))
}