package com.wqz.allinone.act.todo;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.wqz.allinone.R;
import com.wqz.allinone.adapter.TodoAdapter;
import com.wqz.allinone.database.TodoDBHelper;
import com.wqz.allinone.entity.Todo;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TodoListActivity extends AppCompatActivity implements View.OnClickListener {
    private TodoAdapter adapter;
    private TodoDBHelper todoDBHelper;
    private ListView listView;
    private ActivityResultLauncher<Intent> todoChangeRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        todoDBHelper = new TodoDBHelper(this);

        listView = findViewById(R.id.lv_todo_list);
        ImageView ivEmptyData = findViewById(R.id.iv_empty_data);
        listView.setEmptyView(ivEmptyData);

        // 初始化列表
        initListView();

        // 设置监听
        findViewById(R.id.ll_add_todo).setOnClickListener(this);
        findViewById(R.id.ll_completed_todo).setOnClickListener(this);

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

        todoChangeRegister = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result != null) {
                Intent intent = result.getData();
                if (intent != null && result.getResultCode() == Activity.RESULT_OK) {
                    Bundle bundle = intent.getExtras();
                    assert bundle != null;
                    int responseTime = bundle.getInt("is_change");
                    if (responseTime == 1) {
                        try {
                            refreshListView();
                        } catch (Exception e) {
                            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void initListView() {
        List<Todo> uncompletedTodos = todoDBHelper.getUncompletedTodos();
        if (uncompletedTodos != null && !uncompletedTodos.isEmpty()) {
            adapter = new TodoAdapter(this, uncompletedTodos, listView);
            listView.setAdapter(adapter);
            setListViewHeightBasedOnChildren(listView);
        }
    }

    private void refreshListView() {
        List<Todo> uncompletedTodos = todoDBHelper.getUncompletedTodos();
        if (adapter == null) {
            adapter = new TodoAdapter(this, uncompletedTodos, listView);
            listView.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(uncompletedTodos);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_add_todo) {
            todoChangeRegister.launch(new Intent(this, TodoAddActivity.class));
        } else {
            todoChangeRegister.launch(new Intent(this, TodoCompletedActivity.class));
        }
    }

    @SuppressLint("SetTextI18n")
    private void todoDetailsDialog(int id, String name) {
        // 初始化对话框
        View view = View.inflate(this, R.layout.dialog_todo_details, null);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(view);

        TextView tvName = dialog.findViewById(R.id.tv_name);
        tvName.setText(name);

        TextView tvNumber = dialog.findViewById(R.id.tv_todo_number);
        tvNumber.setText("* 这是第 " + id + " 条待办");

        Button confirmButton = dialog.findViewById(R.id.btn_delete);
        AtomicInteger deleteConfirm = new AtomicInteger(3);
        confirmButton.setOnClickListener(v -> {
            int count = deleteConfirm.decrementAndGet();
            if (count > 0) {
                Toast.makeText(this, "再点 " + count + " 次即可删除", Toast.LENGTH_SHORT).show();
            } else {
                // 执行删除操作
                todoDBHelper.deleteTodo(id);
                Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                refreshListView();
            }
        });
        // 显示对话框
        dialog.show();
    }
}