package erepo.app;

import erepo.domain.model.CategoryCount;
import erepo.domain.model.DateCount;
import erepo.domain.model.ErrorInfo;
import erepo.domain.service.ErrorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ErrorInfoService errorInfoService;

    @GetMapping("/")
    public String home(Model model) {
        List<ErrorInfo> recentInfos = errorInfoService.findTop6ByOrderByDateDesc();
        List<CategoryCount> categoryCounts = errorInfoService.findCategoryOrderByCount();

        List<CategoryCount> categoryCountsWithOthers = new ArrayList<>();
        final int allSum = categoryCounts.stream().mapToInt(c -> c.count).sum();
        int othersSum = 0;
        for (int i = 0; i < categoryCounts.size(); i++) {
            CategoryCount categoryCount = categoryCounts.get(i);
            if (i < 5) {
                categoryCount.percent = String.format("%.1f", 100.0 * categoryCount.count / allSum);
                categoryCountsWithOthers.add(categoryCount);
            } else {
                othersSum += categoryCount.count;
            }
        }
        if (othersSum > 0) {
            categoryCountsWithOthers.add(new CategoryCount("others", othersSum, String.format("%.1f", 100.0 * othersSum / allSum)));
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
    public String result(@RequestParam(name = "url") String url, @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        List<DateCount> dateCountsTmp = errorInfoService.findDateCountByUrlContainsAndDuring25days(url);
        List<DateCount> dateCounts = new ArrayList<>();
        int dateCountsIndex = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -24);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        for (int i = 0; i < 25; i++) {
            String dateStr = sdf.format(calendar.getTime());
            int month = Integer.parseInt(dateStr.substring(0, 2));
            int date = Integer.parseInt(dateStr.substring(3, 5));
            if (dateCountsIndex < dateCountsTmp.size() && month == dateCountsTmp.get(dateCountsIndex).month
                    && date == dateCountsTmp.get(dateCountsIndex).date) {
                dateCounts.add(dateCountsTmp.get(dateCountsIndex));
                dateCountsIndex++;
            } else {
                dateCounts.add(new DateCount(month, date, 0));
            }
            calendar.add(Calendar.DATE, 1);
        }

        List<ErrorInfo> list;
        list = errorInfoService.findByUrlContainsOrderByDateDesc(url, new PageRequest(page - 1, 10));
        for (ErrorInfo info : list) {
            urlFilter(info);
        }
        int sum = errorInfoService.countByUrlContains(url);
        int maxPage = (sum + 10 - 1) / 10;

        if (sum > 0 && maxPage < page) {
            return "error";
        }

        model.addAttribute("url", url);
        model.addAttribute("dateCounts", dateCounts);
        model.addAttribute("resultInfos", list);
        model.addAttribute("page", page);
        model.addAttribute("maxPage", maxPage);
        model.addAttribute("min", (page - 1) * 10 + 1);
        model.addAttribute("max", page == maxPage ? sum : page * 10);
        model.addAttribute("sum", sum);
        return "result";
    }

    @GetMapping("/crawl/{shortName}")
    public String crawl(@PathVariable(name = "shortName") String shortName, @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        if (shortName.equals("cs")) {
            model.addAttribute("description", "Result of Computer Science International Conference");
        } else if (shortName.equals("com")) {
            model.addAttribute("description", "Result of Listed Enterprise in Japan");
        } else if (shortName.equals("alexa")) {
            model.addAttribute("description", "Result of Alexa Top 500");
        } else {
            return "error";
        }

        List<ErrorInfo> list;
        list = errorInfoService.findByRemarksOrderByDateDesc(shortName, new PageRequest(page - 1, 10));
        for (ErrorInfo info : list) {
            urlFilter(info);
        }
        int sum = errorInfoService.countByRemarks(shortName);
        int maxPage = (sum + 10 - 1) / 10;

        if (sum > 0 && maxPage < page) {
            return "redirect:/crawl/" + shortName;
        }

        model.addAttribute("shortName", shortName);
        model.addAttribute("resultInfos", list);
        model.addAttribute("page", page);
        model.addAttribute("maxPage", maxPage);
        model.addAttribute("min", (page - 1) * 10 + 1);
        model.addAttribute("max", page == maxPage ? sum : page * 10);
        model.addAttribute("sum", sum);
        return "crawl";
    }

    @GetMapping("/plugin")
    public String plugin(Model model) {
        return "plugin_en";
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
