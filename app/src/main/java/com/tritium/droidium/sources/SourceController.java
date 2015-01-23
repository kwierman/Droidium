package com.tritium.droidium.sources;

import com.tritium.droidium.itemizedlist.ItemizedAdapter;

/**
 * Created by kwierman on 1/16/15.
 */
public class SourceController implements ItemizedAdapter.HostType {

    private Source mSource;
    private String mName;
    private String mDesc;
    private int mImage;

    public SourceController(Source src, String name, String desc, int imageID){
        this.mSource=src;
        this.mName = name;
        this.mDesc = desc;
        this.mImage = imageID;
    }

    public Source getSource(){
        return mSource;
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
