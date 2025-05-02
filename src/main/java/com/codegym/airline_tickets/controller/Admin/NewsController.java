package com.codegym.airline_tickets.controller.Admin;

import com.codegym.airline_tickets.entity.News;
import com.codegym.airline_tickets.service.impl.FirebaseStorageService;
import com.codegym.airline_tickets.service.impl.NewsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;

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
    public String createNews(@Valid @ModelAttribute("news") News news,
                             BindingResult bindingResult,
                             @RequestParam("imageFile") MultipartFile file,
                             RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            bindingResult.rejectValue("imageURL", "file.empty", "Vui lòng chọn ảnh cho tin tức.");
        }
        // Nếu có lỗi, quay lại form
        if (bindingResult.hasErrors()) {
            return "admin/news/create";
        }
        try {
            // Upload ảnh và lưu tin tức
            String imageUrl = firebaseStorageService.uploadFile(file, news.getTitle());
            news.setCreatedAt(LocalDateTime.now());
            news.setImageURL(imageUrl);
            newsService.save(news);
            redirectAttributes.addFlashAttribute("message", "Thêm tin tức thành công!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Lỗi khi tải ảnh: " + e.getMessage());
        }

        return "redirect:/admin/news";
    }

    @GetMapping("/delete/{id}")
    public String deleteNews(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        News news = newsService.findById(id);
        if (news != null) {
//            xóa ảnh từ Firebase Storage
            firebaseStorageService.deleteFileFromUrl(news.getImageURL());
            newsService.remove(id);
        }
        redirectAttributes.addFlashAttribute("message", "Xóa tin tức thành công!");
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
                             @Valid @ModelAttribute("news") News news,
                             BindingResult bindingResult,
                             @RequestParam("imageFile") MultipartFile file,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "admin/news/update";
        }

        try {
            if (!file.isEmpty()) {
                // Nếu có ảnh mới, upload và thay thế
                String imageUrl = firebaseStorageService.uploadFile(file, news.getTitle());
                news.setImageURL(imageUrl);
            } else {
                // giữ ảnh cũ
                News existing = newsService.findById(id);
                news.setImageURL(existing.getImageURL());
            }
            news.setId(id);
            newsService.save(news);
            redirectAttributes.addFlashAttribute("message", "Cập nhật tin tức thành công!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Lỗi khi xử lý ảnh: " + e.getMessage());
        }

        return "redirect:/admin/news";
    }
}

