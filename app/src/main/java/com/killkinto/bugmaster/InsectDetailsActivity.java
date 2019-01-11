package com.killkinto.bugmaster;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.killkinto.bugmaster.data.DatabaseManager;
import com.killkinto.bugmaster.data.Insect;

import java.io.IOException;
import java.io.InputStream;

public class InsectDetailsActivity extends AppCompatActivity {

    public static final String ID_INSECT_DETAILS_KEY = "InsectDetailsActivity.ID_INSECT_DETAILS_KEY";

    private ImageView mInsectView;
    private TextView mFriendlyNameView;
    private TextView mScientificNameView;
    private TextView mClassificationView;
    private RatingBar mDangerLevelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_insect_details);

        mInsectView = (ImageView) findViewById(R.id.img_insect);
        mFriendlyNameView = (TextView) findViewById(R.id.txt_friendly_name);
        mScientificNameView = (TextView) findViewById(R.id.txt_scientific_name);
        mClassificationView = (TextView) findViewById(R.id.txt_classification);
        mDangerLevelView = (RatingBar) findViewById(R.id.bar_danger_level);

        int id = getIntent().getIntExtra(ID_INSECT_DETAILS_KEY, -1);

        if (id > 0) {
            Cursor cursor = DatabaseManager.getInstance(this).queryInsectsById(id);
            cursor.moveToFirst();
            Insect insect = new Insect(cursor);
            Drawable image = getDrawableFromAssets(insect.imageAsset);
            String classification = getString(R.string.classification, insect.classification);

            mInsectView.setImageDrawable(image);
            mFriendlyNameView.setText(insect.name);
            mScientificNameView.setText(insect.scientificName);
            mClassificationView.setText(classification);
            mDangerLevelView.setRating(insect.dangerLevel);
        }
    }

    private Drawable getDrawableFromAssets(String fileName) {
        Drawable drawable = null;
        try {
            InputStream ims = getAssets().open(fileName);
            drawable = Drawable.createFromStream(ims, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drawable;
    }
}
