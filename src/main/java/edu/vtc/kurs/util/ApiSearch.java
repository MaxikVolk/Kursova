package edu.vtc.kurs.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Map;

public class ApiSearch extends Exception {
    public Map<String, String> parameter;
    private static final Gson gson = new Gson();
    public SerpApiHttpClient search;

    public ApiSearch(Map<String, String> parameter) {
        this.parameter = parameter;
    }
    public JsonObject getJson() throws SerpApiSearchException {
        this.search = new SerpApiHttpClient();
        return asJson(search.getResults(parameter));
    }

    public JsonObject asJson(String content) {
        JsonElement element = gson.fromJson(content, JsonElement.class);
        return element.getAsJsonObject();
    }
}