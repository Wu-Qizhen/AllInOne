package com.wqz.allinone.act_todo;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wqz.allinone.R;
import com.wqz.allinone.adapter.TodoAdapter;
import com.wqz.allinone.database.TodoDBHelper;
import com.wqz.allinone.entity.Todo;

import java.util.List;

public class TodoCompletedActivity extends AppCompatActivity {
    private TodoDBHelper todoDBHelper;
    private ListView listView;
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_completed);

        todoDBHelper = new TodoDBHelper(this);

        listView = findViewById(R.id.lv_todo_list);
        ImageView ivEmptyData = findViewById(R.id.iv_empty_data);
        listView.setEmptyView(ivEmptyData);

        // 初始化列表
        initListView();

        // 设置监听
        findViewById(R.id.ll_clear_todo).setOnClickListener(v -> deleteConfirmDialog());

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // 获取点击的 item 数据
            Todo item = (Todo) parent.getAdapter().getItem(position);
            // 查找数据核实
            Todo todo = todoDBHelper.getTodo(item.getId());
            if (todo == null) {
                Toast.makeText(this, "数据获取异常，请刷新界面", Toast.LENGTH_SHORT).show();
            } else {
                todoDetailsDialog(todo.getId(), todo.getTitle());
            }
        });

        Intent intent = new Intent();
        Bundle back = new Bundle();
        back.putInt("is_change", 1);
        intent.putExtras(back);
        setResult(Activity.RESULT_OK, intent);
    }

    private void initListView() {
        List<Todo> completedTodos = todoDBHelper.getCompletedTodos();
        if (completedTodos != null && completedTodos.size() != 0) {
            adapter = new TodoAdapter(this, completedTodos, listView);
            listView.setAdapter(adapter);
            setListViewHeightBasedOnChildren(listView);
        }
    }

    private void refreshListView() {
        List<Todo> completedTodos = todoDBHelper.getCompletedTodos();
        if (adapter == null) {
            adapter = new TodoAdapter(this, completedTodos, listView);
            listView.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(completedTodos);
            adapter.notifyDataSetChanged();
        }
        setListViewHeightWithAnimation(listView);
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取 ListView 对应的 Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount() 返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项 View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight() 获取子项间分隔符占用的高度
        // params.height 最后得到整个 ListView 完整显示需要的高度
        listView.setLayoutParams(params);
    }

    private void setListViewHeightWithAnimation(ListView listView) {
        // 获取当前 ListView 的高度
        int initialHeight = listView.getLayoutParams().height;

        // 获取 ListView 对应的 Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int targetHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount() 返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项 View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            targetHeight += listItem.getMeasuredHeight();
        }
        targetHeight += listView.getDividerHeight() * (listAdapter.getCount() - 1);

        // 使用动画过渡到新的高度
        // 使用 ValueAnimator 为 ListView 高度变化添加动画
        ValueAnimator animator = ValueAnimator.ofInt(initialHeight, targetHeight);
        animator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
            layoutParams.height = animatedValue;
            listView.setLayoutParams(layoutParams);
        });
        animator.setDuration(500); // 设置动画持续时间
        animator.start();
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
            todoDBHelper.deleteCompletedTodos();
            Toast.makeText(this, "已清空", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            finish();
        });
        // 显示对话框
        dialog.show();
    }

    private void todoDetailsDialog(int id, String name) {
        // 初始化对话框
        View view = View.inflate(this, R.layout.dialog_todo_details, null);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(view);

        TextView textView = dialog.findViewById(R.id.tv_name);
        textView.setText(name);

        Button confirmButton = dialog.findViewById(R.id.btn_delete);
        confirmButton.setOnClickListener(v -> {
            // 执行删除操作
            todoDBHelper.deleteTodo(id);
            Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            refreshListView();
        });
        // 显示对话框
        dialog.show();
    }
}