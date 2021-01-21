public class SearchOption {
    private final int minimumStars;
    private final int maximumStars;
    private final String[] languages;
    private final int itemsPerPage;
    private final int page;

    private SearchOption(Builder builder) {
        minimumStars    = builder.minimumStars;
        maximumStars    = builder.maximumStars;
        languages       = builder.languages.clone();
        itemsPerPage    = builder.itemsPerPage;
        page            = builder.page;
    }

    public int minimumStars()   { return minimumStars; }
    public int maximumStars()   { return maximumStars; }
    public String[] languages() { return languages; }
    public int itemsPerPage()   { return itemsPerPage; }
    public int page()           { return page; }

    static class Builder {
        // Required parameters

        // Optional parameters
        private int minimumStars    = 0;
        private int maximumStars    = 0;
        private String[] languages  = null;
        private int itemsPerPage    = 30;
        private int page            = 1;

        public Builder minimumStars(int val) {
            minimumStars = val;
            return this;
        }

        public Builder maximumStars(int val) {
            maximumStars = val;
            return this;
        }

        public Builder languages(String[] val) {
            languages = val.clone();
            return this;
        }

        public Builder itemsPerPage(int val) {
            itemsPerPage = val;
            return this;
        }

        public Builder page(int val) {
            page = val;
            return this;
        }

        public SearchOption build() {
            return new SearchOption(this);
        }
    }
}
