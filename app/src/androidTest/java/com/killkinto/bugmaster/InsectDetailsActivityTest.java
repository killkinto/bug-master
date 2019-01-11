package com.killkinto.bugmaster;

import android.content.Intent;
import android.database.Cursor;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.killkinto.bugmaster.data.BugsDbHelper;
import com.killkinto.bugmaster.data.DatabaseManager;
import com.killkinto.bugmaster.data.Insect;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.*;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class InsectDetailsActivityTest {

    @Rule
    public ActivityTestRule<InsectDetailsActivity> mMainActivityTestRule =
            new ActivityTestRule<>(InsectDetailsActivity.class, false, false);

    @Test
    public void displayProperly() {
        Intent intent = new Intent();
        Cursor cursor = DatabaseManager.getInstance(getTargetContext()).queryAllInsects(null);
        cursor.moveToFirst();
        Insect insect = new Insect(cursor);
        String classification = getTargetContext().getString(R.string.classification, insect.classification);
        intent.putExtra(InsectDetailsActivity.ID_INSECT_DETAILS_KEY, DatabaseManager.getColumnInt(cursor, BugsDbHelper.InsectsColumns._ID));
        mMainActivityTestRule.launchActivity(intent);
        onView(withId(R.id.txt_friendly_name)).check(matches(withText(insect.name)));
        onView(withId(R.id.txt_scientific_name)).check(matches(withText(insect.scientificName)));
        onView(withId(R.id.txt_classification)).check(matches(withText(classification)));
    }
}
