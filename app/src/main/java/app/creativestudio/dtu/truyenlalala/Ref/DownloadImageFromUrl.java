package app.creativestudio.dtu.truyenlalala.Ref;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageFromUrl extends AsyncTask<String, Void, Bitmap> {
    public interface DownloadImageFromUrlInterface{
        void onPostExecute(Bitmap result);
    }
    DownloadImageFromUrlInterface downloadImageFromUrlInterface;
    public DownloadImageFromUrl() {

    }
    void startDownload(String url,DownloadImageFromUrlInterface downloadImageFromUrlInterface1){
        downloadImageFromUrlInterface = downloadImageFromUrlInterface1;
        execute(url);
    }
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon;
    }

    protected void onPostExecute(Bitmap result) {
        downloadImageFromUrlInterface.onPostExecute(result);
    }
}
