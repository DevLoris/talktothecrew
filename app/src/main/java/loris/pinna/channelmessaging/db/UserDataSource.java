/*
 * Copyright Loris Pinna
 * lorispinna.com =)
 */

package loris.pinna.channelmessaging.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserDataSource {
    // Database fields
    private SQLiteDatabase database;
    private FriendsDB dbHelper;
    private String[] allColumns = { FriendsDB.KEY_ID,FriendsDB.KEY_NAME,
            FriendsDB.KEY_IMAGE };
    public UserDataSource(Context context) {
        dbHelper = new FriendsDB(context);
    }
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();
    }
    public User createHomme(String nom,int age) {
        ContentValues values = new ContentValues();

        values.put(FriendsDB.KEY_NAME, nom);
        values.put(FriendsDB.KEY_IMAGE, age);
        long newID = database.insert(FriendsDB.HOMME_TABLE_NAME, null,
                values);
        Cursor cursor = database.query(FriendsDB.HOMME_TABLE_NAME, allColumns,
                FriendsDB.KEY_ID + " = \"" + newID+"\"", null,
                null, null, null);
        cursor.moveToFirst();
        User newHomme = cursorToUser(cursor);
        cursor.close();
        return newHomme;
    }

    public List<User> getAllUsers() {
        List<User> lesHommes = new ArrayList<User>();
        Cursor cursor = database.query(FriendsDB.HOMME_TABLE_NAME,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User unHomme = cursorToUser(cursor);
            lesHommes.add(unHomme);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return lesHommes;
    }

    private User cursorToUser(Cursor cursor) {
        User comment = new User();
        comment.setUserID(cursor.getInt(0));
        comment.setUsername(cursor.getString(1));
        comment.setImageUrl(cursor.getString(2));
        return comment;
    }

    public void deleteFriend(User unHomme) {
        int  id = unHomme.getUserID();
        database.delete(FriendsDB.HOMME_TABLE_NAME, FriendsDB.KEY_ID
                + " = \"" + id +"\"", null);
    }
}