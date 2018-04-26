package gigov.blog.controller;


import gigov.blog.entity.Article;
import gigov.blog.entity.Category;
import gigov.blog.repository.ArticleRepository;
import gigov.blog.repository.CategoryRepository;
import gigov.blog.repository.PhotoRepository;
import gigov.blog.service.ArticleService;
import gigov.blog.service.PageWrapper;
import gigov.blog.viewModels.ListArticleViewModel;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@org.springframework.web.bind.annotation.RestController
public class RestController {


    private ArticleRepository articleRepository;
    private CategoryRepository categoryRepository;
    @Autowired
    private PhotoRepository photoRepository;

    private final ArticleService articleService;

    @Autowired
    public RestController(ArticleRepository articleRepository, CategoryRepository categoryRepository, ArticleService articleService){
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.articleService = articleService;
    }

    @GetMapping("/test")
    public List<Article> test(){
        List<Article> articles = this.articleRepository.findAll();
        return articles;
    }

    @GetMapping("/getAllArticles")
    public List<Article> getAllArticles(){
        List<Article> articles = this.articleRepository.findAllByOrderByIdAsc();
        return articles;
    }

    /*@PostMapping("/search/")
    public String search (Model model, @RequestParam(value = "searchText[]") Integer[] searchText){
        //pagination
      *//*  PageRequest pageRequest = new PageRequest(pageId-1,2, Sort.Direction.ASC, "id");
        PageWrapper<Article> page = new PageWrapper<Article>(articleService.getArticles(pageRequest,searchText),"/search/");
        List<Article> articles = page.getContent();*//*

        *//*List<Article> articles = new List<Article>();
        for (Integer id : searchText) {
            articles.add(articleRepository.findAllById(id));
        }*//*
        List<Article> articles = articleRepository.findAllById(searchText[0]);

        model.addAttribute("articles",articles);
        *//*model.addAttribute("page",page);*//*
        model.addAttribute("view" , "home/list-articles");

        return  "base-layout";
    }*/

    /*@GetMapping( value = "/search/{searchResult}")
    public String search(Model model, @RequestParam(value = "page.page",defaultValue = "1") Integer pageId, @PathVariable("searchResult") Integer[] articlesId, Pageable pageable){

        //pagination
        PageRequest pageRequest = new PageRequest(pageId-1,2, Sort.Direction.ASC, "id");
        PageWrapper<Article> page = new PageWrapper<Article>(articleService.getArticles(pageRequest,articlesId[0]),"/");
        List<Article> articles = page.getContent();

        model.addAttribute("articles",articles);
        model.addAttribute("page",page);
        //pagination

        model.addAttribute("view" , "home/list-articles");

        return  "base-layout";
    }*/

    /*@GetMapping( value = "/page/{categoryId}")
    public Page<Article> blogFilter(Model model, @PathVariable("categoryId") Integer categoryId, Pageable pageable) throws IOException {
        //List<Article> article = this.articleRepository.findAll();
        //return article;
        Category category = this.categoryRepository.findOne(categoryId);

        List<ListArticleViewModel> articles = new ArrayList<>();

        Page<Article> articleEntities = this.articleRepository.findAllByCategory_IdOrderByIdAsc(categoryId, pageable);

        for(Article article : articleEntities) {
            ListArticleViewModel viewModel = new ListArticleViewModel(
                    article.getId(),
                    article.getTitle(),
                    article.getSummary(),
                    article.getAuthor().getFullName(),
                    this.photoRepository.findFirstByArticle(article).getData(),
                    article.getTags()
            );
            articles.add(viewModel);
        }
        model.addAttribute("articles",articles);
        model.addAttribute("view" , "home/list-articles");
        model.addAttribute("category" , category);

        return articleEntities;
    }*/
}
