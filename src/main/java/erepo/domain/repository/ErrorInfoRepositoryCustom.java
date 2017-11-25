package erepo.domain.repository;

import erepo.domain.model.CategoryCount;

import java.util.List;

public interface ErrorInfoRepositoryCustom {
    public List<CategoryCount> findCategoryOrderByCount();
}
