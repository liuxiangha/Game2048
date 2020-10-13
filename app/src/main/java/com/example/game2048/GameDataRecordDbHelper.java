package com.example.game2048;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.game2048.GameDataRecordContract.*;

public class GameDataRecordDbHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_GAME_2048_ENTRIES = "CREATE TABLE " +
            Game2048Entity.TABLE_NAME + "(" + Game2048Entity.COLUMN_NAME_TITLE + " BINGINT PRIMARY KEY, " +
            Game2048Entity.COLUMN_NAME_SECOND_TITLE + " INTEGER NOT NULL)";
    private static final String SQL_INSERT_INITIAL = "INSERT INTO game_2048 VALUES(0, 0)";
    private static final String SQL_DELETE_GAME_2048_ENTRIES =
            "DROP TABLE IF EXISTS " + Game2048Entity.TABLE_NAME;
    private static final String SQL_CREATE_GAME_BOARD_ENTRIES = "CREATE TABLE " +
            GameBoardEntity.TABLE_NAME + "(" + GameBoardEntity.COLUMN_NAME_TITLE + " BIGBINT PRIMARY KEY, " +
            GameBoardEntity.COLUMN_NAME_SECOND_TITLE + " INTEGER NOT NULL)";
    private static final String SQL_DELETE_GAME_Board_ENTRIES =
            "DROP TABLE IF EXISTS " + GameBoardEntity.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GameDataRecord.db";

    public GameDataRecordDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_GAME_2048_ENTRIES);
        db.execSQL(SQL_INSERT_INITIAL);
        db.execSQL(SQL_CREATE_GAME_BOARD_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_GAME_2048_ENTRIES);
        db.execSQL(SQL_INSERT_INITIAL);
        db.execSQL(SQL_DELETE_GAME_Board_ENTRIES);
        onCreate(db);
    }
}
