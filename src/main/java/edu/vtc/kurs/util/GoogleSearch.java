package edu.vtc.kurs.util;

import java.util.Map;
import com.google.gson.JsonArray;

public class GoogleSearch extends SerpApiSearch {
  public GoogleSearch(Map<String, String> parameter) {
    super(parameter, "google");
  }

}