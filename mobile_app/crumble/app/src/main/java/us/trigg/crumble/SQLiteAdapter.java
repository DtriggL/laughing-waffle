package us.trigg.crumble;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SQLiteAdapter {

    public static final String DATABASE_NAME = "CRUMBLE";
    public static final String DATABASE_TABLE_MESSAGE = "MESSAGE";
    public static final int DATABASE_VERSION = 1;
    public static final String KEY_ID = "_id";
    public static final String KEY_CONTENT = "Content";

    private static final String SCRIPT_CREATE_TABLE_USER = Contract.Table_User.CREATE_TABLE;
    private static final String SCRIPT_CREATE_TABLE_CRUMB = Contract.Table_Crumb.CREATE_TABLE;
    private static final String SCRIPT_CREATE_TABLE_DISCOVERED = Contract.Table_Discovered.CREATE_TABLE;

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    private Context context;

    public SQLiteAdapter(Context c){
        context = c;
    }

    public SQLiteAdapter openToRead() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        return this;
    }

    public SQLiteAdapter openToWrite() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        sqLiteHelper.close();
    }

    public long insert(String content){

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CONTENT, content);
        return sqLiteDatabase.insert(DATABASE_TABLE_MESSAGE, null, contentValues);
    }

    public int deleteAll(){
        return sqLiteDatabase.delete(DATABASE_TABLE_MESSAGE, null, null);
    }

    public Cursor queueAll(){
        String[] columns = new String[]{KEY_ID, KEY_CONTENT};
        Cursor cursor = sqLiteDatabase.query(DATABASE_TABLE_MESSAGE, columns,
                null, null, null, null, null);

        return cursor;
    }

    public class SQLiteHelper extends SQLiteOpenHelper {

        public SQLiteHelper(Context context, String name,
                            CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(SCRIPT_CREATE_TABLE_USER);
            db.execSQL(SCRIPT_CREATE_TABLE_CRUMB);
            db.execSQL(SCRIPT_CREATE_TABLE_DISCOVERED);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

        }

    }

}