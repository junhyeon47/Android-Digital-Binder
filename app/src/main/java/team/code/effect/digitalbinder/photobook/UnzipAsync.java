package team.code.effect.digitalbinder.photobook;

import android.os.AsyncTask;

import java.io.File;
import java.util.List;

import team.code.effect.digitalbinder.common.AppConstans;

public class UnzipAsync extends AsyncTask<String, String, List>{
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected List doInBackground(String... params) {
        File dir = new File(AppConstans.TEMP_PHOTOBOOK_PATH);
        if(!dir.isDirectory()) {
            if(dir.mkdirs()){

            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(List list) {

    }

    @Override
    protected void onProgressUpdate(String... values) {

    }
}
