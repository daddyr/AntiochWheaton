package com.example.android.antiochwheaton;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ryan on 6/4/2017.
 */

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.PodcastAdapterViewHolder> {


    private final Context mContext;
    private Cursor mCursor;

    final private PodcastAdapterOnClickHandler mClickHandler;

    public interface PodcastAdapterOnClickHandler{
        void onListItemClick(String clickedItem);
    }

    public PodcastAdapter(PodcastAdapterOnClickHandler handler, Context context){
        mClickHandler = handler;
        mContext = context;
    }

    @Override
    public void onBindViewHolder(PodcastAdapter.PodcastAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String id = mCursor.getString(1);
        String title = mCursor.getString(2);
        String imageURL = mCursor.getString(5);

        holder.mPodcastTextView.setText(title);
    }

    @Override
    public PodcastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.podcast_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachImmediately = false;

        View view = inflater.inflate(layoutIdForListItem,parent,attachImmediately);

        return new PodcastAdapterViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(mCursor == null) return 0;

        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
    }
    class PodcastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mPodcastTextView;

        public PodcastAdapterViewHolder(View itemView) {
            super(itemView);
            mPodcastTextView = (TextView)itemView.findViewById(R.id.tv_podcast_data);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String title = mPodcastTextView.getText().toString();
            mClickHandler.onListItemClick(title);
        }
    }
}
