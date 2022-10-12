package edu.vtc.kurs.util;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
public class SerpApiHttpClient {
    private final int httpConnectionTimeout = 6000;
    private int httpReadTimeout;

    private static final Gson gson = new Gson();

    public String path = "/search";


    protected HttpURLConnection buildConnection(String path, Map<String, String> parameter) throws SerpApiSearchException {
        HttpURLConnection con;
        try {
            allowHTTPS();
            String query = ParameterStringBuilder.getParamsString(parameter);
            URL url = new URL("https://serpapi.com" + path + "?" + query);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
        } catch (IOException e) {
            throw new SerpApiSearchException(e);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            throw new SerpApiSearchException(e);
        }

        String outputFormat = parameter.get("output");
        if (outputFormat == null) {
            if (path.startsWith("/search?")) {
                throw new SerpApiSearchException("output format must be defined: " + path);
            }
        } else if (outputFormat.startsWith("json")) {
            con.setRequestProperty("Content-Type", "application/json");
        }
        con.setConnectTimeout(getHttpConnectionTimeout());
        con.setReadTimeout(getHttpReadTimeout());

        con.setDoOutput(true);
        return con;
    }

    private void allowHTTPS() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }

        }};

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier allHostsValid = (hostname, session) -> true;
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
    public String getResults(Map<String, String> parameter) throws SerpApiSearchException {
        HttpURLConnection con = buildConnection(this.path, parameter);
        int statusCode;
        InputStream is;
        BufferedReader in;
        try {
            statusCode = con.getResponseCode();
            if (statusCode == 200) {
                is = con.getInputStream();
            } else {
                is = con.getErrorStream();
            }
            in = new BufferedReader(new InputStreamReader(is));
        } catch (IOException e) {
            throw new SerpApiSearchException(e);
        }
        String inputLine;
        StringBuilder content = new StringBuilder();
        try {
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            throw new SerpApiSearchException(e);
        }
        con.disconnect();
        if (statusCode != 200) {
            triggerSerpApiClientException(content.toString());
        }
        return content.toString();
    }
    protected void triggerSerpApiClientException(String content) throws SerpApiSearchException {
        String errorMessage;
        try {
            JsonObject element = gson.fromJson(content, JsonObject.class);
            errorMessage = element.get("error").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("invalid response format: " + content);
        }
        throw new SerpApiSearchException(errorMessage);
    }
    public int getHttpConnectionTimeout() {
        return httpConnectionTimeout;
    }
    public int getHttpReadTimeout() {
        return httpReadTimeout;
    }
}