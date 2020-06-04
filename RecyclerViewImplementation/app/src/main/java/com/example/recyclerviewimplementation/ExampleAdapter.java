package com.example.recyclerviewimplementation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> implements Filterable {
    private List<ExampleItem> mExampleList;
    private List<ExampleItem> mExampleListFull;
    private Activity mcontext;
    ExampleAdapter madapter;

    public class ExampleViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public ListView mListView;
        public ImageView deleteicon;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView= itemView.findViewById(R.id.heading);
            deleteicon = itemView.findViewById(R.id.delete_icon);
            mListView=itemView.findViewById(R.id.listview);
        }
    }

    public ExampleAdapter(Activity context, ArrayList<ExampleItem> ExampleList, ExampleAdapter mAdapter)
    {
        mExampleList=ExampleList;
        mExampleListFull = new ArrayList<>(mExampleList);
        mcontext=context;
        madapter = mAdapter;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, final int position) {
        final ExampleItem currentItem = mExampleList.get(position);
        holder.mTextView.setText(currentItem.getText());
        holder.mListView.setAdapter(new CustomListAdapter(mcontext, currentItem.getList(),currentItem.getText()));
        holder.deleteicon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                new BackgroundTask(mcontext, currentItem.getText()).execute();
                mExampleListFull.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }


    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ExampleItem> filteredList = new ArrayList<>();

            if(constraint==null || constraint.length()==0)
            {
                filteredList.addAll(mExampleListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(ExampleItem item:mExampleListFull)
                {
                    if(item.getText().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(item);
                        continue;
                    }

                    for(String e : item.getList()){
                        if(e.toLowerCase().contains(filterPattern)){
                            filteredList.add(item);
                            break;
                        }
                    }

                }
            }
            FilterResults results= new FilterResults();
            results.values= filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mExampleList.clear();
            mExampleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public static class BackgroundTask extends AsyncTask<String, Void, String> {
        Context ctx;
        String json_url;
        ProgressDialog dialog;
        String value;

        public BackgroundTask(Activity ctx, String value){
            this.ctx = ctx;
            dialog = new ProgressDialog(ctx);
            this.value = value;
        }

        @Override
        protected void onPreExecute() {
            json_url= "http://192.168.43.243:80/PassEntries/delete.php";
            dialog.setMessage("Deleting...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection httpURLConnection = null;
            try {
                url = new URL(json_url);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("collegename", "UTF-8")+"="+URLEncoder.encode(value,"UTF-8");
                Log.v("BackgroundTask", data);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String JSON_STRING;
                while((JSON_STRING= bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(JSON_STRING);
                }
                bufferedReader.close();
                inputStream.close();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
        }
    }
}
