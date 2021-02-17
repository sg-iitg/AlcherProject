package com.example.recyclerviewimplementation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomListAdapter extends ArrayAdapter {
    private Activity context;
    private ArrayList<String> mdescription;
    private String mvalue;
    private int mrow_number;

    public CustomListAdapter(Activity context, ArrayList<String> description, String value){
        super(context, R.layout.example_item);
        this.context=context;
        this.mdescription = description;
        this.mvalue = value;
    }

    @Override
    public Object getItem(int pos) {
        return mdescription.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public int getCount()
    {
        return mdescription.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View convertView=inflater.inflate(R.layout.example_item, null,true);

        TextView desc = convertView.findViewById(R.id.item);
        desc.setText(mdescription.get(position));
        return convertView;
    }



}
