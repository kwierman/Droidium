package com.tritium.droidium;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tritium.droidium.runcontrol.RunManager;


public class RunControl extends Fragment {


    private MainActivity mListener;

    public static RunControl newInstance() {
        RunControl fragment = new RunControl();
        return fragment;
    }

    public RunControl() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.fragment_run_control, container, false);
        Button startButton = (Button) vi.findViewById(R.id.button_start_run);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ( (RunManager)mListener.getApplication() ).startRun();
            }
        });

        Button stopButton = (Button) vi.findViewById(R.id.button_stop_run);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ( (RunManager)mListener.getApplication() ).startRun();
            }
        });

        /*
        CheckBox offLineRunBox = (CheckBox) vi.findViewById(R.id.offline_run_checkbox);
        offLineRunBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.getRunControlManager().setOfflineRun(((CheckBox)view).isChecked());
            }
        });
        */
        //TODO:: Setup the other options


        return vi;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (MainActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
