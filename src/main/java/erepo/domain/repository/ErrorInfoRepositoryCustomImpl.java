package erepo.domain.repository;

import erepo.domain.model.CategoryCount;
import erepo.domain.model.ErrorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Component
public class ErrorInfoRepositoryCustomImpl implements ErrorInfoRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    @Override
    public List<CategoryCount> findCategoryOrderByCount() {
        List<Object[]> results = entityManager.createNativeQuery("SELECT category, COUNT(category) AS cnt FROM Info GROUP BY category ORDER BY cnt DESC").getResultList();
        List<CategoryCount> list = new ArrayList<>();
        for (Object[] result : results) {
            list.add(new CategoryCount((String) result[0], ((Number) result[1]).intValue()));
        }
        return list;
    }
}
