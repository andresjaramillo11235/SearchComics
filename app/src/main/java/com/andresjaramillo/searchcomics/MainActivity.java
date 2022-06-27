package com.andresjaramillo.searchcomics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * class that prints the list of comics where the hero of the search appears
 *
 * @author Andres Jaramillo
 * @version 0.1
 * 2022/06/26
 */

public class MainActivity extends AppCompatActivity {

    ArrayList<ListComicsVo> dataList;
    RecyclerView recycler;
    TextView tvHeroName;
    ListComicsVo listComicsVo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvHeroName = (TextView) findViewById(R.id.tvHeroName_id);
        dataList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("jasonObject"));
            buildRecycler(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_title_listing_comics);
    }

    /* Show action bar */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.abmenu, menu);
        return true;
    }

    /* Assign functions menu */
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.itemSearch_id) {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    /* Put the name of the hero */
    private void putHeroName(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has("name")) {
                    String nh = jsonObject.getString("name");
                    tvHeroName.setText(nh);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /* Fill and build the recycler view and place the links for the detail. */
    private void putRecyclerComics(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject object = jsonArray.optJSONObject(i);
            Log.d("ANJARAMI nh", object.toString());
            listComicsVo = new ListComicsVo();
            listComicsVo.setResource(object.optString("resourceURI"));
            listComicsVo.setName(object.optString("name"));

            dataList.add(listComicsVo);
        }

        AdapterList adapter = new AdapterList(dataList);
        recycler.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("resource", dataList.get(recycler.getChildAdapterPosition(view)).getResource());
                startActivity(intent);
            }
        });
    }

    /* Print the recycler view */
    private void buildRecycler(JSONObject jsonObject) {

        recycler = (RecyclerView) findViewById(R.id.recyclerId);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        JSONArray arrayData;
        try {
            JSONArray arrayInfo = jsonObject.getJSONArray("results");
            arrayData = jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("comics").getJSONArray("items");
            putHeroName(arrayInfo);
            putRecyclerComics(arrayData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}




