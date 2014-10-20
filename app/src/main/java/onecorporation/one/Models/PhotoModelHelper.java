package onecorporation.one.Models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Wayne on 10/20/2014.
 */
public class PhotoModelHelper {
    public static String databaseName = "photos.db";

    public static final String photosTable = "photos";
    public static final String photosTableID = "id";
    public static final String photosTablePhoto = "photo";
    public static final String photosTableThumbnail = "thumbnail";
    public static final String photosTableAnnotation = "annotation";
    public static final String photosTableGroup = "groupName";
    public static final String photosTableTags = "tags";
    public static final String photosTableDoctorTags = "doctor";
    public static final String photosTableDate = "date";

    private static final int databaseVersion = 1;

    private static final String table = "CREATE TABLE " + photosTable + " (" + photosTableID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + photosTablePhoto + " BLOB, " + photosTableThumbnail + " BLOB, " + photosTableAnnotation + " TEXT, " + photosTableGroup + " TEXT, " + photosTableTags + " TEXT, " + photosTableDoctorTags + " TEXT, " + photosTableDate + " TEXT);";

    private static PhotoModelHelperInstance userDatabase;
    public Context context;

    public PhotoModelHelper(Context context) {
        this.context = context;
    }

    public void setUser(String username) {
        databaseName = new String(username + ".db");
        userDatabase = new PhotoModelHelperInstance(context);
    }

    public SQLiteDatabase getWritableDatabase() {
        return userDatabase.getWritableDatabase();
    }

    public class PhotoModelHelperInstance extends SQLiteOpenHelper {
        public PhotoModelHelperInstance(Context context) {
            super(context, databaseName, null, databaseVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(table);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            database.execSQL("DROP TABLE IF EXISTS " + photosTable);

            onCreate(database);
        }
    }
}
