package com.andresjaramillo.searchcomics;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.andresjaramillo.searchcomics.model.Service;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Class that performs the search for the hero
 *
 * @author Andres Jaramillo.
 * @version 0.1
 * 2022/06/26
 */

public class SearchActivity extends AppCompatActivity {

    EditText etSearch;
    Button btnSearch;
    TextView tvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearch = (EditText) findViewById(R.id.etSearchId);
        tvMsg = (TextView) findViewById(R.id.tvMsgId);
        btnSearch = (Button) findViewById(R.id.btnSearchId);

        controlButton();
        controlMessage();
    }

    /**
     * @param jsonObject data object returned from the API
     * @return boolean True or false depending on the result of the validation
     */
    private boolean validateData(JSONObject jsonObject) {

        boolean ok = Boolean.FALSE;

        Iterator<String> inter = jsonObject.keys();
        while (inter.hasNext()) {
            String key = inter.next();
            try {
                Object value = jsonObject.get(key);
                if (key.equals("count")) {

                    if (Integer.parseInt(value.toString()) == 0) {
                        ok = Boolean.FALSE;
                    } else if (Integer.parseInt(value.toString()) >= 1) {
                        ok = Boolean.TRUE;
                    }
                }
            } catch (JSONException e) {
                Log.d("ANJARAMI", "Error Respect en JSON: " + e.getMessage());
            }
        }

        return ok;
    }

    private void searchHero(String heroName) {

        Service service = new Service();
        String url = service.requestHeroData(heroName);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ANJARAMI", response.toString());

                        try {

                            JSONObject dataResponse = response.getJSONObject("data");

                            if (SearchActivity.this.validateData(dataResponse)) {
                                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                                intent.putExtra("jasonObject", dataResponse.toString());
                                SearchActivity.this.startActivity(intent);
                            } else {
                                tvMsg.setText(R.string.fail_hero_name_not_avalaible);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ANJARAMI", e.toString());
                            tvMsg.setText(R.string.internet_connection_failure);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ANJARAMI", error.getMessage());
                        tvMsg.setText(R.string.internet_connection_failure);
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void controlButton() {
        btnSearch.setOnClickListener(view -> {

            String heroName = etSearch.getText().toString().trim();

            if (heroName.isEmpty()) {
                tvMsg.setText(R.string.fail_empty_hero_name);
            } else {
                searchHero(heroName);
            }
        });
    }

    /**
     * Eliminate error feedback message.
     */
    private void controlMessage() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvMsg.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}