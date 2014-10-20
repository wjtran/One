package onecorporation.one.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.Date;

import onecorporation.one.Data.PhotoEntry;

/**
 * Created by Wayne on 10/20/2014.
 */
public class PhotoModel {

    private PhotoModelHelper photoModelHelper;
    private SQLiteDatabase photoDatabase;

    public PhotoModel(Context context) {
        photoModelHelper = new PhotoModelHelper(context);
    }

    public void setUser(String username) {
        photoModelHelper.setUser(username);

        try {
            photoDatabase.close();
        } catch (Exception e) { }

        open();
    }

    private void open() throws SQLException {
        photoDatabase = photoModelHelper.getWritableDatabase();
    }

    public void close() {
        photoDatabase.close();
    }

    public PhotoEntry getPhotoByID(int id) {
        Cursor cursor = photoDatabase.rawQuery("SELECT * FROM " + photoModelHelper.photosTable
                      + " WHERE " + photoModelHelper.photosTableID + " = ?", new String[] {"" + id});

        PhotoEntry entry = new PhotoEntry();

        if (cursor.moveToNext()) {
            int ID = cursor.getInt(cursor.getColumnIndex(photoModelHelper.photosTableID));
            byte[] imageBlob = cursor.getBlob(cursor.getColumnIndex(photoModelHelper.photosTablePhoto));
            byte[] thumbBlob = cursor.getBlob(cursor.getColumnIndex(photoModelHelper.photosTableThumbnail));
            Date date = null;

            try {
                date = new Date(Long.parseLong((cursor.getString(cursor.getColumnIndex(photoModelHelper.photosTableDate)))));
            } catch (Exception e) { }

            entry.setID(ID);
            entry.setBitmapBytes(imageBlob);
            entry.setThumbnailBytes(thumbBlob);
            entry.setDate(date);
        }
        cursor.close();

        return entry;
    }

    public boolean addPhoto(PhotoEntry photo) {
        ContentValues entry = new ContentValues();

        entry.put(photoModelHelper.photosTablePhoto, photo.getBitmapBytes());
        entry.put(photoModelHelper.photosTableDate, "" + photo.getDate().getTime());
        entry.put(photoModelHelper.photosTableThumbnail, photo.getThumbnailBytes());

        long row = photoDatabase.insert(photoModelHelper.photosTable, null, entry);

        if (row != -1) {
            return true;
        }

        return false;
    }

    public boolean removePhoto(int id) {
        int row = photoDatabase.delete(photoModelHelper.photosTable, photoModelHelper.photosTableID + " = ?", new String[] {"" + id});

        if (row != 0) {
            return true;
        }

        return false;
    }

    public boolean updatePhoto(PhotoEntry photoEntry) {
        ContentValues entry = new ContentValues();

        entry.put(photoModelHelper.photosTableDate, "" + photoEntry.getDate().getTime());

        int row = photoDatabase.update(photoModelHelper.photosTable, entry, photoModelHelper.photosTableID + " = ?", new String[] {"" + photoEntry.getID()});

        if (row != 0)
            return true;

        return false;
    }

    public Uri getTemporaryImage() {
        return Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "tmp.jpg"));
    }
}
