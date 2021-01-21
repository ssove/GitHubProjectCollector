public class SearchOption {
    private final String q;
    private final String sort;
    private final String order;
    private final int itemsPerPage;
    private final int page;


    private SearchOption(Builder builder) {
        q               = builder.q;
        sort            = builder.sort;
        order           = builder.order;
        itemsPerPage    = builder.itemsPerPage;
        page            = builder.page;
    }

    public String q()           { return q; }
    public String sort()        { return sort; }
    public String order()       { return order; }
    public int itemsPerPage()   { return itemsPerPage; }
    public int page()           { return page; }

    static class Builder {
        // Required parameters

        // Optional parameters
        private String q = "";
        private String sort         = "stars";
        private String order        = "desc";
        private int itemsPerPage    = 30;
        private int page            = 1;

        public Builder q(String val) {
            q = val;
            return this;
        }

        public Builder sort(String val) {
            sort = val;
            return this;
        }

        public Builder order(String val) {
            order = val;
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
