package com.bruinlyfe.bruinlyfe;

import android.util.Log;

/**
 * Created by chris on 10/26/13.
 */
public class MenuDownloader extends DownloadTask {
    MenuLoader menuLoader;

    @Override
    protected  void onPostExecute(String result) {
        Log.w("BruinLyfe", "Done downloading menu data!");
        menuLoader.parseData(result);
    }

    public void setMenuLoader(MenuLoader mL) {
        menuLoader = mL;
    }
}