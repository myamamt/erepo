package erepo.domain.repository;

import erepo.domain.model.ErrorInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface ErrorInfoRepository extends JpaRepository<ErrorInfo, Integer> {
    public List<ErrorInfo> findTop6ByRemarksIsNullOrderByDateDesc();
    public List<ErrorInfo> findByRemarksIsNullAndUrlContainsOrderByDateDesc(String url);
    public Integer deleteByUrlAndDateAndUserAgent(String url, Timestamp date, String userAgent);
}
