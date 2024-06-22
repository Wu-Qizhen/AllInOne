package com.wqz.allinone.act_bookmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.wqz.allinone.R;
import com.wqz.allinone.adapter.FolderAdapter;
import com.wqz.allinone.database.BookmarkDBHelper;
import com.wqz.allinone.entity.Folder;

import java.util.List;

public class BookmarkFoldersActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    private BookmarkDBHelper bookmarkDBHelper;
    private FolderAdapter adapter;
    private ActivityResultLauncher<Intent> folderAddRegister;
    private ActivityResultLauncher<Intent> folderChangeRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_folders);

        // 初始化数据库
        bookmarkDBHelper = new BookmarkDBHelper(this);

        // 初始化控件
        listView = findViewById(R.id.lv_folder_list);
        ImageView ivEmptyData = findViewById(R.id.iv_empty_data);
        listView.setEmptyView(ivEmptyData);

        // 初始化列表
        initListView();

        // 设置监听
        findViewById(R.id.ll_add_folder).setOnClickListener(this);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // 获取点击的 item 数据
            Folder item = (Folder) parent.getAdapter().getItem(position);
            // Log.d("Test", item.toString());
            // 查找数据
            Folder folder = bookmarkDBHelper.getFolder(item.getId());
            if (folder == null) {
                Toast.makeText(this, "数据获取异常，请刷新界面", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, BookmarkLinksActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("folder_id", folder.getId());
                bundle.putString("folder_name", folder.getName());
                intent.putExtras(bundle);
                // startActivity(intent);
                folderChangeRegister.launch(intent);

            }
        });

        folderAddRegister = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result != null) {
                Intent intent = result.getData();
                if (intent != null && result.getResultCode() == Activity.RESULT_OK) {
                    Bundle bundle = intent.getExtras();
                    // 从 Bundle 中取出数据
                    assert bundle != null;
                    int responseTime = bundle.getInt("is_add");
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

        folderChangeRegister = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result != null) {
                Intent intent = result.getData();
                if (intent != null && result.getResultCode() == Activity.RESULT_OK) {
                    Bundle bundle = intent.getExtras();
                    assert bundle != null;
                    int responseTime = bundle.getInt("is_edit");
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
        List<Folder> folders = bookmarkDBHelper.getFolders();
        if (folders != null && folders.size() != 0) {
            adapter = new FolderAdapter(this, folders);
            listView.setAdapter(adapter);
            setListViewHeightBasedOnChildren(listView);
        } /* else {
            // Toast.makeText(this, "数据为空", Toast.LENGTH_SHORT).show();
            listView.setEmptyView(ivEmptyData);
        } */
    }

    private void refreshListView() {
        List<Folder> folders = bookmarkDBHelper.getFolders();
        if (folders != null && folders.size() != 0) {
            if (adapter == null) {
                adapter = new FolderAdapter(this, folders);
                listView.setAdapter(adapter);
            } else {
                adapter.clear();
                adapter.addAll(folders);
                adapter.notifyDataSetChanged();
            }
            setListViewHeightBasedOnChildren(listView);
        } /* else {
            Toast.makeText(this, "数据为空", Toast.LENGTH_SHORT).show();
        } */
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_add_folder) {
            folderAddRegister.launch(new Intent(this, FolderAddActivity.class));
        }
    }
}