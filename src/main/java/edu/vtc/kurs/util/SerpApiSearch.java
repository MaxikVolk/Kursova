package edu.vtc.kurs.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.HashMap;

public class SerpApiSearch extends Exception {
    public static final String API_KEY_NAME = "api_key";
    public static String api_key_default;
    protected String api_key;
    protected String engine;
    public Map<String, String> parameter;
    private static final Gson gson = new Gson();
    public SerpApiHttpClient search;

    public SerpApiSearch(Map<String, String> parameter, String engine) {
        this.parameter = parameter;
        this.engine = engine;
    }

    public Map<String, String> buildQuery(String path, String output) throws SerpApiSearchException {
        if (search == null) {
            this.search = new SerpApiHttpClient(path);
            this.search.setHttpConnectionTimeout(6000);
        } else {
            this.search.path = path;
        }
        this.parameter.put("source", "java");
        if (this.parameter.get(API_KEY_NAME) == null) {
            if (this.api_key != null) {
                this.parameter.put(API_KEY_NAME, this.api_key);
            } else if (getApiKey() != null) {
                this.parameter.put(API_KEY_NAME, getApiKey());
            } else {
                throw new SerpApiSearchException(API_KEY_NAME + " is not defined");
            }
        }
        this.parameter.put("engine", this.engine);
        this.parameter.put("output", output);
        return this.parameter;
    }

    public static String getApiKey() {
        return api_key_default;
    }

    public String getHtml() throws SerpApiSearchException {
        Map<String, String> query = buildQuery("/search", "html");
        return search.getResults(query);
    }

    public JsonObject getJson() throws SerpApiSearchException {
        Map<String, String> query = buildQuery("/search", "json");
        return asJson(search.getResults(query));
    }

    public JsonObject asJson(String content) {
        JsonElement element = gson.fromJson(content, JsonElement.class);
        return element.getAsJsonObject();
    }
}