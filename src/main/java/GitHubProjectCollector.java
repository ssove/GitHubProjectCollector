import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

public class GitHubProjectCollector {
    public static final String API_SERVER = "https://api.github.com";
    public static final String SEARCH_API_URL = API_SERVER + "/search";
    public static final String REPO_SEARCH_API_URL = SEARCH_API_URL + "/repositories";
    public static final String REPO_DOWNLOAD_API_URL_PREFIX = API_SERVER + "/repos/";
    public static final String REPO_DOWNLOAD_API_URL_SUFFIX = "/tarball/";


    public boolean isPrivate(JSONObject project) {
        return (boolean) project.get("private");
    }

    public static void main(String[] args) {
        try {
            GitHubProjectCollector collector = new GitHubProjectCollector();
            Response resCProjectsRetrieving = GitHubRESTAPI.get(collector.REPO_SEARCH_API_URL + "?q=stars:>=100+language:c+language:csharp+language:cpp&sort=stars&order=desc&per_page:100");
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(resCProjectsRetrieving.body().string());
            JSONArray projects = (JSONArray) jsonObject.get("items");

            for (int i = 0; i < 3000; i++) {
                JSONObject project = (JSONObject) projects.get(i);

                if (collector.isPrivate(project))
                    continue;

                JSONObject ownerInfo = (JSONObject) project.get("owner");
                String owner = (String) ownerInfo.get("login");
                String projectName = (String) project.get("name");
                GitHubRESTAPI.downloadRepository(owner, projectName, "project/", owner + projectName + ".tar");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}