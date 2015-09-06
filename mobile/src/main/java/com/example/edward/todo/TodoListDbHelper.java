package com.example.edward.todo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Edward on 9/5/2015.
 */
public class TodoListDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "todo.db";
    public static final String TABLE_NAME = "todoList_table";
    public static final String COLUMN_NAME_ENTRY_ID = "entryid";
    public static final String COLUMN_NAME_DESCRIPTION = "ITEM";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME +
            "(" +
            COLUMN_NAME_ENTRY_ID +      " INTEGER PRIMARY KEY," +
            COLUMN_NAME_DESCRIPTION +   " TEXT" +
            ")";

    public TodoListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
