/*
 * Copyright Loris Pinna
 * lorispinna.com =)
 */

package loris.pinna.channelmessaging.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

/**
 * Created by pinnal on 29/01/2018.
 */
public class FriendsDB extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 3;
        private static final String DATABASE_NAME = "MyDB.db";
        public static final String USER_TABLENAME = "Friends";
        public static final String KEY_ID = "userID";
        public static final String KEY_NAME = "username";
        public static final String KEY_IMAGE = "imageUrl";
        private static final String HOMME_TABLE_CREATE = "CREATE TABLE " + USER_TABLENAME + "   (" + KEY_ID + " INTEGER PRIMARY KEY, " +    KEY_NAME + " TEXT, " + KEY_IMAGE + " TEXT);";
        public FriendsDB(Context context) {
            super(context, Environment.getExternalStorageDirectory()+"/"+DATABASE_NAME, null,
                    DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(HOMME_TABLE_CREATE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + USER_TABLENAME);
            onCreate(db);
        }
}
