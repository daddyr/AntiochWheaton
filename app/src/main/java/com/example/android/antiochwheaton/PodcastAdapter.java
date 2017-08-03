package com.example.android.antiochwheaton;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.antiochwheaton.data.DataContract;
import com.example.android.antiochwheaton.utilities.AntiochUtilties;
import com.squareup.picasso.Picasso;

/**
 * Created by Ryan on 6/4/2017.
 */

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.PodcastAdapterViewHolder> {


    private final Context mContext;
    private Cursor mCursor;

    final int viewRecent = R.layout.podcast_list_item_recent;
    final int viewPrevious = R.layout.podcast_list_item;

    final private PodcastAdapterOnClickHandler mClickHandler;

    public interface PodcastAdapterOnClickHandler{
        void onListItemClick(String clickedItem);
    }

    public PodcastAdapter(PodcastAdapterOnClickHandler handler, Context context){
        mClickHandler = handler;
        mContext = context;
    }

    //// DONE: 6/27/2017 use databases to get author tags, and media url
    @Override
    public void onBindViewHolder(PodcastAdapter.PodcastAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String date = mCursor.getString(SermonFragment.DATE);
        String title = mCursor.getString(SermonFragment.TITLE);
        String imageID = mCursor.getString(SermonFragment.IMAGE);
        String authorID = mCursor.getString(SermonFragment.AUTHOR);

        String author = AntiochUtilties.getAuthor(mContext,authorID);



        String imageURL = AntiochUtilties.getImageUrl(mContext,imageID);


        holder.tvTitle.setText(title);
        holder.tvDate.setText(date);
        holder.tvAuthor.setText(author);

        if(imageURL == ""){
            holder.ivImage.setImageResource(R.mipmap.ic_launcher);
        }else {
            Picasso.with(mContext)
                    .load(imageURL)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.ivImage);
        }


    }

    @Override
    public PodcastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(mContext)
                .inflate(viewType,parent,false);

        return new PodcastAdapterViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(mCursor == null) return 0;

        return mCursor.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return viewRecent;

        return viewPrevious;
    }

    void swapCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
    }
    class PodcastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView tvTitle;
        final TextView tvDate;
        final TextView tvAuthor;
        final ImageView ivImage;

        public PodcastAdapterViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView)itemView.findViewById(R.id.textTitle);
            tvDate = (TextView)itemView.findViewById(R.id.textDate);
            tvAuthor = (TextView) itemView.findViewById(R.id.textAuthor);
            ivImage = (ImageView) itemView.findViewById(R.id.imagePodcast);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String id = mCursor.getString(SermonFragment.ID);
            mClickHandler.onListItemClick(id);
        }
    }
}
