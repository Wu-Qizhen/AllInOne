package com.wqz.allinone.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wqz.allinone.entity.Folder;
import com.wqz.allinone.entity.Link;

import java.util.ArrayList;
import java.util.List;

public class BookmarkDBHelper extends SQLiteOpenHelper {
    // 数据库版本
    private static final int DATABASE_VERSION = 1;
    // 数据库名称
    private static final String DATABASE_NAME = "Bookmark.db";

    // Folder 表
    private static final String TABLE_FOLDER = "folder";
    private static final String KEY_FOLDER_ID = "id";
    private static final String KEY_FOLDER_NAME = "name";

    // Link 表
    private static final String TABLE_LINK = "link";
    private static final String KEY_LINK_ID = "id";
    private static final String KEY_LINK_TITLE = "title";
    private static final String KEY_LINK_URL = "url";
    private static final String KEY_LINK_FOLDER = "folder"; // 假设这是一个外键，指向 Folder 的 id

    public BookmarkDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 创建数据库表
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FOLDER_TABLE = "CREATE TABLE " + TABLE_FOLDER + "("
                + KEY_FOLDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_FOLDER_NAME + " TEXT NOT NULL UNIQUE"
                + ")";
        String CREATE_LINK_TABLE = "CREATE TABLE " + TABLE_LINK + "("
                + KEY_LINK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_LINK_TITLE + " TEXT NOT NULL,"
                + KEY_LINK_URL + " TEXT NOT NULL,"
                + KEY_LINK_FOLDER + " INTEGER NOT NULL,"
                + "FOREIGN KEY(" + KEY_LINK_FOLDER + ") REFERENCES " + TABLE_FOLDER + "(" + KEY_FOLDER_ID + ")"
                + ")";
        db.execSQL(CREATE_FOLDER_TABLE);
        db.execSQL(CREATE_LINK_TABLE);

