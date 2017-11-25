package erepo.app;

import erepo.domain.model.CategoryCount;
import erepo.domain.model.ErrorInfo;
import erepo.domain.service.ErrorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ErrorInfoService errorInfoService;

    @GetMapping("/")
    public String home(Model model) {
        List<ErrorInfo> recentInfos = errorInfoService.findTop10ByOrderByDateDesc();
        List<CategoryCount> categoryCounts = errorInfoService.findCategoryOrderByCount();
        model.addAttribute("recentInfos", recentInfos);
        model.addAttribute("categoryCounts", categoryCounts);
        return "home";
    }
}
