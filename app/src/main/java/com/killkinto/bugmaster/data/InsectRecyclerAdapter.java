package com.killkinto.bugmaster.data;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.killkinto.bugmaster.R;
import com.killkinto.bugmaster.views.DangerLevelView;

/**
 * RecyclerView adapter extended with project-specific required methods.
 */

public class InsectRecyclerAdapter extends
        RecyclerView.Adapter<InsectRecyclerAdapter.InsectHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /* ViewHolder for each insect item */
    public class InsectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView friendlyNameView;
        TextView scientificNameView;
        DangerLevelView dangerLevelView;

        public InsectHolder(View itemView) {
            super(itemView);

            friendlyNameView = itemView.findViewById(R.id.txt_friendly_name);
            scientificNameView = itemView.findViewById(R.id.txt_scientific_name);
            dangerLevelView = itemView.findViewById(R.id.img_danger_level);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    private Cursor mCursor;
    private OnItemClickListener mOnItemClickListener;

    public InsectRecyclerAdapter(Cursor cursor, OnItemClickListener listener) {
        mCursor = cursor;
        mOnItemClickListener = listener;
    }

    @Override
    public InsectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_insect, parent, false);
        return new InsectHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InsectHolder holder, int position) {
        Insect insect = getItem(position);
        holder.friendlyNameView.setText(insect.name);
        holder.scientificNameView.setText(insect.scientificName);
        holder.dangerLevelView.setDangerLevel(insect.dangerLevel);
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    /**
     * Return the {@link Insect} represented by this item in the adapter.
     *
     * @param position Adapter item position.
     *
     * @return A new {@link Insect} filled with this position's attributes
     *
     * @throws IllegalArgumentException if position is out of the adapter's bounds.
     */
    public Insect getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            throw new IllegalArgumentException("Item position is out of adapter's range");
        } else if (mCursor.moveToPosition(position)) {
            return new Insect(mCursor);
        }
        return null;
    }

    public void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }
}
