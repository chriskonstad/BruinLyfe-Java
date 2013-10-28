package com.bruinlyfe.bruinlyfe;

/**
 * Created by chris on 10/26/13.
 */
public class MenuDownloader extends DownloadTask {
    MenuLoader menuLoader;

    @Override
    protected  void onPostExecute(String result) {
        menuLoader.mainActivity.cacheMenuData(result);
        menuLoader.parseData(result);
    }

    public void setMenuLoader(MenuLoader mL) {
        menuLoader = mL;
    }
}