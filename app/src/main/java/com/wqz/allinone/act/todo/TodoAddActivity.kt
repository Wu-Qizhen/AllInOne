package com.wqz.allinone.act.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.wqz.allinone.R;
import com.wqz.allinone.database.TodoDBHelper;

public class TodoAddActivity extends AppCompatActivity {
    private EditText etName;
    private TodoDBHelper todoDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add);

        todoDBHelper = new TodoDBHelper(this);
        etName = findViewById(R.id.et_name);
        findViewById(R.id.btn_add).setOnClickListener(v -> addTodo());
    }

    private void addTodo() {
        String name = etName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.length() > 50) {
            Toast.makeText(this, "内容过长", Toast.LENGTH_SHORT).show();
            return;
        }
        /* if (name.contains("\n")) {
            Toast.makeText(this, "名称不符合要求", Toast.LENGTH_SHORT).show();
            return;
        } */

        todoDBHelper.addTodo(name);
        Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("is_change", 1);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}