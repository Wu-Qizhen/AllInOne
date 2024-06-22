package com.wqz.allinone.act_bookmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wqz.allinone.R;
import com.wqz.allinone.database.BookmarkDBHelper;

public class FolderAddActivity extends AppCompatActivity {
    private EditText etName;
    private BookmarkDBHelper bookmarkDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_add);

        bookmarkDBHelper = new BookmarkDBHelper(this);
        etName = findViewById(R.id.et_name);
        findViewById(R.id.btn_add).setOnClickListener(v -> addFolder());
    }

    private void addFolder() {
        String name = etName.getText().toString().trim();
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

        boolean l = bookmarkDBHelper.addFolder(name);
        if (l) {
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("is_add", 1);
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
        }
    }
}