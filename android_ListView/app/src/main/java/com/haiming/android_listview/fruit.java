package com.haiming.android_listview;

/**
 * Created by haiming on 2015/8/29.
 */
public class fruit {
    private String name;
    private int sourceId;
    public fruit(String name,int sourceId){
        this.name = name;
        this.sourceId = sourceId;
    }

    public String getName() {
        return name;
    }

    public int getSourceId() {
        return sourceId;
    }
}
