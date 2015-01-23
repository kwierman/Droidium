package com.tritium.droidium;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.util.Log;


import com.tritium.droidium.itemizedlist.ItemizedAdapter;
import com.tritium.droidium.runcontrol.RunManager;
import com.tritium.droidium.sources.NewSourceController;
import com.tritium.droidium.sources.Source;
import com.tritium.droidium.sources.SourceController;

public class SourcePickerFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String TAG="SOURCEPICKERFRAGMENT";

    private MainActivity mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ItemizedAdapter mAdapter;


    public static SourcePickerFragment newInstance() {
        SourcePickerFragment fragment = new SourcePickerFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SourcePickerFragment() {
        //getActivity().getApplication();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAdapter = new ItemizedAdapter(getActivity() );
        mAdapter.addHeader(R.string.local_sources);
        //mAdapter.addHeader(R.string.remote_sources);
        RunManager rc = (RunManager)getActivity().getApplication();
        if(rc==null){
            Log.d(TAG, "Could not resolve ron control manager");
            return;
        }
        for(Source src: rc.getAvailableSources()){
            mAdapter.addItem(new SourceController(src, src.getName(), src.getDesc(), src.getImage() ), R.string.local_sources);
        }
        //TODO: Add in new local sources for file streaming
        //mAdapter.addItem( new NewSourceController(),R.string.local_sources );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_source, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (MainActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Main Activity");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            Log.v(TAG,"Item Clicked: "+position+" "+id);
            ItemizedAdapter.HostType host = mAdapter.getHostAt(position);

            //check for add new local source:
            if(host instanceof NewSourceController){
                Log.d(TAG, "Source Fragment: Adding new local source");
                mListener.showFragment(NewLocalSourceFragment.newInstance());
                return;
            }
            RunManager rc = (RunManager)getActivity().getApplication();
            if( mAdapter.select(position) ){
                rc.getCurrentRun().addSource(((SourceController) host).getSource());
            }
            else{
                rc.getCurrentRun().removeSource(((SourceController) host).getSource());
            }
        }

        mAdapter.notifyDataSetChanged();
    }


}
