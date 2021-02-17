package com.example.recyclerviewimplementation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundTask extends AsyncTask<String, Void, String> {
    Context ctx;
    String json_url;
    ProgressDialog dialog;

    BackgroundTask(Context ctx){
        this.ctx = ctx;
        dialog = new ProgressDialog(ctx);
    }

    @Override
    protected void onPreExecute() {
        json_url= "http://192.168.43.243:80/PassEntries/json_put_data.php";
        dialog.setMessage("Processing...");
        dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        URL url;
        HttpURLConnection httpURLConnection = null;
        try {
            url = new URL(json_url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data =
                    URLEncoder.encode("collegename", "UTF-8")+"="+URLEncoder.encode(params[0],"UTF-8")+"&"+
                    URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8")+"&"+
                    URLEncoder.encode("mobile", "UTF-8")+"="+URLEncoder.encode(params[2],"UTF-8")+"&"+
                    URLEncoder.encode("age", "UTF-8")+"="+URLEncoder.encode(params[3],"UTF-8")+"&"+
                    URLEncoder.encode("gender", "UTF-8")+"="+URLEncoder.encode(params[4],"UTF-8")+"&"+
                    URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(params[5],"UTF-8");
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
