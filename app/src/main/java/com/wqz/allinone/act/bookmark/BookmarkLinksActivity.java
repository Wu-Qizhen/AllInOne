package com.wqz.allinone.act.bookmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.wqz.allinone.R;
import com.wqz.allinone.adapter.LinkAdapter;
import com.wqz.allinone.database.BookmarkDBHelper;
import com.wqz.allinone.entity.Link;

import java.util.List;

public class BookmarkLinksActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    private BookmarkDBHelper bookmarkDBHelper;
    private LinkAdapter adapter;
    private Bundle bundle;
    private ActivityResultLauncher<Intent> linkAddRegister;
    private ActivityResultLauncher<Intent> linkChangeRegister;
    private ActivityResultLauncher<Intent> folderChangeRegister;
    private int folderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_links);

        // 初始化数据库
        bookmarkDBHelper = new BookmarkDBHelper(this);

        // 初始化控件
        TextView tvCompetitionTime = findViewById(R.id.tv_folder);
        bundle = getIntent().getExtras();
        assert bundle != null;
        folderId = bundle.getInt("folder_id");
        // List<Link> linksByFolder = bookmarkDBHelper.getLinksByFolder(folderId);
        tvCompetitionTime.setText(bundle.getString("folder_name"));

        listView = findViewById(R.id.lv_link_list);
        ImageView ivEmptyData = findViewById(R.id.iv_empty_data);
        listView.setEmptyView(ivEmptyData);

        // 初始化列表
        initListView();

        // 设置监听
        findViewById(R.id.ll_add_link).setOnClickListener(this);
        LinearLayout llEditFolder = findViewById(R.id.ll_edit_folder);
        if (folderId == 1) {
            llEditFolder.setVisibility(View.GONE);
        } else {
            llEditFolder.setOnClickListener(this);
        }

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // 获取点击的 item 数据
            Link item = (Link) parent.getAdapter().getItem(position);
            // 查找数据
            Link link = bookmarkDBHelper.getLink(item.getId());
            if (link.getId() < 0) {
                Toast.makeText(this, "数据获取异常，请刷新界面", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(BookmarkLinksActivity.this, LinkDetailsActivity.class);
                bundle.putInt("folder_id", folderId);
                bundle.putInt("link_id", link.getId());
                bundle.putString("link_name", link.getTitle());
                bundle.putString("link_url", link.getUrl());
                intent.putExtras(bundle);
                linkChangeRegister.launch(intent);
            }
        });

        linkAddRegister = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
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
                            Toast.makeText(this, "刷新失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        linkChangeRegister = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result != null) {
                Intent intent = result.getData();
                if (intent != null && result.getResultCode() == Activity.RESULT_OK) {
                    Bundle bundle = intent.getExtras();
                    // 从 Bundle 中取出数据
                    assert bundle != null;
                    int responseTime = bundle.getInt("is_edit");
                    if (responseTime == 1) {
                        try {
                            refreshListView();
                        } catch (Exception e) {
                            Toast.makeText(this, "刷新失败", Toast.LENGTH_SHORT).show();
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
                        Intent backIntent = new Intent();
                        Bundle backBundle = new Bundle();
                        backBundle.putInt("is_edit", 1);
                        backIntent.putExtras(backBundle);
                        setResult(Activity.RESULT_OK, backIntent);
                        finish();
                    }
                }
            }
        });
    }

    private void initListView() {
        List<Link> links = bookmarkDBHelper.getLinks(folderId);
        if (links != null && links.size() != 0) {
            adapter = new LinkAdapter(this, links);
            listView.setAdapter(adapter);
            setListViewHeightBasedOnChildren(listView);
        } /* else {
            Toast.makeText(this, "数据为空", Toast.LENGTH_SHORT).show();
        } */
    }

    private void refreshListView() {
        List<Link> links = bookmarkDBHelper.getLinks(folderId);
        if (links != null && links.size() != 0) {
            if (adapter == null) {
                adapter = new LinkAdapter(this, links);
                listView.setAdapter(adapter);
            } else {
                adapter.clear();
                adapter.addAll(links);
                adapter.notifyDataSetChanged();
            }
            setListViewHeightBasedOnChildren(listView);
        } else {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
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
        if (v.getId() == R.id.ll_add_link) {
            Intent intent = new Intent(this, LinkAddActivity.class);
            intent.putExtras(bundle);
            linkAddRegister.launch(intent);
        } else {
            Intent intent = new Intent(this, FolderDetailsActivity.class);
            intent.putExtras(bundle);
            folderChangeRegister.launch(intent);
        }
    }
}