package erepo.app;

import erepo.domain.model.ErrorInfo;
import erepo.domain.service.ErrorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/info")
public class ErrorInfoRestController {
    @Autowired
    private ErrorInfoService errorInfoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ErrorInfo postErrorInfo(@RequestBody ErrorInfo errorInfo) {
        int index = errorInfo.getMessage().indexOf(':');
        errorInfo.setCategory(index == -1 ? "" : errorInfo.getMessage().substring(0, index));
        return errorInfoService.save(errorInfo);
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public Integer removeErrorInfo(@RequestBody ErrorInfo errorInfo) {
        return errorInfoService.deleteByUrlAndDateAndUserAgent(errorInfo.getUrl(), errorInfo.getDate(), errorInfo.getUserAgent());
    }
}
