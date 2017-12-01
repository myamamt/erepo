package erepo.domain.repository;

import erepo.domain.model.ErrorInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ErrorInfoRepository extends JpaRepository<ErrorInfo, Integer> {
    public List<ErrorInfo> findTop10ByOrderByDateDesc();
    public List<ErrorInfo> findByUrlContains(String url);
}
