package com.example.jobsuche_app;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


class JobDB{
    public static final String JOB_TABLE = "job_data";

    public static final String KEY_ROWID = "_id";
    public static final String STELLE = "stelle";
    public static final String LOGO = "logo";
    public static final String BUNDESLAND = "bundesland";
    public static final String EMAIL = "email";
    public static final String STRASSE = "strasse";
    public static final String ACTIV = "activ";
    public static final String AUFGABE = "aufgabe";
    public static final String BERUFSFELD= "berufsfeld";

    public static final String JOB_TABLE_CREATE = "create table "
            + JOB_TABLE
            + " (" + KEY_ROWID + " integer primary key autoincrement, "
            + STELLE + " text not null, "
            + LOGO + " text not null, "
            + BERUFSFELD + " text not null, "
            + AUFGABE + " text not null, "
            + ACTIV + " text, "
            + BUNDESLAND + " text, "
            + EMAIL + " text, "
            + STRASSE + " text)";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " +  JOB_TABLE;
public static class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "jobs_data";
    private static final int DATABASE_VERSION = 1;

    private final Resources mResources;
    private SQLiteDatabase mDb;
    private JobDB mDbHelper;
    private Context mContext;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mResources = context.getResources();
        mContext = context;
        mDb = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(JOB_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    private void readDataToDb(SQLiteDatabase db) {
        String jsonData = readJsonFromFile();
        try {
            JSONArray arr = new JSONArray(jsonData);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String stelle = obj.getString("Bezeichnung der Stelle");
                String logo = obj.getString("Logo");
                String email = obj.getString("E-Mail");
                String bundesland = obj.getString("Bundesland");
                String aufgabe = obj.getString("Aufgabengebiet");
                String strasse = obj.getString("Straße");
                JSONArray berufsfeld = obj.getJSONArray("Berufsfeld");
                String[] bFeldeArr = new String[berufsfeld.length()];
                for (int j = 0; j < berufsfeld.length(); j++) {
                    bFeldeArr[i] = berufsfeld.getString(i);
                }
                String activ = obj.getString("Stelle aktiv bis (Publikationsende)");

                ContentValues values = new ContentValues();
                values.put(STELLE, stelle);
                values.put(LOGO, logo);
                values.put(EMAIL, email);
                values.put(AUFGABE, aufgabe);
                values.put(BERUFSFELD, String.valueOf(bFeldeArr));
                values.put(STRASSE, strasse);
                values.put(ACTIV, activ);
                values.put(BUNDESLAND, bundesland);

                db.insert(JOB_TABLE, null, values);
                db.close();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String readJsonFromFile() {
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        String jsonDataString = null;
        is = mResources.openRawResource(R.raw.jobs);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is))) {
            while ((jsonDataString = bufferedReader.readLine()) != null) {
                sb.append(jsonDataString).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();

    }
}
}