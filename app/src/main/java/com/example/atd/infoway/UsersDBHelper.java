package com.example.atd.infoway;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by atd on 9/13/2016.
 */
public class UsersDBHelper extends SQLiteOpenHelper {
    private static UsersDBHelper sInstance;
    // Database Info
    private static final String DATABASE_NAME = "usersDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ITEMS = "items";


    // Users Table Columns
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_USERNAME = "userName";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_FIRST_NAME = "firstName";
    private static final String KEY_USER_LAST_NAME = "lastName";
    private static final String KEY_USER_AGE = "age";
    private static final String KEY_USER_GENDER = "gender";

    // Items Table Columns
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_USER_ID_FK = "userId";
    private static final String KEY_ITEM_NAME = "itemName";
    private static final String KEY_ITEM_PIC = "itemPic";
    // Item status is boolean, 0 = false, 1= true
    private static final String KEY_ITEM_DONE = "itemDone";

    // create a new user
    User NewUser = User.getInstance();

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private UsersDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," +
                KEY_USER_USERNAME + " TEXT," +
                KEY_USER_PASSWORD + " INTEGER," +
                KEY_USER_FIRST_NAME + " TEXT," +
                KEY_USER_LAST_NAME + " TEXT," +
                KEY_USER_AGE + " INTEGER," +
                KEY_USER_GENDER + " TEXT" +
                ")";

        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_ITEM_USER_ID_FK + " INTEGER REFERENCES " + TABLE_USERS + "," + // Define a foreign key
                KEY_ITEM_NAME + " TEXT," +
                KEY_ITEM_PIC + " TEXT," +
                KEY_ITEM_DONE + " INTEGER DEFAULT 0);";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public static synchronized UsersDBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new UsersDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    // Insert a new user into the database
    public void addUser(User user) {

        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();


        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_USER_USERNAME, NewUser.getUsername());
            values.put(KEY_USER_PASSWORD, user.getPassword().hashCode());
            values.put(KEY_USER_FIRST_NAME, user.getFirstName());
            values.put(KEY_USER_LAST_NAME, user.getLastName());
            values.put(KEY_USER_GENDER, user.getGender());
            values.put(KEY_USER_AGE, user.getAge());

            db.insertOrThrow(TABLE_USERS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("ERROR:", "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    // Get a user firstname in the database
    public int getPassHash(String userName) {

        Log.wtf("WTF:", "GetPassHash called, username: " + userName);

        String[] projection = {
                KEY_USER_ID,
                KEY_USER_USERNAME,
                KEY_USER_PASSWORD,
                KEY_USER_FIRST_NAME,
                KEY_USER_LAST_NAME,
                KEY_USER_GENDER,
                KEY_USER_AGE,
        };

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();

        String selection = KEY_USER_USERNAME + " = ?";
        String[] selectionArgs = {userName};


        Cursor c = db.query(
                TABLE_USERS,                              // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't The sort order
        );


        if (c != null && c.moveToFirst()) {
            int hash = c.getInt(c.getColumnIndex(KEY_USER_PASSWORD));
            Log.d("w", "user: " + c.getString(c.getColumnIndex(KEY_USER_USERNAME)));
            Log.d("w", "hash " + c.getInt(c.getColumnIndex(KEY_USER_PASSWORD)));
            Log.d("w", "first: " + c.getString(c.getColumnIndex(KEY_USER_FIRST_NAME)));
            Log.d("w", "last: " + c.getString(c.getColumnIndex(KEY_USER_LAST_NAME)));
            Log.d("w", "gender: " + c.getString(c.getColumnIndex(KEY_USER_GENDER)));
            c.close();
            return hash;
        } else {
            c.close();
            return 0;
        }
    }


    //this method takes username and sets gloabel user attribatues
    public void setUser(String usern) {
        String[] projection = {
                KEY_USER_ID,
                KEY_USER_USERNAME,
                KEY_USER_PASSWORD,
                KEY_USER_FIRST_NAME,
                KEY_USER_LAST_NAME,
                KEY_USER_GENDER,
                KEY_USER_AGE,
        };


        SQLiteDatabase db = getReadableDatabase();

        String selection = KEY_USER_USERNAME + " = ?";
        String[] selectionArgs = {usern};


        Cursor c = db.query(
                TABLE_USERS,                              // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't The sort order
        );

        if (c != null && c.moveToFirst()) {
            NewUser.setUserID(c.getInt(c.getColumnIndex(KEY_USER_ID)));
            NewUser.setUsername(c.getString(c.getColumnIndex(KEY_USER_USERNAME)));
            NewUser.setFirstName(c.getString(c.getColumnIndex(KEY_USER_FIRST_NAME)));
            NewUser.setLastName(c.getString(c.getColumnIndex(KEY_USER_LAST_NAME)));
            NewUser.setGender(c.getString(c.getColumnIndex(KEY_USER_GENDER)));
            NewUser.setAge(c.getInt(c.getColumnIndex(KEY_USER_AGE)));
            c.close();
        } else {
            c.close();
        }

    }
    // Add new item
    // Insert a post into the database
    public void addItem(Item item) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_NAME, item.name);
            values.put(KEY_ITEM_USER_ID_FK, item.user.getUserID());
            values.put(KEY_ITEM_PIC, item.pic);

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_ITEMS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            // something wrong happened
        } finally {
            db.endTransaction();
        }
    }

    // Get all posts in the database
    public List<Item> getUserItems(User user) {
        List<Item> posts = new ArrayList<>();
Log.d("CALLED", "GET all  items called");
        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s = %s",
                        TABLE_ITEMS,
                        KEY_ITEM_USER_ID_FK,
                        user.getUserID());

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Log.d("TAG", "Item Name: " + cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME)) + "\nUsr ID: " + cursor.getString(cursor.getColumnIndex(KEY_ITEM_USER_ID_FK)) +"\nItem Pic: " + cursor.getString(cursor.getColumnIndex(KEY_ITEM_PIC)));


                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return posts;
    }




}
