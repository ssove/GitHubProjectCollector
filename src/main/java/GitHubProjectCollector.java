import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class GitHubProjectCollector {
    private final String API_SERVER = "https://api.github.com";
    private final String SEARCH_API_URL = API_SERVER + "/search";
    private final String REPO_SEARCH_API_URL = SEARCH_API_URL + "/repositories";
    public static void main(String[] args) throws IOException {
        GitHubRESTAPI api = new GitHubRESTAPI();
        GitHubProjectCollector collector = new GitHubProjectCollector();

        Response resCProjectsRetrieving = api.get(collector.REPO_SEARCH_API_URL + "?q=stars:>=10+language:c&sort=stars&order=desc");
        Response res = api.get(collector.API_SERVER + "/repos/ssove/GitHubProjectCollector/tarball/");
        System.out.println(res.toString());
        String fileUrl = res.request().url().toString();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(fileUrl)
                .build();
        CallbackToDownloadFile callbackToDownloadFile = new CallbackToDownloadFile("projects\\", "downloaded.zip");
        client.newCall(request).enqueue(callbackToDownloadFile);
    }
}