package onecorporation.one.Models;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper class to handle creates and updates
 */
public class LoginModelHelper extends SQLiteOpenHelper {
    /* constant declarations */
    public static final String databaseName = "admin.db";
    public static final String usersTable = "users";
    public static final String usersTableID = "id";
    public static final String usersTableName = "name";
    public static final String usersTablePassword = "password";
    public static final String usersTableFirstName = "firstname";
    public static final String usersTableLastName = "lastname";
    public static final String usersTableCreationDate = "creationdate";
    public static final String notNull = " TEXT NOT NULL, ";

    /* database housekeeping */
    private static final int databaseVersion = 1;
    private static final String createTable = "CREATE TABLE " + usersTable + " (" + usersTable +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + usersTableName + notNull +
            usersTablePassword + notNull + usersTableFirstName + notNull + usersTableLastName +
            notNull + usersTableCreationDate + " TEXT NOT NULL);";

    public LoginModelHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(createTable);

        ContentValues entry = new ContentValues();

        //TODO: have some encryption in database
        Date date = new Date();

        entry.put(usersTableFirstName, "");
        entry.put(usersTableLastName, "");
        entry.put(usersTableCreationDate, date.toString());

        database.insert(usersTable, null, entry);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + usersTable);
        onCreate(database);
    }
}
