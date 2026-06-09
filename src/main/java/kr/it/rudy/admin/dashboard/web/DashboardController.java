package kr.it.rudy.admin.dashboard.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class DashboardController {

    @GetMapping
    public String dashboard() {
        return "dashboard/index";
    }
}
