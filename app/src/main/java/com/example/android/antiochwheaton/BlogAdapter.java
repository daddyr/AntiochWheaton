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



class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogAdapterViewHolder> {
    private final Context mContext;
    private Cursor mCursor;

    private final int viewPrevious;

    final private BlogAdapterOnClickHandler mClickHandler;

    interface BlogAdapterOnClickHandler{
        void onListItemClick(String clickedItem);
    }

    BlogAdapter(BlogAdapterOnClickHandler handler, Context context){
        mClickHandler = handler;
        mContext = context;
        viewPrevious = R.layout.blog_list_item;
    }

    //// DONE: 6/27/2017 use databases to get author tags, and media url
    @Override
    public void onBindViewHolder(BlogAdapter.BlogAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String date = mCursor.getString(BlogFragment.DATE);
        String title = mCursor.getString(BlogFragment.TITLE);
        String imageID = mCursor.getString(BlogFragment.IMAGE);
        String authorID = mCursor.getString(BlogFragment.AUTHOR);

        String author = AntiochUtilties.getAuthor(mContext,authorID);



        String imageURL = AntiochUtilties.getImageUrl(mContext,imageID);


        holder.tvTitle.setText(title);
        holder.tvDate.setText(date);
        holder.tvAuthor.setText(author);

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
    public BlogAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(mContext)
                .inflate(viewType,parent,false);

        return new BlogAdapter.BlogAdapterViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(mCursor == null) return 0;

        return mCursor.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        int viewRecent = R.layout.blog_list_item_recent;
        if(position == 0)
            return viewRecent;

        return viewPrevious;
    }

    void swapCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
    }
    class BlogAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView tvTitle;
        final TextView tvDate;
        final TextView tvAuthor;
        final ImageView ivImage;

        BlogAdapterViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView)itemView.findViewById(R.id.textBlogTitle);
            tvDate = (TextView)itemView.findViewById(R.id.textBlogDate);
            tvAuthor = (TextView) itemView.findViewById(R.id.textBlogAuthor);
            ivImage = (ImageView) itemView.findViewById(R.id.imageBlog);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String id = mCursor.getString(BlogFragment.ID);
            mClickHandler.onListItemClick(id);
        }
    }
}
