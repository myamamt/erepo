package erepo.domain.service;

import erepo.domain.model.CategoryCount;
import erepo.domain.model.ErrorInfo;
import erepo.domain.repository.ErrorInfoRepository;
import erepo.domain.repository.ErrorInfoRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<ErrorInfo> findTop10ByOrderByDateDesc() {
        return repository.findTop10ByOrderByDateDesc();
    }

    public List<CategoryCount> findCategoryOrderByCount() {
        return repositoryCustom.findCategoryOrderByCount();
    }

    public List<ErrorInfo> findByUrlContains(String url) {
        return repository.findByUrlContains(url);
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
