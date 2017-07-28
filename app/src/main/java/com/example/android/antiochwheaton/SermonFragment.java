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
import com.example.android.antiochwheaton.sync.AntiochSyncUtils;


public class SermonFragment extends Fragment implements
        PodcastAdapter.PodcastAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor>{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    String[] mProjection = new String[]{
            DataContract.PodcastEntry.COLUMN_WP_ID,
            DataContract.PodcastEntry.COLUMN_TITLE,
            DataContract.PodcastEntry.COLUMN_DATE,
            DataContract.PodcastEntry.COLUMN_AUTHOR,
            DataContract.PodcastEntry.COLUMN_IMAGE_URL,

    };

    public static final int ID = 0;
    public static final int TITLE = 1;
    public static final int DATE = 2;
    public static final int AUTHOR = 3;
    public static final int IMAGE = 4;

    private static final int LOADER = 0;

    private RecyclerView mRecyclerView;
    private PodcastAdapter mPodcastAdapter;
    private int mPosition = RecyclerView.NO_POSITION;

    Toast mToast;
    TextView mErrorTextView;
    ProgressBar mLoadingProgressBar;
    public SermonFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_sermon, container, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerview_podcasts);

        mLoadingProgressBar = (ProgressBar)rootView.findViewById(R.id.pb_loading);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mPodcastAdapter = new PodcastAdapter(this,getContext());

        mRecyclerView.setAdapter(mPodcastAdapter);

        showLoading();



        AntiochSyncUtils.initialize(getContext());

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(LOADER,null,this);
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
    public void onListItemClick(String podcastID) {
        Intent intent = new Intent(getContext(),PodcastDetail.class);
        Uri uriForItemClicked = DataContract.PodcastEntry.buildPodcastUriWithId(podcastID);
        intent.setData(uriForItemClicked);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), DataContract.PodcastEntry.CONTENT_URI,mProjection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPodcastAdapter.swapCursor(data);

        if(mPosition == RecyclerView.NO_POSITION){
            mPosition = 0;
        }

        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) showPodcastDataView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPodcastAdapter.swapCursor(null);
    }

    private void showPodcastDataView(){
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

}
