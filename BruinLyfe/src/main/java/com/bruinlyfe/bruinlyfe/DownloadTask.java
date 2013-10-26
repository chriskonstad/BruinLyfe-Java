package com.bruinlyfe.bruinlyfe;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by chris on 10/26/13.
 */
public class DownloadTask extends AsyncTask<String, Void, String> {
    String finalResult = "";
    MainActivity myMainActivity;

    public void setParentFragment(MainActivity mA) {
        myMainActivity = mA;
    }

    @Override
    protected String doInBackground(String... urls) {
        HttpResponse response = null;
        HttpGet httpGet = null;
        HttpClient mHttpClient = null;
        String s = "";

        try {
            if(mHttpClient == null){
                mHttpClient = new DefaultHttpClient();
            }
            httpGet = new HttpGet(urls[0]);
            response = mHttpClient.execute(httpGet);
            s = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finalResult = s;
        return s;
    }

    @Override
    protected void onPostExecute(String result){
        //Do something cool?
        Log.w("BruinLyfe", "Done downloading dining hours!");
        myMainActivity.fillDiningHours(myMainActivity.findMatches(result));
    }

    public String getFinalResult() {
        return finalResult;
    }
}