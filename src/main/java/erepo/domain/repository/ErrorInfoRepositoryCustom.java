package erepo.domain.repository;

import erepo.domain.model.CategoryCount;
import erepo.domain.model.DateCount;

import java.util.List;

public interface ErrorInfoRepositoryCustom {
    public List<CategoryCount> findCategoryOrderByCount();
    public List<DateCount> findDateCountByRemarksIsNullAndUrlContainsAndDuring25days(String url);
}
