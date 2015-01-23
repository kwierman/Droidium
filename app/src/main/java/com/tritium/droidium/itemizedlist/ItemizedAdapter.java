package com.tritium.droidium.itemizedlist;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tritium.droidium.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwierman on 9/23/14.
 */
public class ItemizedAdapter extends ArrayAdapter<ItemizedAdapter.ListItem >{

    static final String TAG="ITEMIZEDADAPTER";

    private LayoutInflater mInflater;

    public interface HostType{
        public String getName();
        public String getDesc();
        public int getImage();
    }


    interface ListItem{
        public enum ItemType{
            header,item;
        }

        public int getViewType();
        public View getView(LayoutInflater inflater, View convertView);
        public String getName();
        public boolean isSelected();
        public void flipSelect();
    }

    private class Header implements ListItem{
        public final String name;

        public Header(String n){
            name=n;
        }

        @Override
        public int getViewType() {
            return ItemType.header.ordinal();
        }

        @Override
        public View getView(LayoutInflater inflater, View convertView) {
            View view;
            if(convertView==null)
                view = (View)inflater.inflate(com.tritium.droidium.R.layout.itemized_header,null);
            else
                view = convertView;
            TextView txt = (TextView) view.findViewById(R.id.item_header);
            txt.setText(name);
            return view;
        }

        @Override
        public String getName(){return name;}

        @Override
        public boolean isSelected(){return false;}
        @Override
        public void flipSelect(){}
    }

    public class Item implements ListItem{
        public final String name;
        public final String description;
        public final int imageId;
        private final HostType host;
        private boolean selected;

        public Item(String n, String desc,int i, HostType h ){
            name = n;
            description=desc;
            host=h;
            imageId = h.getImage();
            selected = false;
        }

        @Override
        public String getName(){return name;}

        public HostType getHost(){return host;}


        @Override
        public int getViewType() {
            return ItemType.item.ordinal();
        }

        @Override
        public View getView(LayoutInflater inflater, View convertView) {
            View view;
            if(convertView==null)
                view = (View)inflater.inflate(com.tritium.droidium.R.layout.itemized_item,null);
            else
                view = convertView;
            TextView txt = (TextView) view.findViewById(R.id.item_name);
            txt.setText(name);
            txt = (TextView)view.findViewById(R.id.item_description);
            txt.setText(description);
            ImageView img = (ImageView) view.findViewById(R.id.item_image);
            img.setImageResource(imageId);
            if(selected)
                view.setBackgroundColor( view.getResources().getColor(R.color.green_light)   );
            else
                view.setBackgroundColor(R.style.AppTheme);
            return view;
        }
        @Override
        public boolean isSelected(){return this.selected;}
        @Override
        public void flipSelect(){
            Log.d(TAG, "In item interior Flipping selected: "+Boolean.toString(this.selected) );
            if(this.selected)
                this.selected=false;
            else
                this.selected=true;
            Log.d(TAG, "In item interior Flipping selected: "+Boolean.toString(this.selected) );
        }
    }


    public ItemizedAdapter(Context context){
        super(context,0, new ArrayList<ListItem>() );
        mInflater = LayoutInflater.from(context);
    }

    public void addHeader(int id){
        Header tmp = new Header(getContext().getString(id) );
        this.add(tmp);
    }

    public Item addItem(HostType host, int headerID){
        Item tmp = new Item(host.getName(), host.getDesc(), host.getImage(),host );
        //find where in the list

        for(int i=0; i< this.getCount(); i++ )
            if( this.getItem(i).getName() == getContext().getString(headerID) ) {
                this.insert(tmp, i+1);
                return tmp;
            }

        this.add(tmp);
        return tmp;
    }


    @Override
    public int getViewTypeCount(){
        return 2;
    }

    @Override
    public int getItemViewType(int position){
        return getItem(position).getViewType();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        return getItem(position).getView(mInflater, convertView);
    }

    public HostType getHostAt(int position){
        if( this.getItem(position).getViewType() ==ListItem.ItemType.item.ordinal() ) {
            Item item = (Item) (this.getItem(position));
            return item.getHost();
        }
        return null;
    }

    public boolean select(int position){
        ListItem it = getItem(position);
        Log.d(TAG,"selecting item at: "+Integer.toString(position));
        Log.d(TAG, "Item Selected: "+Boolean.toString(it.isSelected()));
        it.flipSelect();
        Log.d(TAG, "Item Selected: "+Boolean.toString(it.isSelected()));
        return it.isSelected();
    }

}

