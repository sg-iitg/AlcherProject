package com.example.recyclerviewimplementation;

import java.util.ArrayList;

public class ExampleItem {
    private String mText;
    private ArrayList<String> mList;

    public ExampleItem(String Text, ArrayList<String> List)
    {
        mText=Text;
        mList=List;
    }

    public String getText()
    {
        return mText;
    }

    public ArrayList<String> getList()
    {
        return mList;
    }
}
