package com.andresjaramillo.searchcomics.model;

import android.util.Log;

public class Service {

    private String publicAPIKey = "1d859c890288d0592d03a24460c30cfd";
    private String privateAPIKey = "bb9a9fc09da5657b57bee7f1519d55c6b71262fe";
    private String ts = "1";
    private String HASH = "b1d53750c6a5894a684a7633b734cef7";
    private String baseUrl = "https://gateway.marvel.com/v1/public/";
    private String url;

    public Service() {

    }

    public String requestHeroData(String heroName) {

        String url = this.baseUrl + "characters?name=" + heroName + "&ts=" + this.ts;
        url += "&apikey=" + this.publicAPIKey + "&hash=" + this.HASH;

        return url;
    }

    public String requesDetailComic(String resource) {

        String[] split = resource.split(":");
        Log.d("ANJARAMI", split[0]);

        this.url = "https:" + split[1] + "?ts=" + this.ts;
        this.url += "&apikey=" + this.publicAPIKey + "&hash=" + this.HASH;

        return this.url;
    }


}
