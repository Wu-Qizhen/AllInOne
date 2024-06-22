package com.wqz.allinone.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wqz.allinone.entity.Todo;

import java.util.ArrayList;
import java.util.List;

public class TodoDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Todo.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TODO = "todos";
    // 表的列名
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_COMPLETED = "completed";

    public TodoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建语句
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_COMPLETED + " INTEGER"
                + ")";
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 在实际应用中，根据版本号进行数据库结构的升级
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(db);
    }

    // 添加一个新的待办
    public void addTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, todo.getTitle());
        values.put(COLUMN_COMPLETED, todo.isCompleted() ? 1 : 0);
        db.insert(TABLE_TODO, null, values);
        db.close();
    }

    public void addTodo(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_COMPLETED, false);
        db.insert(TABLE_TODO, null, values);
        db.close();
    }

    // 获取所有待办
    @SuppressLint("Range")
    public List<Todo> getAllTodos() {
        List<Todo> todos = new ArrayList<>();
        String SELECT_ALL_TODOS = "SELECT * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL_TODOS, null);
        if (cursor.moveToFirst()) {
            do {
                Todo todo = new Todo();
                todo.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                todo.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                todo.setCompleted(cursor.getInt(cursor.getColumnIndex(COLUMN_COMPLETED)) == 1);
                todos.add(todo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return todos;
    }

    @SuppressLint("Range")
    public List<Todo> getCompletedTodos() {
        List<Todo> todos = new ArrayList<>();
        String SELECT_UNCOMPLETED_TODOS = "SELECT * FROM " + TABLE_TODO + " WHERE " + COLUMN_COMPLETED + " = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_UNCOMPLETED_TODOS, null);
        if (cursor.moveToFirst()) {
            do {
                Todo todo = new Todo();
                todo.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                todo.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                todo.setCompleted(true);
                todos.add(todo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return todos;
    }

    @SuppressLint("Range")
    public List<Todo> getUncompletedTodos() {
        List<Todo> todos = new ArrayList<>();
        String SELECT_UNCOMPLETED_TODOS = "SELECT * FROM " + TABLE_TODO + " WHERE " + COLUMN_COMPLETED + " = 0";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_UNCOMPLETED_TODOS, null);
        if (cursor.moveToFirst()) {
            do {
                Todo todo = new Todo();
                todo.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                todo.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                todo.setCompleted(false);
                todos.add(todo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return todos;
    }

    // 更新一个待办
    public int updateTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, todo.getTitle());
        values.put(COLUMN_COMPLETED, todo.isCompleted() ? 1 : 0);
        return db.update(TABLE_TODO, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(todo.getId())});
    }

    public int updateTodo(int id, int completed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPLETED, completed);
        return db.update(TABLE_TODO, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // 删除一个待办
    public void deleteTodo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteCompletedTodos() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, COLUMN_COMPLETED + " = 1", null);
        db.close();
    }

    // 根据 ID 获取一个待办
    @SuppressLint("Range")
    public Todo getTodo(int id) {
        Todo todo = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TODO, new String[]{COLUMN_ID,
                        COLUMN_TITLE, COLUMN_COMPLETED}, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            todo = new Todo(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_COMPLETED)) == 1);
            cursor.close();
        }
        db.close();
        return todo;
    }
}