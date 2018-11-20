package com.geodetka.rtgtest;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class getJson extends AsyncTask <String, Void, String> {
    private Context mContext;

    //ProgressDialog mProgress;
    private TaskCompleted mCallback;


    public getJson(Context context){
        this.mContext = context;
        this.mCallback = (TaskCompleted) context;
    }

    @Override
    public void onPreExecute() {
//        mProgress = new ProgressDialog(mContext);
 //       mProgress.setMessage("Downloading \nPlease wait...");
 //       mProgress.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
//        mProgress.setMessage(values[0]);
    }

    @Override
    protected String doInBackground(String... values) {
        OkHttpClient client = new OkHttpClient();

        String url_of_trg_json = values[0];
        Request request = new Request.Builder().url(url_of_trg_json).build();
        Response response = null;

        try{
            response = client.newCall(request).execute();
            return response.body().string();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String results) {
        mCallback.onTaskComplete(results);
    }
}
