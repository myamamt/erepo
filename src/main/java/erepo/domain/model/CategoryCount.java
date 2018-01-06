package erepo.domain.model;

public class CategoryCount {
    public String category;
    public int count;
    public String percent;

    public CategoryCount(String category, int count, String percent) {
        this.category = category;
        this.count = count;
        this.percent = percent;
    }
}
