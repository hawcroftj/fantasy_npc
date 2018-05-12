package hawcroftj.github.com.fantasynpc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "FantasyNPC.db";
    // NPC Character table
    public static final String CHARACTER_TABLE_NAME = "character_table";
    public static final String CHARACTER_COL_1 = "ID";
    public static final String CHARACTER_COL_2 = "FIRSTNAME";
    public static final String CHARACTER_COL_3 = "LASTNAME";
    public static final String CHARACTER_COL_4 = "RACE";
    public static final String CHARACTER_COL_5 = "CLASS";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CHARACTER_TABLE_NAME +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FIRSTNAME TEXT, LASTNAME TEXT, RACE TEXT, CLASS TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drops old table, and creates new table
        db.execSQL("DROP TABLE IF EXISTS " + CHARACTER_TABLE_NAME);
        onCreate(db);
    }
}
