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


import com.example.android.antiochwheaton.data.DataContract;


public class EventsFragment extends Fragment implements EventAdapter.EventAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

    String[] mProjection = new String[]{
            DataContract.EventsEntry.COLUMN_WP_ID,
            DataContract.EventsEntry.COLUMN_TITLE,
            DataContract.EventsEntry.COLUMN_START_DATE,
            DataContract.EventsEntry.COLUMN_IMAGE,
    };

    public static final int ID = 0;
    public static final int TITLE = 1;
    public static final int DATE = 2;
    public static final int IMAGE = 3;

    private static final int LOADER = 2;

    private RecyclerView mRecyclerView;
    private EventAdapter mEventAdapter;
    private int mPosition = RecyclerView.NO_POSITION;

    ProgressBar mLoadingProgressBar;
    TextView mEmptyText;
    
    public EventsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events,container,false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerview_events);

        mLoadingProgressBar = (ProgressBar)rootView.findViewById(R.id.pb_loading_events);
        mEmptyText = (TextView) rootView.findViewById(R.id.no_events);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mEventAdapter = new EventAdapter(this,getContext());

        mRecyclerView.setAdapter(mEventAdapter);

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
        Intent intent = new Intent(getContext(),EventDetail.class);
        Uri itemClickedUri = DataContract.EventsEntry.buildUriWithId(blogID);
        intent.setData(itemClickedUri);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), DataContract.EventsEntry.CONTENT_URI,mProjection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mEventAdapter.swapCursor(data);

        if(mPosition == RecyclerView.NO_POSITION){
            mPosition = 0;
        }

        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0)
            showEventDataView();
        else{
            mLoadingProgressBar.setVisibility(View.INVISIBLE);
            mEmptyText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mEventAdapter.swapCursor(null);
    }

    private void showEventDataView(){
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
