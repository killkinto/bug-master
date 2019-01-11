package com.killkinto.bugmaster.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.killkinto.bugmaster.R;
import com.killkinto.bugmaster.utils.InsectsJsonUtils;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Database helper class to facilitate creating and updating
 * the database from the chosen schema.
 */
public class BugsDbHelper extends SQLiteOpenHelper {
    private static final String TAG = BugsDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "insects.db";
    private static final int DATABASE_VERSION = 1;

    //Used to read data from res/ and assets/
    private Resources mResources;

    public static final String TABLE_INSECTS = "insects";

    public static final class InsectsColumns implements BaseColumns {
        public static final String FRIENDLY_NAME = "friendly_name";
        public static final String SCIENTIFIC_NAME = "scientific_name";
        public static final String CLASSIFICATION = "classification";
        public static final String IMAGE_ASSET = "image_asset";
        public static final String DANGER_LEVEL = "danger_level";
    }

    private static final String SQL_CREATE_TABLE_INSECTS = String.format("CREATE TABLE %s"
            +" (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER)",
            TABLE_INSECTS,
            InsectsColumns._ID,
            InsectsColumns.FRIENDLY_NAME,
            InsectsColumns.SCIENTIFIC_NAME,
            InsectsColumns.CLASSIFICATION,
            InsectsColumns.IMAGE_ASSET,
            InsectsColumns.DANGER_LEVEL);

    public BugsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mResources = context.getResources();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_INSECTS);

        try {
            readInsectsFromResources(db);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSECTS);
        onCreate(db);
    }

    /**
     * Streams the JSON data from insect.json, parses it, and inserts it into the
     * provided {@link SQLiteDatabase}.
     *
     * @param db Database where objects should be inserted.
     * @throws IOException
     * @throws JSONException
     */
    private void readInsectsFromResources(SQLiteDatabase db) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.insects);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        //Parse resource into key/values
        final String rawJson = builder.toString();

        ContentValues[] contentValues = InsectsJsonUtils.getInsectsContentValuesFromJson(rawJson);

        for (ContentValues cv : contentValues) {
            db.insert(TABLE_INSECTS, null, cv);
        }
    }
}
