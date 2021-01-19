public class GitHubProjectCollector {
    public static void main(String[] args) {
        GitHubRESTAPI api = new GitHubRESTAPI();
        api.get("https://api.github.com/");
    }
}
