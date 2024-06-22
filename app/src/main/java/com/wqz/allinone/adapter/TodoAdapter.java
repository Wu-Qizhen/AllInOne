package com.wqz.allinone.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wqz.allinone.R;
import com.wqz.allinone.database.TodoDBHelper;
import com.wqz.allinone.entity.Todo;

import java.util.List;

public class TodoAdapter extends ArrayAdapter<Todo> {

    private final TodoDBHelper todoDBHelper;
    private final LayoutInflater inflater;
    private final List<Todo> todos;
    private final ListView listView;

    public TodoAdapter(Context context, List<Todo> objects, ListView listView) {
        super(context, R.layout.item_todo, objects);
        this.todos = objects;
        this.listView = listView;

        inflater = LayoutInflater.from(context);
        todoDBHelper = new TodoDBHelper(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_todo, parent, false);
        }

        Todo todo = getItem(position);

        CheckBox cbCompleted = convertView.findViewById(R.id.cb_completed);
        TextView tvTitle = convertView.findViewById(R.id.tv_title);

        assert todo != null;
        boolean completed = todo.isCompleted();
        tvTitle.setText(todo.getTitle());
        cbCompleted.setChecked(completed);
        if (completed) {
            tvTitle.setTextColor(Color.GRAY);
        }

        // 设置 CheckBox 的点击监听器来更新待办事项的状态
        cbCompleted.setOnClickListener(v -> {
            cbCompleted.setClickable(false);
            todo.toggleCompleted(); // 切换完成状态
            todoDBHelper.updateTodo(todo); // 更新数据库中的待办事项
            new Handler().postDelayed(() -> {
                todos.remove(todo);
                notifyDataSetChanged(); // 再次通知 ListView 更新视图
                setListViewHeightWithAnimation(listView); // 再次调整 ListView 高度
            }, 500); // 延迟 500 毫秒（0.5 秒）执行
        });

        return convertView;
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
}