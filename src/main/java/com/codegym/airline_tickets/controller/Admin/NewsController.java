package com.codegym.airline_tickets.controller.Admin;

import com.codegym.airline_tickets.entity.News;
import com.codegym.airline_tickets.service.impl.FirebaseStorageService;
import com.codegym.airline_tickets.service.impl.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @GetMapping
    public String listNews(Model model) {
        model.addAttribute("newsList", newsService.getAll());
        return "admin/news/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("news", new News());
        return "admin/news/create";
    }

    @PostMapping("/create")
    public String createNews(@ModelAttribute News news,
                             @RequestParam("imageFile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String imageUrl = firebaseStorageService.uploadFile(file, news.getTitle());
            news.setImageURL(imageUrl);
        }
        newsService.save(news);
        return "redirect:/admin/news";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        News news = newsService.findById(id);
        model.addAttribute("news", news);
        return "admin/news/update";
    }

    @PostMapping("/edit/{id}")
    public String updateNews(@PathVariable Long id,
                             @ModelAttribute News news,
                             @RequestParam("imageFile") MultipartFile file) throws IOException {
        News existing = newsService.findById(id);

        if (!file.isEmpty()) {
            firebaseStorageService.deleteFileFromUrl(existing.getImageURL());
            String imageUrl = firebaseStorageService.uploadFile(file, "news");
            news.setImageURL(imageUrl);
        } else {
            news.setImageURL(existing.getImageURL());
        }
        news.setId(id);
        newsService.save(news);
        return "redirect:/admin/news";
    }
}

