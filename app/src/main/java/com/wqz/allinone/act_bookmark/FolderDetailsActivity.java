package com.wqz.allinone.act_bookmark;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wqz.allinone.R;
import com.wqz.allinone.database.BookmarkDBHelper;
import com.wqz.allinone.entity.Folder;

public class FolderDetailsActivity extends AppCompatActivity {

    private BookmarkDBHelper bookmarkDBHelper;
    private int folderId;
    private String folderName;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_details);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        folderId = bundle.getInt("folder_id");
        folderName = bundle.getString("folder_name");

        TextView tvName = findViewById(R.id.tv_name);
        tvName.setText(folderName);

        bookmarkDBHelper = new BookmarkDBHelper(this);
        TextView tvBookmarkNumber = findViewById(R.id.tv_bookmark_number);
        tvBookmarkNumber.setText(String.valueOf(bookmarkDBHelper.getLinkCount(folderId)));

        findViewById(R.id.btn_delete_folder).setOnClickListener(v -> deleteConfirmDialog());
        findViewById(R.id.btn_modify_folder).setOnClickListener(v -> {
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
            bookmarkDBHelper.deleteLinks(folderId);
            bookmarkDBHelper.deleteFolder(folderId);
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
        View view = View.inflate(this, R.layout.dialog_folder_modify, null);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(view);

        EditText etName = dialog.findViewById(R.id.et_name);
        etName.setText(folderName);

        Button modifyButton = dialog.findViewById(R.id.btn_modify);
        modifyButton.setOnClickListener(v -> {
            modifyFolder(etName.getText().toString().trim());
            dialog.dismiss(); // 关闭对话框
        });
        // 显示对话框
        dialog.show();
    }

    private void modifyFolder(String name) {
        if (name.equals(folderName)) {
            Toast.makeText(this, "未作出修改", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.isEmpty()) {
            Toast.makeText(this, "名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.length() > 10 || name.length() < 2) {
            Toast.makeText(this, "名称长度不符合要求", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.contains("\n")) {
            Toast.makeText(this, "名称不符合要求", Toast.LENGTH_SHORT).show();
            return;
        }

        bookmarkDBHelper.updateFolder(new Folder(folderId, name));
        Toast.makeText(this, "修改成功，请刷新界面", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("is_edit", 1);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}