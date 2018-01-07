package erepo.domain.repository;

import erepo.domain.model.CategoryCount;
import erepo.domain.model.DateCount;
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
        List<Object[]> results = entityManager.createNativeQuery("SELECT category, COUNT(category) AS cnt FROM Info WHERE remarks IS NULL GROUP BY category ORDER BY cnt DESC").getResultList();
        List<CategoryCount> list = new ArrayList<>();
        for (Object[] result : results) {
            list.add(new CategoryCount((String) result[0], ((Number) result[1]).intValue(), ""));
        }
        return list;
    }

    @Override
    public List<DateCount> findDateCountByUrlByDuring30days(String url) {
        List<Object[]> results = entityManager.createNativeQuery("SELECT truncate(date) as d, COUNT(*) FROM Info WHERE date >= DATEADD('DAY', -29, truncate(dateadd('HOUR', 9, current_timestamp()))) GROUP BY d ORDER BY d").getResultList();
        List<DateCount> list = new ArrayList<>();
        for (Object[] result : results) {
            String dateStr = (String) result[0];
            int count = ((Number) result[1]).intValue();

            list.add(new DateCount(Integer.parseInt(dateStr.substring(5, 7)), Integer.parseInt(dateStr.substring(8, 10)), count));
        }
        return list;
    }
}
