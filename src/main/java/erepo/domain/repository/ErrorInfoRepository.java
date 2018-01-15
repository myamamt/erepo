package erepo.domain.repository;

import erepo.domain.model.ErrorInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface ErrorInfoRepository extends JpaRepository<ErrorInfo, Integer> {
    public List<ErrorInfo> findTop6ByRemarksIsNullOrderByDateDesc();
    public List<ErrorInfo> findByRemarksIsNullAndUrlContainsOrderByDateDesc(String url, Pageable pageable);
    public List<ErrorInfo> findByRemarksOrderByDateDesc(String remarks, Pageable pageable);
    public Integer countByRemarksIsNullAndUrlContains(String url);
    public Integer countByRemarks(String remarks);
    public Integer deleteByUrlAndDateAndUserAgent(String url, Timestamp date, String userAgent);
}
