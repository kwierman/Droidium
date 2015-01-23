package com.tritium.droidium.outputs;

import com.tritium.droidium.itemizedlist.ItemizedAdapter;

/**
 * Created by kwierman on 1/23/15.
 */
public class OutputController implements ItemizedAdapter.HostType {

    private Output mOutput;
    private String mName;
    private String mDesc;
    private int mImage;

    public OutputController(Output out, String name, String desc, int imageID){
        this.mOutput=out;
        this.mName = name;
        this.mDesc = desc;
        this.mImage = imageID;
    }

    public Output getOutput(){
        return mOutput;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getDesc() {
        return mDesc;
    }

    @Override
    public int getImage() {
        return mImage;
    }

}
