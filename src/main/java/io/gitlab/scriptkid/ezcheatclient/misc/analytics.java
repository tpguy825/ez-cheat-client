package io.gitlab.scriptkid.ezcheatclient.misc;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;


import okhttp3.*;

public class analytics {

    private static String endpoint = "https://www.google-analytics.com/collect";
    private static String googleVersion = "1";
    private static String googleTrackingId = "";

    public static void track(String category, String action) {
        track(category, action, null, null);
    }

    public static void track(String category, String action, String label, Integer value) {

        if(FakeCheat.Settings.get("DisableGoogleAnalytics") == true) return;

        try {

            OkHttpClient client = new OkHttpClient();

            FormBody.Builder postdata = new FormBody.Builder();

            String clientId = utils.MD5(System.getProperty("user.name"));

            if(clientId == null || clientId == "" || googleTrackingId == "") return;

            postdata.add("v", googleVersion);
            postdata.add("tid", googleTrackingId);
            postdata.add("cid", clientId);
            postdata.add("t", "event");
            postdata.add("ec", category);
            postdata.add("ea", action);

            if (label != null) {
                postdata.add("el", label);
            }

            if (value != null) {
                postdata.add("ev", value.toString());
            }

            RequestBody formBody = postdata.build();

            Request request = new Request.Builder()
                    .url(endpoint)
                    .header("User-Agent", "EzCheatClient")
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.addErrorEntry("analytics: " + e.getMessage());
        }

    }
}