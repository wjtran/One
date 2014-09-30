package onecorporation.one.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.View;

import java.util.Date;

/**
 * Model to hold the SQLite database for accounts
 * This is local only for prototype development, conversion to online scheduled for future
 */
public class LoginModel {

    /* variable declarations */
    private LoginModelHelper loginModelHelper;
    private SQLiteDatabase loginDatabase;
    private String currentUser;

    /* initialize login helper */
    public LoginModel(Context context) {
        currentUser = null;
        loginModelHelper = new LoginModelHelper(context);
        open();
    }

    /* open up the database */
    public void open() throws SQLException {
        loginDatabase = loginModelHelper.getWritableDatabase();
    }

    /* close the database */
    public void close() {
        loginDatabase.close();
    }

    /* return current username */
    public String getCurrentUser() {
        return currentUser;
    }

    /* login logic, return true if successful */
    public boolean login(String username, String password) {
        try {
            Cursor cursor = loginDatabase.rawQuery("SELECT * FROM " + loginModelHelper.usersTable +
                    " WHERE " + loginModelHelper.usersTableName + " = ?", new String[]{username.trim()});

            if (cursor.moveToNext()) {
                String databasePassword = cursor.getString(cursor.getColumnIndex(loginModelHelper.usersTablePassword));

                if (databasePassword.equals(password)) {
                    currentUser = username;
                    cursor.close();
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    /* account creation, return true if successful */
    public boolean create(String username, String password) {
        try {
            Cursor cursor = loginDatabase.rawQuery("SELECT * FROM " + loginModelHelper.usersTable +
                    " WHERE " + loginModelHelper.usersTableName + " = ?", new String[]{username.trim()});

            if (cursor.moveToNext()) {
                cursor.close();
                return false;
            }

            cursor.close();

            ContentValues entry = new ContentValues();

            entry.put(loginModelHelper.usersTableName, username);
            entry.put(loginModelHelper.usersTablePassword, password);
            entry.put(loginModelHelper.usersTableFirstName, "Temp First Name"); //TODO change here
            entry.put(loginModelHelper.usersTableLastName, "Temp Last Name"); // TODO change here too

            Date date = new Date();

            entry.put(loginModelHelper.usersTableCreationDate, date.toString());

            long row = loginDatabase.insert(LoginModelHelper.usersTable, null, entry);

            if (row != -1) {
                currentUser = username;
                return true;
            }

        } catch (Exception e) {
        }
        return false;
    }

    /* account deletion, return true if successful */
    public boolean delete(String username) {
        int row = loginDatabase.delete(loginModelHelper.usersTable, loginModelHelper.usersTableName + " = ?", new String[]{username});

        if (row != 0) {
            return true;
        } else {
            return false;
        }
    }

    /* TODO password changes, return true if successful */
    public boolean changePassword(String username, String password) {
        return false;
    }
}
