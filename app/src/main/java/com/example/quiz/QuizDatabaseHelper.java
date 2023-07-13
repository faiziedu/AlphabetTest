package com.example.quiz;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class QuizDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "attempts.db";
    private static final String TABLE_ATTEMPTS = "attempts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ATTEMPT_NUMBER = "attempt_number";
    private static final String COLUMN_ANSWERS = "answers";

    public QuizDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ATTEMPTS_TABLE = "CREATE TABLE " + TABLE_ATTEMPTS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_ATTEMPT_NUMBER + " INTEGER," +
                COLUMN_ANSWERS + " TEXT" +
                ")";
        db.execSQL(CREATE_ATTEMPTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTEMPTS);
        onCreate(db);
    }

    public void addAttempt(int attemptNumber, List<String> answers) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ATTEMPT_NUMBER, attemptNumber);
        values.put(COLUMN_ANSWERS, convertListToString(answers));
        db.insert(TABLE_ATTEMPTS, null, values);

    }

    public List<String> getAttemptAnswers(int attemptNumber) {
        List<String> answersList = new ArrayList<>();
        String query = "SELECT " + COLUMN_ANSWERS + " FROM " + TABLE_ATTEMPTS +
                " WHERE " + COLUMN_ATTEMPT_NUMBER + " = " + attemptNumber;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String answers = cursor.getString(cursor.getColumnIndex(COLUMN_ANSWERS));
            answersList = convertStringToList(answers);
        }
        cursor.close();

        return answersList;
    }

    private String convertListToString(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(list.get(i));
        }
        return stringBuilder.toString();
    }

    private List<String> convertStringToList(String str) {
        List<String> list = new ArrayList<>();
        String[] items = str.split(",");
        for (String item : items) {
            list.add(item);
        }
        return list;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ATTEMPTS, null, null);

    }

    public List<List<String>> getAllAttempts() {
        List<List<String>> attemptList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ATTEMPTS;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String answers = cursor.getString(cursor.getColumnIndex(COLUMN_ANSWERS));
                List<String> attempt = convertStringToList(answers);
                attemptList.add(attempt);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return attemptList;
    }


}