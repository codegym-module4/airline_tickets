package com.codegym.airline_tickets.controller.User;

import com.codegym.airline_tickets.entity.News;
import com.codegym.airline_tickets.service.impl.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/news")
public class NewsUserController {

    @Autowired
    private NewsService newsService;

    @GetMapping("")
    public String getAllNews(Model model) {
        model.addAttribute("newsList", newsService.getAll());
        return "user/news/list"; // Đường dẫn tới file Thymeleaf
    }


    @GetMapping("/detail/{id}")
    public String getNewsDetail(@PathVariable("id") Long id, Model model) {
        News news = newsService.findById(id);
        String[] paragraphs = news.getContent().split("\n");
        model.addAttribute("news", news);
        model.addAttribute("paragraphs", paragraphs);

        return "user/news/news_detail"; // Đường dẫn tới file Thymeleaf
    }
}
