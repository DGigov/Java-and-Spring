package gigov.blog.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import gigov.blog.bindingModel.ArticleBindingModel;
import gigov.blog.entity.Article;
import gigov.blog.entity.Category;
import gigov.blog.entity.User;
import gigov.blog.repository.ArticleRepository;
import gigov.blog.repository.CategoryRepository;
import gigov.blog.repository.PhotoRepository;
import gigov.blog.service.ArticleService;
import gigov.blog.service.PageWrapper;
import gigov.blog.viewModels.ListArticleViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private ArticleRepository articleRepository;

    private final ArticleService articleService;

    @Autowired
    public HomeController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String index(Model model, @RequestParam(value = "page.page",defaultValue = "1") Integer pageId, Pageable pageable){

        List<Category> categories = this.categoryRepository.findAll();

        //pagination
        PageRequest pageRequest = new PageRequest(pageId-1,4, Sort.Direction.ASC, "id");
        PageWrapper<Article> page = new PageWrapper<Article>(articleService.getArticles(pageRequest),"/");
        List<Article> articles = page.getContent();
        model.addAttribute("articles",articles);
        model.addAttribute("page",page);
        //pagination

        model.addAttribute("view", "home/index");
        model.addAttribute("categories", categories);
        return "base-layout";
    }

    @GetMapping("/error/403")
    public String accessDenied(Model model){
        model.addAttribute("view", "error/403");

        return "base-layout";
    }

    @GetMapping("/category/{catId}")
    public String listArticles(Model model, @RequestParam(value = "page.page",defaultValue = "1") Integer pageId, @PathVariable Integer catId, Pageable pageable){

        if (!this.categoryRepository.exists(catId)) {

            return "redirect:/";
        }
        Category category = this.categoryRepository.findOne(catId);

        //pagination
        PageRequest pageRequest = new PageRequest(pageId-1,2, Sort.Direction.ASC, "id");
        PageWrapper<Article> page = new PageWrapper<Article>(articleService.getPendingArticles(pageRequest,catId),"/category/" + catId);
        List<Article> articles = page.getContent();

        model.addAttribute("articles",articles);
        model.addAttribute("page",page);
        //pagination

        model.addAttribute("view" , "home/list-articles");
        model.addAttribute("category" , category);

        return  "base-layout";
    }

}
