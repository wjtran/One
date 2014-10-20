package onecorporation.one.Data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Date;

import onecorporation.one.LoginActivity;
import onecorporation.one.Utilities.SimpleCrypto;

/**
 * Created by Wayne on 10/20/2014.
 */
public class PhotoEntry implements Serializable {

    private int id;
    private Date date;
    private byte[] encryptedImage;
    private byte[] encryptedThumbnail;

    public PhotoEntry() {
        date = new Date();
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = null;

        byte[] decryptedBlob;

        try {
            decryptedBlob = SimpleCrypto.decrypt(SimpleCrypto.getRawKey(LoginActivity.ENCRYPTION_KEY.getBytes()), encryptedImage);
            bitmap = BitmapFactory.decodeByteArray(decryptedBlob, 0, decryptedBlob.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        try {
            Bitmap thumbnail = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            ByteArrayOutputStream photoOutput = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, photoOutput);
            byte[] bytes = photoOutput.toByteArray();
            photoOutput.flush();

            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, photoOutput);
            byte[] thumbytes = photoOutput.toByteArray();
            photoOutput.flush();

            encryptedImage = SimpleCrypto.encrypt(SimpleCrypto.getRawKey(LoginActivity.ENCRYPTION_KEY.getBytes()), bytes);
            encryptedThumbnail = SimpleCrypto.encrypt(SimpleCrypto.getRawKey(LoginActivity.ENCRYPTION_KEY.getBytes()), thumbytes);

            photoOutput.close();
        } catch (Exception e) { }
    }

    public byte[] getThumbnailBytes() {
        return encryptedThumbnail;
    }

    public void setThumbnailBytes(byte[] thumbnail) {
        encryptedThumbnail = thumbnail;
    }

    public Bitmap getThumbnail() {
        Bitmap bitmap = null;

        byte[] decryptedBlob;

        try {
            decryptedBlob = SimpleCrypto.decrypt(SimpleCrypto.getRawKey(LoginActivity.ENCRYPTION_KEY.getBytes()), encryptedThumbnail);
            bitmap = BitmapFactory.decodeByteArray(decryptedBlob, 0, decryptedBlob.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public byte[] getBitmapBytes() {
        return encryptedImage;
    }

    public void setBitmapBytes(byte[] bytes) {
        encryptedImage = bytes;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }
}
