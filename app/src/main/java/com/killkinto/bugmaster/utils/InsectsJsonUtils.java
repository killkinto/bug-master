package com.killkinto.bugmaster.utils;

import android.content.ContentValues;

import com.killkinto.bugmaster.data.BugsDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility functions to handle insects data to JSON data.
 */
public final class InsectsJsonUtils {

    private static final String INSECTS_LIST = "insects";
    private static final String FRIENDLY_NAME = "friendlyName";
    private static final String SCIENTIFIC_NAME = "scientificName";
    private static final String CLASSIFICATION = "classification";
    private static final String IMAGE_ASSET = "imageAsset";
    private static final String DANGER_LEVEL = "dangerLevel";

    public static ContentValues[] getInsectsContentValuesFromJson(String insectsJsonStr)
            throws JSONException {
        JSONObject insectsJson = new JSONObject(insectsJsonStr);

        JSONArray jsonInsectsArray = insectsJson.getJSONArray(INSECTS_LIST);

        ContentValues[] insectsContentValues = new ContentValues[jsonInsectsArray.length()];

        for (int i = 0; i < jsonInsectsArray.length(); i++) {
            JSONObject insect = jsonInsectsArray.getJSONObject(i);

            ContentValues cv = new ContentValues();
            cv.put(BugsDbHelper.InsectsColumns.FRIENDLY_NAME, insect.getString(FRIENDLY_NAME));
            cv.put(BugsDbHelper.InsectsColumns.SCIENTIFIC_NAME, insect.getString(SCIENTIFIC_NAME));
            cv.put(BugsDbHelper.InsectsColumns.CLASSIFICATION, insect.getString(CLASSIFICATION));
            cv.put(BugsDbHelper.InsectsColumns.IMAGE_ASSET, insect.getString(IMAGE_ASSET));
            cv.put(BugsDbHelper.InsectsColumns.DANGER_LEVEL, insect.getInt(DANGER_LEVEL));

            insectsContentValues[i] = cv;
        }

        return insectsContentValues;
    }
}
