package printinghouse.data;

import java.io.Serializable;

public class Publication implements Serializable {
    private PublicationType type;
    private String title;
    private PageSize pageSize;
    private int pageCount;

    public Publication(PublicationType type, String title, PageSize pageSize, int pageCount) {
        this.type = type;
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        this.title = title;
        this.pageSize = pageSize;
        if (pageCount <= 0) {
            throw new IllegalArgumentException("Page count must be greater than 0");
        }
        this.pageCount = pageCount;
    }

    public PublicationType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public PageSize getPageSize() {
        return pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Type: " + type.toString() + ", Page size: " + pageSize + ", Page count: "
                + pageCount;
    }
}
