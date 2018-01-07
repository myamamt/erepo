package erepo.domain.service;

import erepo.domain.model.CategoryCount;
import erepo.domain.model.DateCount;
import erepo.domain.model.ErrorInfo;
import erepo.domain.repository.ErrorInfoRepository;
import erepo.domain.repository.ErrorInfoRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class ErrorInfoService {
    private final ErrorInfoRepository repository;
    private final ErrorInfoRepositoryCustom repositoryCustom;

    @Autowired
    public ErrorInfoService(ErrorInfoRepository repository, ErrorInfoRepositoryCustom repositoryCustom) {
        this.repository = repository;
        this.repositoryCustom = repositoryCustom;
    }

    public List<ErrorInfo> findTop6ByOrderByDateDesc() {
        return repository.findTop6ByRemarksIsNullOrderByDateDesc();
    }

    public List<CategoryCount> findCategoryOrderByCount() {
        return repositoryCustom.findCategoryOrderByCount();
    }

    public List<DateCount> findDateCountByUrlContainsAndDuring25days(String url) {
        return repositoryCustom.findDateCountByRemarksIsNullAndUrlContainsAndDuring25days(url);
    }

    public List<ErrorInfo> findByUrlContainsOrderByDateDesc(String url, Pageable pageable) {
        return repository.findByRemarksIsNullAndUrlContainsOrderByDateDesc(url, pageable);
    }

    public Integer countByUrlContains(String url) {
        return repository.countByRemarksIsNullAndUrlContains(url);
    }

    public ErrorInfo findOne(Integer id) {
        return repository.findOne(id);
    }

    public ErrorInfo save(ErrorInfo errorInfo) {
        return repository.save(errorInfo);
    }

    public Integer deleteByUrlAndDateAndUserAgent(String url, Timestamp date, String userAgent) {
        return repository.deleteByUrlAndDateAndUserAgent(url, date, userAgent);
    }
}
