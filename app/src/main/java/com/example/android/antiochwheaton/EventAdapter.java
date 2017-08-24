package com.example.android.antiochwheaton;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.antiochwheaton.utilities.AntiochUtilties;
import com.squareup.picasso.Picasso;


class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventAdapterViewHolder> {
    private final Context mContext;
    private Cursor mCursor;

    private final int viewPrevious;

    final private EventAdapterOnClickHandler mClickHandler;

    interface EventAdapterOnClickHandler{
        void onListItemClick(String clickedItem);
    }

    EventAdapter(EventAdapterOnClickHandler handler, Context context){
        mClickHandler = handler;
        mContext = context;
        viewPrevious = R.layout.event_list_item;
    }

    //// DONE: 6/27/2017 use databases to get author tags, and media url
    @Override
    public void onBindViewHolder(EventAdapter.EventAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String date = mCursor.getString(EventsFragment.DATE);
        String title = mCursor.getString(EventsFragment.TITLE);
        String imageID = mCursor.getString(EventsFragment.IMAGE);


        date = AntiochUtilties.getFormattedEventDate(date);


        String imageURL = AntiochUtilties.getImageUrl(mContext,imageID);


        holder.tvTitle.setText(title);
        holder.tvDate.setText(date);


        if(imageURL.equals("")){
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
    public EventAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(mContext)
                .inflate(viewType,parent,false);

        return new EventAdapter.EventAdapterViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(mCursor == null) return 0;

        return mCursor.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        int viewRecent = R.layout.event_list_item_recent;
        if(position == 0)
            return viewRecent;

        return viewPrevious;
    }

    void swapCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
    }
    class EventAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView tvTitle;
        final TextView tvDate;
        final ImageView ivImage;

        EventAdapterViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView)itemView.findViewById(R.id.textEventTitle);
            tvDate = (TextView)itemView.findViewById(R.id.textEventDate);
            ivImage = (ImageView) itemView.findViewById(R.id.imageEvent);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String id = mCursor.getString(EventsFragment.ID);
            mClickHandler.onListItemClick(id);
        }
    }
}
