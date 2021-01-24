import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class GitHubProjectCollector {
    public static final String API_SERVER = "https://api.github.com";
    public static final String SEARCH_API_URL = API_SERVER + "/search";
    public static final String PROJECT_SEARCH_API_URL = SEARCH_API_URL + "/repositories";
    public static final String PROJECT_DOWNLOAD_API_URL_PREFIX = API_SERVER + "/repos/";
    public static final String PROJECT_DOWNLOAD_API_URL_SUFFIX = "/tarball/";
    public static final int MAX_REQUEST_PER_MIN = 30;


    public static String constructProjectSearchUrl(SearchOption searchOption) {
        return PROJECT_SEARCH_API_URL
                + "?q="         + searchOption.q()
                + "&sort="      + searchOption.sort()
                + "&order="     + searchOption.order()
                + "&per_page="  + searchOption.itemsPerPage()
                + "&page="      + searchOption.page();
    }

    public static String constructProjectDownloadUrl(String fullNameOfProject) {
        return GitHubProjectCollector.PROJECT_DOWNLOAD_API_URL_PREFIX + fullNameOfProject
                + GitHubProjectCollector.PROJECT_DOWNLOAD_API_URL_SUFFIX;
    }

    public static JSONObject searchProjects(SearchOption searchOption) throws IOException, ParseException {
        String url = GitHubProjectCollector.constructProjectSearchUrl(searchOption);
        Response res = GitHubRESTAPI.get(url);

        return (JSONObject) (new JSONParser()).parse(res.body().string());
    }

    public static String getFullNameOfProject(JSONObject projectInfo) {
        JSONObject ownerInfo = (JSONObject) projectInfo.get("owner");

        return (String) ownerInfo.get("login") + "/" + (String) projectInfo.get("name");
    }

    public static void downloadProject(String fullNameOfProject, String destination) {
        String url = constructProjectDownloadUrl(fullNameOfProject);
        GitHubRESTAPI.downloadProject(url, destination, fullNameOfProject.substring(fullNameOfProject.lastIndexOf("/")) + ".zip");
    }
}