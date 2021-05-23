package io.gitlab.scriptkid.ezcheatclient.misc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

public class oauth2 {

    private static String endpoint = "https://oauth2.googleapis.com/token";

    /*
        For replay uploading to work you need to create client_id and client_secret for google apis.
        Refresh token can be generated using client_id and client_secret, google is your friend :)
     */
    private static String client_id = "";
    private static String client_secret = "";
    private static String refresh_token = "";

    public static String getApiToken() {

        if(client_id == "" || client_secret == "" || refresh_token == "") {
            System.out.println("Error: Missing Google Api Refresh Token");
            return "";
        }

        try {

            OkHttpClient client = new OkHttpClient();

            FormBody.Builder postdata = new FormBody.Builder();

            postdata.add("client_id", client_id);
            postdata.add("client_secret", client_secret);
            postdata.add("refresh_token", refresh_token);
            postdata.add("grant_type", "refresh_token");

            RequestBody formBody = postdata.build();

            Request request = new Request.Builder()
                    .url(endpoint)
                    .header("User-Agent", "EzCheatClient")
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();

            if(response != null) {
                String responseBody = response.body().string();
                log.addErrorEntry(responseBody);
                if(responseBody != null) {
                    JsonObject jsonObject = new JsonParser().parse(responseBody).getAsJsonObject();
                    return jsonObject.get("access_token").getAsString();
                }
            }

        } catch(Exception e)
        {
            System.out.println(e.getMessage());
            log.addErrorEntry("oauth2: " + e.getMessage());
        }

        return "";
    }

}
