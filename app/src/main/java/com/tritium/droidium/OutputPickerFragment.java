package com.tritium.droidium;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;


import com.tritium.droidium.itemizedlist.ItemizedAdapter;
import com.tritium.droidium.outputs.NewOutputController;
import com.tritium.droidium.outputs.Output;
import com.tritium.droidium.outputs.OutputController;
import com.tritium.droidium.runcontrol.RunManager;


public class OutputPickerFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String TAG="OUTPUTPICKERFRAGMENT";
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

    public static OutputPickerFragment newInstance() {
        OutputPickerFragment fragment = new OutputPickerFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OutputPickerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ItemizedAdapter(getActivity() );
        mAdapter.addHeader(R.string.outputs);
        //mAdapter.addHeader(R.string.remote_sources);
        RunManager rc = (RunManager)getActivity().getApplication();
        if(rc==null){
            Log.d(TAG, "Could not resolve ron control manager");
            return;
        }
        for(Output out: rc.getAvailableOutputs()){
            mAdapter.addItem(new OutputController(out, out.getName(), out.getDesc(), out.getImage() ), R.string.outputs);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_output, container, false);

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
                    + " must implement MainActivity");
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
            if(host instanceof NewOutputController){
                Log.d(TAG, "Output Fragment: Adding new local source");
                //mListener.showFragment(NewLocalSourceFragment.newInstance());
                return;
            }
            RunManager rc = (RunManager)getActivity().getApplication();
            if( mAdapter.select(position) ){
                rc.getCurrentRun().addOutput(((OutputController) host).getOutput());
            }
            else{
                rc.getCurrentRun().removeOutput(((OutputController) host).getOutput());
            }
        }

        mAdapter.notifyDataSetChanged();
    }


}
