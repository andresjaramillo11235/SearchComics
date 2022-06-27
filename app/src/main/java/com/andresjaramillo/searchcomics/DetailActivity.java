package com.andresjaramillo.searchcomics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.andresjaramillo.searchcomics.model.Service;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Prints the detail of the selected comic.
 *
 * @author Andres Jaramillo
 * @version 0.1
 * 2022/06/26
 */

public class DetailActivity extends AppCompatActivity {

    String resource;
    TextView tvTitle, tvWriter, tvPublished, tvSummary;
    ImageView ivComic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        resource = getIntent().getExtras().getString("resource");
        tvTitle = (TextView) findViewById(R.id.tvTitle_id);
        tvSummary = (TextView) findViewById(R.id.tvSummary_id);
        tvPublished = (TextView) findViewById(R.id.tvPublished_id);
        tvWriter = (TextView) findViewById(R.id.tvWriter_id);
        ivComic = (ImageView) findViewById(R.id.ivComic_id);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_title_detail_comic);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        detailComic();
    }

    /**
     * Show action bar
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.abmenu, menu);
        return true;
    }

    /**
     * capture bar events
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.itemSearch_id:
                Intent i = new Intent(DetailActivity.this, SearchActivity.class);
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Retrieves the comic information from the API.
     */
    private void detailComic() {

        Service service = new Service();
        String url = service.requesDetailComic(resource);
        Log.d("ANJARAMI URL=>", url);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> putDetail(response),
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailActivity.this, getString(R.string.internet_connection_failure), Toast.LENGTH_SHORT).show();
                        Log.e("ANJARAMI", error.getMessage());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Print the detail of the comic
     */
    private void putDetail(JSONObject jsonObject) {

        try {
            JSONObject dataResponse = jsonObject.getJSONObject("data");
            JSONArray jsonArray = dataResponse.getJSONArray("results");
            JSONArray arrayCreators = jsonArray.getJSONObject(0).getJSONObject("creators").getJSONArray("items");

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject arrayJSONObject = jsonArray.getJSONObject(i);

                    if (arrayJSONObject.has("title")) {
                        String data = arrayJSONObject.getString("title");
                        tvTitle.setText(data);
                    }
                    if (arrayJSONObject.has("description")) {
                        String data = arrayJSONObject.getString("description");
                        tvSummary.setText(data.equals("null") || data.isEmpty() ? getString(R.string.comic_description_empty) : data);
                    }
                    if (arrayJSONObject.has("modified")) {
                        String data = arrayJSONObject.getString("modified");
                        tvPublished.setText(data.equals("null") || data.isEmpty() ? getString(R.string.comic_published_empty) : formatDate(data));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            putCreators(arrayCreators);
            putThumbnail(jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Build the creators textview
     */
    private void putCreators(JSONArray jsonArray) {

        String creators = "";

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject arrayJSONObject = jsonArray.getJSONObject(i);

                if (arrayJSONObject.has("name")) {
                    creators += arrayJSONObject.getString("name") + " ";
                }
                if (arrayJSONObject.has("role")) {
                    creators += arrayJSONObject.getString("role") + "\n";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        tvWriter.setText(creators);
    }

    /**
     * Retrieve and print the image
     */
    private void putThumbnail(JSONArray jsonArray) {

        String thumbnail = null;

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject imgResponse = jsonArray.getJSONObject(0).getJSONObject("thumbnail");

                if (imgResponse.has("path")) {
                    String data = imgResponse.getString("path");
                    thumbnail = data + "/portrait_xlarge.jpg";

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (thumbnail != null) {
            thumbnail = thumbnail.replace("http", "https");
        }

        Picasso.get()
                .load(thumbnail)
                .error(R.drawable.marvel)
                .into(ivComic);
    }

    /**
     * Change the display format of the date
     */
    private String formatDate(String date) {

        Date d = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
        try {
            d = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedTime = output.format(d);

        return formattedTime;
    }

}
