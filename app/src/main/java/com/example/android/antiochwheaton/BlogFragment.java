package com.example.android.antiochwheaton;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.antiochwheaton.data.DataContract;

/**
 * Created by ryan_ on 7/25/2017.
 */

public class BlogFragment extends Fragment
        implements BlogAdapter.BlogAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor>{
    String[] mProjection = new String[]{
            DataContract.BlogEntry.COLUMN_WP_ID,
            DataContract.BlogEntry.COLUMN_TITLE,
            DataContract.BlogEntry.COLUMN_DATE,
            DataContract.BlogEntry.COLUMN_AUTHOR,
            DataContract.BlogEntry.COLUMN_IMAGE_URL,

    };

    public static final int ID = 0;
    public static final int TITLE = 1;
    public static final int DATE = 2;
    public static final int AUTHOR = 3;
    public static final int IMAGE = 4;

    private static final int LOADER = 1;

    private RecyclerView mRecyclerView;
    private BlogAdapter mblogAdapter;
    private int mPosition = RecyclerView.NO_POSITION;

    Toast mToast;
    TextView mErrorTextView;
    ProgressBar mLoadingProgressBar;
    public BlogFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blog, container, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerview_blogs);

        mLoadingProgressBar = (ProgressBar)rootView.findViewById(R.id.pb_loading_blog);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mblogAdapter = new BlogAdapter(this,getContext());

        mRecyclerView.setAdapter(mblogAdapter);

        showLoading();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getSupportLoaderManager().initLoader(LOADER,null,this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void showLoading(){
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onListItemClick(String blogID) {
        Intent intent = new Intent(getContext(),BlogDetail.class);
        Uri itemClickedUri = DataContract.BlogEntry.buildUriWithId(blogID);
        intent.setData(itemClickedUri);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), DataContract.BlogEntry.CONTENT_URI,mProjection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mblogAdapter.swapCursor(data);

        if(mPosition == RecyclerView.NO_POSITION){
            mPosition = 0;
        }

        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) showblogDataView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mblogAdapter.swapCursor(null);
    }

    private void showblogDataView(){
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
