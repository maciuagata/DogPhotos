package lt.vtvpmc.ems.zp182.photojson;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private LinearLayout list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (LinearLayout) findViewById(R.id.list);
        loadJSONFromAsset();
    }

    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView iv;

        public DownLoadImageTask(ImageView iv) {
            this.iv = iv;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result) {
            iv.setImageBitmap(result);
        }
    }

    public void loadJSONFromAsset() {
        ArrayList<String> list = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getAssets().open("dog_urls.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray m_jArry = obj.getJSONArray("urls");
            HashMap<String, String> urls;

            for (int i = 0; i < 10; i++) {
                String url_value = m_jArry.getString(i);

                ImageView view = new ImageView(this);
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                this.list.addView(view);
                new DownLoadImageTask(view).execute(url_value);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
