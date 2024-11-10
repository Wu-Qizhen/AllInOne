package com.wqz.allinone.act.bookmark;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wqz.allinone.R;
import com.wqz.allinone.database.BookmarkDBHelper;
import com.wqz.allinone.entity.Link;

import java.util.regex.Pattern;

public class LinkDetailsActivity extends AppCompatActivity {

    private TextView tvUrl;
    private BookmarkDBHelper bookmarkDBHelper;
    private int linkId;
    private int folderId;
    private String linkName;
    private String linkUrl;
    // private boolean isDelete = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_details);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        folderId = bundle.getInt("folder_id");
        linkId = bundle.getInt("link_id");
        linkName = bundle.getString("link_name");
        linkUrl = bundle.getString("link_url");

        TextView tvName = findViewById(R.id.tv_name);
        tvName.setText(linkName);
        tvUrl = findViewById(R.id.tv_url);
        tvUrl.setText(linkUrl);

        bookmarkDBHelper = new BookmarkDBHelper(this);

        tvUrl.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "无法打开链接", Toast.LENGTH_SHORT).show();
            }
        });

        tvUrl.setOnLongClickListener(v -> {
            // 获取系统剪贴板服务
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建 ClipData 对象，第一个参数是剪贴板数据的 MIME 类型，第二个参数是标签，第三个参数是要复制的内容
            ClipData clip = ClipData.newPlainText("simple text", linkUrl);
            // 把数据集设置（复制）到剪贴板
            clipboard.setPrimaryClip(clip);
            // 可以选择性地显示一个提示，告知用户文本已复制
            Toast.makeText(getApplicationContext(), "已复制链接", Toast.LENGTH_SHORT).show();
            return false;
        });

        tvUrl.setOnTouchListener((v, event) -> {
            ScaleAnimation zoomInAnimation = new ScaleAnimation(1f, 0.95f, 1f, 0.95f, v.getPivotX(), v.getPivotY());
            zoomInAnimation.setDuration(150); // 设置动画持续时间
            zoomInAnimation.setFillAfter(true); // 保持动画结束后的状态

            ScaleAnimation zoomOutAnimation = new ScaleAnimation(0.95f, 1f, 0.95f, 1f, v.getPivotX(), v.getPivotY());
            zoomOutAnimation.setDuration(150); // 设置动画持续时间
            zoomOutAnimation.setFillAfter(true); // 保持动画结束后的状态

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                tvUrl.startAnimation(zoomInAnimation);

            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                tvUrl.startAnimation(zoomOutAnimation);
            }
            return false;
        });

        findViewById(R.id.btn_delete_link).setOnClickListener(v -> deleteConfirmDialog());
        findViewById(R.id.btn_modify_link).setOnClickListener(v -> {
            try {
                modifyLinkDialog();
            } catch (Exception e) {
                Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteConfirmDialog() {
        // 初始化对话框
        View view = View.inflate(this, R.layout.dialog_delete_confirm, null);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(view);

        Button cancelButton = dialog.findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(v -> {
            dialog.dismiss(); // 关闭对话框
        });

        Button confirmButton = dialog.findViewById(R.id.btn_delete);
        confirmButton.setOnClickListener(v -> {
            // 执行删除操作
            bookmarkDBHelper.deleteLink(linkId);
            Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            Intent intent = new Intent();
            Bundle back = new Bundle();
            back.putInt("is_edit", 1);
            intent.putExtras(back);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
        // 显示对话框
        dialog.show();
    }

    private void modifyLinkDialog() {
        // 初始化对话框
        View view = View.inflate(this, R.layout.dialog_link_modify, null);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(view);

        EditText etName = dialog.findViewById(R.id.et_name);
        EditText etUrl = dialog.findViewById(R.id.et_url);
        etName.setText(linkName);
        etUrl.setText(linkUrl);

        Button modifyButton = dialog.findViewById(R.id.btn_modify);
        modifyButton.setOnClickListener(v -> {
            updateLink(etName.getText().toString().trim(), etUrl.getText().toString().trim());
            dialog.dismiss(); // 关闭对话框
        });
        // 显示对话框
        dialog.show();
    }

    private void updateLink(String name, String url) {
        if (name.equals(linkName) && url.equals(linkUrl)) {
            Toast.makeText(this, "未作出修改", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.isEmpty() || url.isEmpty()) {
            Toast.makeText(this, "名称或链接不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.length() > 25 || name.length() < 2) {
            Toast.makeText(this, "名称长度不符合要求", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.contains("\n")) {
            Toast.makeText(this, "名称格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidUrl(url)) {
            Toast.makeText(this, "链接格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }

        bookmarkDBHelper.updateLink(new Link(linkId, name, url, folderId));
        Toast.makeText(this, "修改成功，请刷新界面", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("is_edit", 1);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private boolean isValidUrl(String url) {
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(url).matches();
    }
}