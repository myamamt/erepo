package erepo.app;

import erepo.domain.model.CategoryCount;
import erepo.domain.model.ErrorInfo;
import erepo.domain.service.ErrorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        ErrorInfo info = errorInfoService.findOne(id);
        urlFilter(info);
        model.addAttribute("info", info);
        return "detail";
    }

    @GetMapping("/result")
    public String result(@RequestParam("url") String url, Model model) {
        model.addAttribute("url", url);
        List<ErrorInfo> list = errorInfoService.findByUrlContains(url);
        for (ErrorInfo info : list) {
            urlFilter(info);
        }
        model.addAttribute("resultInfos", errorInfoService.findByUrlContains(url));
        return "result";
    }

    private void urlFilter(ErrorInfo info) {
        info.setUrl(hideQuery(info.getUrl()));
        info.setStackTrace(hideQuery(info.getStackTrace()));
        info.setFileName(hideQuery(info.getFileName()));
    }

    private static String hideQuery(String str) {
        if (str == null) return null;
        return str.replaceAll("(https?:.*?\\?).+", "$1[query]");
    }
}
