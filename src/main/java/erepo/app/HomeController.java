package erepo.app;

import erepo.domain.model.CategoryCount;
import erepo.domain.model.ErrorInfo;
import erepo.domain.service.ErrorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ErrorInfoService errorInfoService;

    @GetMapping("/")
    public String home(Model model) {
        List<ErrorInfo> recentInfos = errorInfoService.findTop10ByOrderByDateDesc();
        List<CategoryCount> categoryCounts = errorInfoService.findCategoryOrderByCount();

        List<CategoryCount> categoryCountsWithOthers = new ArrayList<>();
        final int allSum = categoryCounts.stream().mapToInt(c -> c.count).sum();
        int othersSum = 0;
        for (CategoryCount categoryCount : categoryCounts) {
            if (categoryCount.count * 100 >= allSum) {
                categoryCountsWithOthers.add(categoryCount);
            } else {
                othersSum += categoryCount.count;
            }
        }
        if (othersSum > 0) {
            categoryCountsWithOthers.add(new CategoryCount("others", othersSum));
        }

        model.addAttribute("recentInfos", recentInfos);
        model.addAttribute("categoryCounts", categoryCountsWithOthers);
        return "home";
    }
}
