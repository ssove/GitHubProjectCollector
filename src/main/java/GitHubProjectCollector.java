import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class GitHubProjectCollector {
    public static final String API_SERVER = "https://api.github.com";
    public static final String SEARCH_API_URL = API_SERVER + "/search";
    public static final String REPO_SEARCH_API_URL = SEARCH_API_URL + "/repositories";
    public static final String REPO_DOWNLOAD_API_URL_PREFIX = API_SERVER + "/repos/";
    public static final String REPO_DOWNLOAD_API_URL_SUFFIX = "/tarball/";
    public static final int MAX_REQUEST_PER_MIN = 30;


    public static String constructRepositorySearchUrl(SearchOption searchOption) {
        return REPO_SEARCH_API_URL
                + "?q="         + searchOption.q()
                + "&sort="      + searchOption.sort()
                + "&order="     + searchOption.order()
                + "&per_page="  + searchOption.itemsPerPage()
                + "&page="      + searchOption.page();
    }

    public static String constructRepositoryDownloadUrl(String owner, String projectName) {
        return GitHubProjectCollector.REPO_DOWNLOAD_API_URL_PREFIX + owner
                + "/" + projectName + GitHubProjectCollector.REPO_DOWNLOAD_API_URL_SUFFIX;
    }

    public static JSONObject searchProjects(SearchOption searchOption) throws IOException, ParseException {
        String url = GitHubProjectCollector.constructRepositorySearchUrl(searchOption);
        Response res = GitHubRESTAPI.get(url);

        return (JSONObject) (new JSONParser()).parse(res.body().string());
    }

    public static void downloadProjects(SearchOption searchOption, String destination) {
        JSONObject jsonObject;

        try {
            jsonObject = searchProjects(searchOption);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return;
        }

        Long totalCountOfSearchResults = (Long) jsonObject.get("total_count");
        JSONArray projects = (JSONArray) jsonObject.get("items");

        for (int i = 0; i < totalCountOfSearchResults / 3; i++) {
            JSONObject project = (JSONObject) projects.get(i);
            JSONObject ownerInfo = (JSONObject) project.get("owner");
            String owner = (String) ownerInfo.get("login");
            String projectName = (String) project.get("name");
            String url = constructRepositoryDownloadUrl(owner, projectName);

            GitHubRESTAPI.downloadRepository(url, destination, projectName + ".zip");
        }
    }

    public static void main(String[] args) {
        SearchOption searchOption = new SearchOption.Builder()
                .q("stars:>=100+language:c+language:csharp+language:cpp+is:public")
                .sort("stars")
                .order("desc")
                .build();
        GitHubProjectCollector.downloadProjects(searchOption, "project");
    }
}