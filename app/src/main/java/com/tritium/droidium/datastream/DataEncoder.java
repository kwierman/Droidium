package com.tritium.droidium.datastream;

import android.os.Bundle;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.zip.DataFormatException;


/**
 * Created by kwierman on 9/26/14.
 *
 * As binary this is supposed to represent the following format
 * **** **** **** **** **** **** **** **** ID of data record
 * **** **** **** **** **** **** **** **** Length of record following this point
 * **** **** **** **** **** **** **** **** Repeating data fields
 *
 */
public class DataEncoder {

    private String name;

    enum FieldType{
        Int,Long,Float
    }

    private class RecordDescription{
        FieldType type;
        String name;
        RecordDescription(FieldType t, String n){
            name=n;
            type=t;
        }
        public void describe(Node node, Document builder) throws DOMException{
            Element desc = builder.createElement("field");
            desc.setAttribute("name", this.name);
            switch(this.type){
                case Int:
                    desc.setAttribute("type", "Int");
                    break;
                case Long:
                    desc.setAttribute("type","Long");
                    break;
                case Float:
                    desc.setAttribute("type", "Float");
                    break;
                default:
                    desc.setAttribute("type", "Unknown");
            }
            node.appendChild(desc);
        }
    }

    private int id;//id for the responsible component
    private byte[] fields;//holds the data (the real record)
    ArrayList<RecordDescription> description;

    public DataEncoder(int id, String name){
        this.name = name;
        this.id = id;
        this.fields = new byte[0];
        this.description = new ArrayList<>();
    }

    public DataEncoder(DataEncoder other){
        this.id = other.id;
        this.fields = other.fields;
        this.description = new ArrayList<>();
        for(RecordDescription r : other.description){
            this.description.add(new RecordDescription(r.type, r.name));
        }
    }

    //reconstruct the encoder from xml
    public DataEncoder(Element node){
        this.name = node.getAttribute("name");
        this.id = Integer.getInteger( node.getAttribute("id") );
        NodeList list = node.getElementsByTagName("field");

        for(int i=0; i<list.getLength();i++){
            Element field = (Element)list.item(i);
            String type_string = field.getAttribute("type");
            String name_string = field.getAttribute("name");
            if(type_string.equalsIgnoreCase("Int")){
                this.add((int)0, name_string);
            }
            else if(type_string.equalsIgnoreCase("Long")){
                this.add((long)0,name_string );
            }
            else if(type_string.equalsIgnoreCase("Float")){
                this.add((float)0.0, name_string);
            }
        }
    }

    public void describe(Element n, Document doc) throws DOMException{
        Element thisNode = doc.createElement("data_encoder");
        thisNode.setAttribute("name", this.name);
        thisNode.setAttribute("id", Integer.toString(this.id) );
        for(RecordDescription r : description) {
            r.describe(thisNode, doc);
        }
        n.appendChild(thisNode);
    }

    public int getID(){return this.id;}
    public int getNFields(){
        return this.description.size();
    }

    public byte[] asBinary(){
        byte[] idBuffer = new byte[8];
        ByteBuffer.wrap(idBuffer).putInt(id).putInt(fields.length);        ;
        byte[] output = new byte[8+fields.length];
        System.arraycopy(idBuffer,0,output,0,idBuffer.length);
        System.arraycopy(fields,0,output,idBuffer.length,fields.length);
        return output;
    }

    public Bundle toBundle(){
        Bundle ofFun = new Bundle();
        ofFun.putByteArray("DataRecord",this.asBinary());
        return ofFun;
    }

    public void append(byte[] buffer){
        byte[] temp1 = fields;
        fields = new byte[fields.length+buffer.length];
        System.arraycopy(temp1,0,fields,0,temp1.length);
        System.arraycopy(buffer,0,fields,temp1.length,buffer.length);
    }
    public void add(int x, String name){
        byte[] buffer = new byte[4];
        ByteBuffer.wrap(buffer).putInt(x);
        this.append(buffer);
        description.add( new RecordDescription(FieldType.Int, name));
    }
    public void add(long x, String name){
        byte[] buffer = new byte[8];
        ByteBuffer.wrap(buffer).putLong(x);
        this.append(buffer);
        description.add(  new RecordDescription(FieldType.Long, name) );
    }
    public void add(float x, String name){
        byte[] buffer = new byte[4];
        ByteBuffer.wrap(buffer).putFloat(x);
        this.append(buffer);
        description.add(  new RecordDescription(FieldType.Int, name) );
    }

    public void modify(int index, int newInt) throws DataFormatException {
        if(this.description.get(index).type!= FieldType.Int)
            throw new DataFormatException("Access to index of incorrect type");
        //else fast forward to the site and
        byte[] buffer = new byte[4];
        ByteBuffer.wrap(buffer).putInt(newInt);
        int position=0;
        for(int i=0; i<index;i++){
            FieldType r = description.get(i).type;
            switch(r){
                case Int:
                    position+=4;
                    break;
                case Long:
                    position+=8;
                    break;
                case Float:
                    position+=4;
                    break;
                default:
                    break;
            }
        }
        System.arraycopy(buffer,0,fields,position,buffer.length);
    }
    public void modify(int index, long newlong) throws DataFormatException {
        if(this.description.get(index).type!= FieldType.Long)
            throw new DataFormatException("Access to index of incorrect type");
        //else fast forward to the site and
        byte[] buffer = new byte[8];
        ByteBuffer.wrap(buffer).putLong(newlong);
        int position=0;
        for(int i=0; i<index;i++){
            FieldType r = description.get(i).type;
            switch(r){
                case Int:
                    position+=4;
                    break;
                case Long:
                    position+=8;
                    break;
                case Float:
                    position+=4;
                    break;
                default:
                    break;
            }
        }
        System.arraycopy(buffer,0,fields,position,buffer.length);
    }
    public void modify(int index, float newFloat) throws DataFormatException {
        if(this.description.get(index).type!= FieldType.Float)
            throw new DataFormatException("Access to index of incorrect type");
        //else fast forward to the site and
        byte[] buffer = new byte[4];
        ByteBuffer.wrap(buffer).putFloat(newFloat);
        int position=0;
        for(int i=0; i<index;i++){
            FieldType r = description.get(i).type;
            switch(r){
                case Int:
                    position+=4;
                    break;
                case Long:
                    position+=8;
                    break;
                case Float:
                    position+=4;
                    break;
                default:
                    break;
            }
        }
        System.arraycopy(buffer,0,fields,position,buffer.length);
    }

    void  fromBinary(byte[] input) throws DataFormatException {
        ByteBuffer buffer = ByteBuffer.wrap(input);

        //DataEncoder output = new DataEncoder(buffer.getInt(0));
        int length = buffer.getInt(1);
        if(length!= description.size() )
            throw new DataFormatException("Input Binary Doesn't match descriptor");
        int position= 8;
        for(int i =0; i<length;i++){
            /*
            switch(r.get(i)){
                case Int:
                    output.add(buffer.getInt(position));
                    position+=4;
                    break;
                case Long:
                    output.add(buffer.getLong(position));
                    position+=8;
                    break;
                case Float:
                    output.add(buffer.getFloat(position));
                    position+=4;
                    break;
            }
            */
        }
        //return output;
    }

}
