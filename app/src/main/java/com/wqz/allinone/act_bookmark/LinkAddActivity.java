package com.wqz.allinone.act_bookmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wqz.allinone.R;
import com.wqz.allinone.database.BookmarkDBHelper;

import java.util.regex.Pattern;

public class LinkAddActivity extends AppCompatActivity {
    private EditText etName, etUrl;
    private BookmarkDBHelper bookmarkDBHelper;
    private int folderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_add);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        folderId = bundle.getInt("folder_id");
        bookmarkDBHelper = new BookmarkDBHelper(this);

        etName = findViewById(R.id.et_name);
        etUrl = findViewById(R.id.et_url);
        findViewById(R.id.btn_add).setOnClickListener(v -> addLink());
    }

    private void addLink() {
        String name = etName.getText().toString().trim();
        String url = etUrl.getText().toString().trim();

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

        bookmarkDBHelper.addLink(name, url, folderId);
        Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("is_add", 1);
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