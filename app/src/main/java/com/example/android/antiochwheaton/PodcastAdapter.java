package com.example.android.antiochwheaton;

import android.content.Context;
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

    private String[] mPodcastData;

    final private PodcastAdapterOnClickHandler mClickHandler;

    public interface PodcastAdapterOnClickHandler{
        void onListItemClick(String clickedItem);
    }

    public PodcastAdapter(PodcastAdapterOnClickHandler handler){mClickHandler = handler;}

    @Override
    public void onBindViewHolder(PodcastAdapter.PodcastAdapterViewHolder holder, int position) {
        String podcastString = mPodcastData[position];
        holder.mPodcastTextView.setText(podcastString);
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
        if(mPodcastData == null){
            return 0;
        }

        return mPodcastData.length;
    }

    public void setmPodcastData (String[] podcastData){
        mPodcastData = podcastData;
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
            String itemClicked = mPodcastData[getAdapterPosition()];
            mClickHandler.onListItemClick(itemClicked);
        }
    }
}
