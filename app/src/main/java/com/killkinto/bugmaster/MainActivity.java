package com.killkinto.bugmaster;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.killkinto.bugmaster.data.DatabaseManager;
import com.killkinto.bugmaster.data.Insect;
import com.killkinto.bugmaster.data.InsectRecyclerAdapter;
import com.killkinto.bugmaster.data.BugsDbHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        InsectRecyclerAdapter.OnItemClickListener {

    private static final String INSECTS_LIST_KEY = "MainActivity.INSECTS_LIST_KEY";
    private static final String SORT_ORDER_INSECTS_LIST_KEY = "MainActivity.SORT_ORDER_INSECTS_LIST_KEY";

    private RecyclerView mRecyclerView;
    private InsectRecyclerAdapter mAdapter;
    private Cursor mCursor;
    private String mSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (savedInstanceState != null) {
            mCursor = DatabaseManager.mAllInsects;
            mSortOrder = savedInstanceState.getString(SORT_ORDER_INSECTS_LIST_KEY);
        } else {
            mSortOrder = BugsDbHelper.InsectsColumns.FRIENDLY_NAME + " ASC";
            mCursor = DatabaseManager.getInstance(this).queryAllInsects(mSortOrder);
        }

        mAdapter = new InsectRecyclerAdapter(mCursor, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORT_ORDER_INSECTS_LIST_KEY, mSortOrder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                if (mSortOrder.startsWith(BugsDbHelper.InsectsColumns.FRIENDLY_NAME)) {
                    mSortOrder = BugsDbHelper.InsectsColumns.DANGER_LEVEL + " DESC";
                } else {
                    mSortOrder = BugsDbHelper.InsectsColumns.FRIENDLY_NAME + " ASC";
                }
                mCursor = DatabaseManager.getInstance(this).queryAllInsects(mSortOrder);
                mAdapter.swapCursor(mCursor);
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Click events in Floating Action Button */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            List<Insect> insects = new ArrayList<>(mCursor.getCount());
            mCursor.moveToFirst();
            while (mCursor.moveToNext()) {
                insects.add(new Insect(mCursor));
            }
            mCursor.close();
            Collections.shuffle(insects);
            insects = insects.subList(0, QuizActivity.ANSWER_COUNT);
            ArrayList<Insect> options = new ArrayList<>(insects);
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putParcelableArrayListExtra(QuizActivity.EXTRA_INSECTS, options);
            intent.putExtra(QuizActivity.EXTRA_ANSWER, options.get(new Random().nextInt(QuizActivity.ANSWER_COUNT)));
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, InsectDetailsActivity.class);
        mCursor.moveToPosition(position);
        int id = DatabaseManager.getColumnInt(mCursor, BugsDbHelper.InsectsColumns._ID);
        intent.putExtra(InsectDetailsActivity.ID_INSECT_DETAILS_KEY, id);
        startActivity(intent);
    }
}
