import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ProjectCollectingTest {
    public static void main(String[] args) {
        SearchOption searchOption = new SearchOption.Builder()
                .q("stars:>=100+language:c+language:csharp+language:cpp+is:public")
                .sort("stars")
                .order("desc")
                .build();
        try {
            JSONObject jsonObject = GitHubProjectCollector.searchProjects(searchOption);
            Long totalCountOfSearchResults = (Long) jsonObject.get("total_count");
            int projectsPerPage = 100;

            for (int curPage = 1; curPage < totalCountOfSearchResults / projectsPerPage; curPage++) {
                SearchOption option = new SearchOption.Builder()
                        .q("stars:>=100+language:c+language:csharp+language:cpp+is:public")
                        .sort("stars")
                        .order("desc")
                        .page(curPage)
                        .itemsPerPage(projectsPerPage)
                        .build();
                JSONObject searchResult = GitHubProjectCollector.searchProjects(option);
                JSONArray projects = (JSONArray) searchResult.get("items");
                JSONObject project;
                for (int i = 0; i < projectsPerPage; i++) {
                    project = (JSONObject) projects.get(i);
                    GitHubProjectCollector.downloadProject(GitHubProjectCollector.getFullNameOfProject(project), "project");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
