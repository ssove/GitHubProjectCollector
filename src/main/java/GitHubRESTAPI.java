import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class GitHubRESTAPI {
    public static Response get(String requestUrl) {
        Response res = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(requestUrl)
                    .build();

            res = client.newCall(request).execute();
        } catch (Exception e){
            System.err.println(e.toString());
        }
        return res;
    }

    public static void downloadRepository(String owner, String projectName, String dir, String fileName) {
        String downloadUrl = GitHubProjectCollector.REPO_DOWNLOAD_API_URL_PREFIX + owner + "/" + projectName + GitHubProjectCollector.REPO_DOWNLOAD_API_URL_SUFFIX;
        Response res = GitHubRESTAPI.get(downloadUrl);
        String fileUrl = res.request().url().toString();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(fileUrl)
                .build();
        CallbackToDownloadFile callbackToDownloadFile = new CallbackToDownloadFile(dir, fileName);
        client.newCall(request).enqueue(callbackToDownloadFile);
    }

}

class CallbackToDownloadFile implements Callback {
    private File dir;
    private File fileToBeDownloaded;

    public CallbackToDownloadFile(String directory, String fileName) {
        this.dir = new File(directory);
        this.fileToBeDownloaded = new File(this.dir.getAbsolutePath() + File.separator + fileName);
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        e.printStackTrace();
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (fileToBeDownloaded.exists()) {
            fileToBeDownloaded.delete();
        }

        try {
            fileToBeDownloaded.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        InputStream inputStream = response.body().byteStream();
        OutputStream outputStream = new FileOutputStream(this.fileToBeDownloaded);
        final int BUFFER_SIZE = 2048;
        byte[] data = new byte[BUFFER_SIZE];

        int count = 0;
        long total = 0;

        while ((count = inputStream.read(data)) != -1) {
            total += count;
            outputStream.write(data, 0, count);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
        System.out.println(fileToBeDownloaded + " is downloaded completely.");
    }
}