        ContentValues values = new ContentValues();
        values.put(KEY_FOLDER_NAME, "默认收藏夹");
        // 插入行
        db.insert(TABLE_FOLDER, null, values);
    }

    // 更新数据库版本
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果需要，在这里添加代码来更新数据库结构
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LINK);
        onCreate(db);
    }

    // 添加新的 Folder
    public boolean addFolder(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FOLDER_NAME, name);

        // 插入行
        try {
            // 插入行，如果 name 已经存在，将会抛出 SQLiteConstraintException
            db.insertOrThrow(TABLE_FOLDER, null, values);
            return true;
        } catch (SQLiteConstraintException e) {
            // 处理重复值的异常
            return false;
            // 可能需要通知用户或者进行其他错误处理
        } finally {
            db.close();
        }
    }

    // 获取一个 Folder
    public Folder getFolder(int folderId) {
        SQLiteDatabase db = this.getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.query(TABLE_FOLDER, new String[]{KEY_FOLDER_ID, KEY_FOLDER_NAME}, KEY_FOLDER_ID + "=?",
                new String[]{String.valueOf(folderId)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Folder folder = new Folder();
        assert cursor != null;
        folder.setId(Integer.parseInt(cursor.getString(0)));
        folder.setName(cursor.getString(1));

        return folder;
    }

    // 获取所有 LinkFolders
    @SuppressLint("Range")
    public List<Folder> getFolders() {
        List<Folder> folders = new ArrayList<>();
        // 实现获取所有 Folder 的代码
        String selectQuery = "SELECT * FROM " + TABLE_FOLDER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Folder folder = new Folder();
                folder.setId(cursor.getInt(cursor.getColumnIndex(KEY_FOLDER_ID)));
                folder.setName(cursor.getString(cursor.getColumnIndex(KEY_FOLDER_NAME)));
                folders.add(folder);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // 查询数据库并填充 folders 列表
        return folders;
    }

    // 更新 Folder
    public void updateFolder(Folder folder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FOLDER_NAME, folder.getName());

        // 更新行
        db.update(TABLE_FOLDER, values, KEY_FOLDER_ID + "=?",
                new String[]{String.valueOf(folder.getId())});
    }

    // 删除 Folder
    public void deleteFolder(int folderId) {
        if (folderId == 1) {
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOLDER, KEY_FOLDER_ID + "=?", new String[]{String.valueOf(folderId)});
        db.close();
    }

    // 获取 Folder 的数量
    public int getFolderCount() {
        String countQuery = "SELECT * FROM " + TABLE_FOLDER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }


    // 添加新的 Link
    public void addLink(String title, String url, int folderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LINK_TITLE, title);
        values.put(KEY_LINK_URL, url);
        values.put(KEY_LINK_FOLDER, folderId);
        // 插入行
        db.insert(TABLE_LINK, null, values);
        db.close();
    }

    // 获取一个 Link
    @SuppressLint("Range")
    public Link getLink(int linkId) {
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.query(TABLE_LINK, new String[]{KEY_LINK_ID, KEY_LINK_TITLE, KEY_LINK_URL, KEY_LINK_FOLDER}, KEY_LINK_ID + "=?",
                new String[]{String.valueOf(linkId)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Link link = new Link();
        assert cursor != null;
        link.setId(cursor.getInt(cursor.getColumnIndex(KEY_LINK_ID)));
        link.setTitle(cursor.getString(cursor.getColumnIndex(KEY_LINK_TITLE)));
        link.setUrl(cursor.getString(cursor.getColumnIndex(KEY_LINK_URL)));
        link.setFolder(cursor.getInt(cursor.getColumnIndex(KEY_LINK_FOLDER)));
        return link;
    }

    // 获取所有 Links
    @SuppressLint("Range")
    public List<Link> getLinks() {
        List<Link> links = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_LINK;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Link link = new Link();
                link.setId(cursor.getInt(cursor.getColumnIndex(KEY_LINK_ID)));
                link.setTitle(cursor.getString(cursor.getColumnIndex(KEY_LINK_TITLE)));
                link.setUrl(cursor.getString(cursor.getColumnIndex(KEY_LINK_URL)));
                link.setFolder(cursor.getInt(cursor.getColumnIndex(KEY_LINK_FOLDER)));
                links.add(link);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return links;
    }

    @SuppressLint("Range")
    public List<Link> getLinks(int folderId) {
        List<Link> links = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_LINK + " WHERE " + KEY_LINK_FOLDER + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(folderId)});

        if (cursor.moveToFirst()) {
            do {
                Link link = new Link();
                link.setId(cursor.getInt(cursor.getColumnIndex(KEY_LINK_ID)));
                link.setTitle(cursor.getString(cursor.getColumnIndex(KEY_LINK_TITLE)));
                link.setUrl(cursor.getString(cursor.getColumnIndex(KEY_LINK_URL)));
                link.setFolder(cursor.getInt(cursor.getColumnIndex(KEY_LINK_FOLDER)));

                links.add(link);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return links;
    }

    @SuppressLint("Range")
    public int getLinkCount(int folderId) {
        String selectQuery = "SELECT * FROM " + TABLE_LINK + " WHERE " + KEY_LINK_FOLDER + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(folderId)});
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    // 更新 Link
    public int updateLink(Link link) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LINK_TITLE, link.getTitle());
        values.put(KEY_LINK_URL, link.getUrl());
        values.put(KEY_LINK_FOLDER, link.getFolder());
        // 更新行
        return db.update(TABLE_LINK, values, KEY_LINK_ID + "=?",
                new String[]{String.valueOf(link.getId())});
    }

    // 删除 Link
    public void deleteLink(int linkId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LINK, KEY_LINK_ID + "=?", new String[]{String.valueOf(linkId)});
        db.close();
    }

    public void deleteLinks(int folderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LINK, KEY_LINK_FOLDER + "=?", new String[]{String.valueOf(folderId)});
        db.close();
    }
}