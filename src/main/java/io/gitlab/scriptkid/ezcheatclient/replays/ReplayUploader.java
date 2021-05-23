package io.gitlab.scriptkid.ezcheatclient.replays;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.misc.*;
import okhttp3.*;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class ReplayUploader implements Runnable {

    private static String uploadFilesEndpoint = "https://www.googleapis.com/upload/drive/v3/files";
    private static String listFilesEndpoint = "https://www.googleapis.com/drive/v3/files";

    public static void checkForReplaysToUpload() {

        try {

            if(utils.getReplayDir() == null) return;

            if(FakeCheat.Settings.get("DisableReplayUpload") == true) return;

            analytics.track("ReplayUploader", "CheckForReplaysToUpload");

            Path replayDir = utils.getReplayDir();

            File[] directoryListing = replayDir.toFile().listFiles();
            if (directoryListing != null) {
                for (File replay : directoryListing) {

                    if (replay.getPath().endsWith(".mcpr")) {

                        File log = new File(replayDir + "\\" + replay.getName() + ".mclog");

                        if (log.exists()) {

                            analytics.track("ReplayUploader", "FoundReplay");

                            List<File> files = Arrays.asList(replay, log);
                            File zipfile = zip.files(files,replayDir + "\\" + replay.getName() + ".zip");

                            ReplayUploader.upload(zipfile, false);

                        } else {
                            // We could upload replay without punishment log but nah, lets bet it really means no punishments
                            //ReplayUploader.upload(replay);
                        }
                    } else if(replay.getPath().endsWith(".mcpr.tmp.zip")) {

                        ReplayUploader.upload(replay, true);

                    }

                }
            }
        }  catch(Exception e) {
            System.out.println(e.getMessage());
            log.addErrorEntry( "checkForReplaysToUpload: " + e.getMessage());
        }

    }

    private static String isFileUploadedAlready(File File, String AccessToken) {

        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + AccessToken)
                    .url(listFilesEndpoint + "?q=name+%3d+%27" + File.getName() + "%27")
                    .build();

            Response response = client.newCall(request).execute();

            if(response != null) {
                String responseBody = response.body().string();
                if(responseBody != null) {

                    JsonObject jsonObject = new JsonParser().parse(responseBody).getAsJsonObject();
                    if(jsonObject.get("files") != null && jsonObject.get("files").getAsJsonArray().size() > 0) {
                        String fileID = jsonObject.get("files").getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
                        return fileID;
                     }
                }
            }

        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            log.addErrorEntry( "checkForReplaysToUpload2: " + e.getMessage());
        }

        return "";
    }

    private static void upload(File File, Boolean replaceExisting) {

        try {
            analytics.track("ReplayUploader", "UploadingReplay");

            String AccessToken = oauth2.getApiToken();

            String fileID = "";

            if(replaceExisting) {
               fileID = isFileUploadedAlready(File, AccessToken);
               Thread.sleep(5000);
            }

            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody = new MultipartBody.Builder()
                .setType(MediaType.parse("multipart/related; charset=utf-8"))
                .addPart(RequestBody.create("{ name: \'" + File.getName() + "\' }", MediaType.parse("application/json")))
                .addFormDataPart("file", File.getName(), RequestBody.create(File, MediaType.parse("application/zip")))
                .build();

            Request.Builder builder = new Request.Builder();

            builder.addHeader("Content-Type", "multipart/related; boundary=file");
            builder.addHeader("Authorization", "Bearer " + AccessToken);

            if(fileID != "") {
                // Replace existing
                System.out.println("Replacing existing file in drive");
                analytics.track("ReplayUploader", "UploadingExistingReplay");
                System.out.println(uploadFilesEndpoint + "/" + fileID + "?uploadType=multipart");
                builder.url(uploadFilesEndpoint + "/" + fileID + "?uploadType=multipart");
                builder.patch(requestBody);
            } else {
                // Upload new file
                analytics.track("ReplayUploader", "UploadingNewReplay");
                System.out.println("Uploading new file to drive");
                builder.url(uploadFilesEndpoint + "?uploadType=multipart");
                builder.post(requestBody);
            }

            Request request = builder.build();

            Response response = client.newCall(request).execute();

            System.out.println(response.body().string());

            File.delete();

        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            log.addErrorEntry( "checkForReplaysToUpload3: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            checkForReplaysToUpload();
        } catch (Exception e) {
            log.addErrorEntry( "checkForReplaysToUpload4: " + e.getMessage());
        }
    }

}
