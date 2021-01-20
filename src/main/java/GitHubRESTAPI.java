import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GitHubRESTAPI {
    public Response get(String requestURL) {
        Response res = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(requestURL)
                    .build();

            res = client.newCall(request).execute();
        } catch (Exception e){
            System.err.println(e.toString());
        }
        return res;
    }

}
