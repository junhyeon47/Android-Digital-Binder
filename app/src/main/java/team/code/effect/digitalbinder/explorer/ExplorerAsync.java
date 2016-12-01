package team.code.effect.digitalbinder.explorer;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by student on 2016-12-01.
 */

public class ExplorerAsync extends AsyncTask<String, Void, String> {

    String TAG;
    Context context;

    public ExplorerAsync(Context context) {
        this.context=context;
        TAG=this.getClass().getName();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected String doInBackground(String... strings) {


        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


    }
}
