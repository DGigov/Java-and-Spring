package gigov.blog.controller;

import gigov.blog.bindingModel.ArticleBindingModel;
import gigov.blog.entity.*;
import gigov.blog.repository.*;
import gigov.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private CommentRepository commentRepository;

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model) {
        List<Category> categories = this.categoryRepository.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("view", "article/create");
        return "base-layout";
    }

    @PostMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(ArticleBindingModel articleBindingModel) throws IOException {

        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userEntity = this.userRepository.findByEmail(user.getUsername());
        Category category = this.categoryRepository.findOne(articleBindingModel.getCategoryId());
        HashSet<Tag> tags = this.findTagsFromString(articleBindingModel.getTagString());

        Article articleEntity = new Article(
                articleBindingModel.getTitle(),
                articleBindingModel.getContent(),
                userEntity,
                category,
                tags,
                articleBindingModel.getDate(),
                articleBindingModel.getPrice()
        );

        Article article = this.articleRepository.saveAndFlush(articleEntity);

        for (MultipartFile pictureFile : articleBindingModel.getPhotos()) {

            String photoData = Base64.getEncoder().encodeToString(pictureFile.getBytes());

            Photo photo = new Photo(photoData, article);
            Photo photoEntity = this.photoRepository.saveAndFlush(photo);
            article.addPhoto(photoEntity);
        }
        this.articleRepository.saveAndFlush(articleEntity);
        return "redirect:/";
    }

    @GetMapping("/article/{id}")
    public String details(Model model, @PathVariable Integer id) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }

        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User entityUser = this.userRepository.findByEmail(principal.getUsername());
            model.addAttribute("user", entityUser);
        }
        Article article = this.articleRepository.findOne(id);
        Photo photo = this.photoRepository.findOne(id);
        List<Comment> comments = this.commentRepository.findAllByArticleOrderByIdDesc(article);


        model.addAttribute("comments", comments);
        model.addAttribute("photo", photo);
        model.addAttribute("article", article);
        model.addAttribute("view", "article/details");

        return "base-layout";
    }

    @GetMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable Integer id, Model model) {

        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }
        Article article = this.articleRepository.findOne(id);

        if (!isUserAuthorOrAdmin(article)) {
            return "redirect:/article/" + id;
        }

        List<Category> categories = this.categoryRepository.findAll();

        String tagString = article.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.joining(","));

        model.addAttribute("tags", tagString);
        model.addAttribute("categories", categories);
        model.addAttribute("article", article);
        model.addAttribute("view", "article/edit");

        return "base-layout";
    }

    @PostMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable Integer id, ArticleBindingModel articleBindingModel) throws IOException {
        Article article = this.articleRepository.findOne(id);
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }
        if (!isUserAuthorOrAdmin(article)) {
            return "redirect:/article/" + id;
        }
        this.articleService.editProcess(id,articleBindingModel);
        return "redirect:/article/" + id;
    }

    @GetMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable Integer id) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }
        Article article = this.articleRepository.findOne(id);
        if (!isUserAuthorOrAdmin(article)) {
            return "redirect:/article/" + id;
        }
        model.addAttribute("article", article);
        model.addAttribute("view", "article/delete");

        return "base-layout";
    }

    private boolean isUserAuthorOrAdmin(Article article) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userEntity = this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin() || userEntity.isAuthor(article);
    }

    @PostMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable Integer id) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }
        this.articleRepository.delete(id);

        return "redirect:/";
    }

    private HashSet<Tag> findTagsFromString(String tagString) {
        HashSet<Tag> tags = new HashSet<>();
        String[] tagNames = tagString.split(",\\s*");

        for (String tagName : tagNames) {
            Tag currentTag = this.tagRepository.findByName(tagName);

            if (currentTag == null) {
                currentTag = new Tag(tagName);
                this.tagRepository.saveAndFlush(currentTag);
            }
            tags.add(currentTag);
        }
        return tags;
    }

    @GetMapping("/article/managePic/{id}")
    @PreAuthorize("isAuthenticated()")
    public String managePic(@PathVariable Integer id, Model model) throws IOException {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }
        Article article = this.articleRepository.findOne(id);

        if (!isUserAuthorOrAdmin(article)) {
            return "redirect:/article/" + id;
        }

        model.addAttribute("article", article);
        model.addAttribute("view", "article/managePic");

        return "base-layout";
    }

    @PostMapping("/article/{articleId}/photo/delete/{photoId}")
    @PreAuthorize("isAuthenticated()")
    public String managePicProcess(@PathVariable("articleId") Integer articleId,
                                   @PathVariable("photoId") Integer photoId) throws IOException {

        if (!this.articleRepository.exists(articleId)) {
            return "redirect:/";
        }

        Article article = this.articleRepository.findOne(articleId);

        //Picture to be deleted
        Photo photo = this.photoRepository.findOne(photoId);

        article.getPhotos().remove(photo);

        this.photoRepository.delete(photoId);

        return "redirect:/article/edit/" + articleId;
    }
    @PostMapping("/article/addPic/{articleId}")
    @PreAuthorize("isAuthenticated()")
    public  String addPic(@PathVariable("articleId") Integer articleId, ArticleBindingModel articleBindingModel) throws IOException {

        Article article = this.articleRepository.findOne(articleId);
        for (MultipartFile pictureFile : articleBindingModel.getPhotos()) {

            String photoData = Base64.getEncoder().encodeToString(pictureFile.getBytes());

            Photo photo = new Photo(photoData, article);
            Photo photoEntity = this.photoRepository.saveAndFlush(photo);
            article.addPhoto(photoEntity);
        }

        return "redirect:/article/edit/" + articleId;
    }

    @PostMapping("/article/addComment/{articleId}")
    public  String addComment(@PathVariable("articleId") Integer articleId, ArticleBindingModel articleBindingModel) throws IOException {

        Article article = this.articleRepository.findOne(articleId);

        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userEntity = this.userRepository.findByEmail(user.getUsername());


        String text = articleBindingModel.getCommentsText();
        Comment comment = new Comment(text, article, userEntity, articleBindingModel.getDate());
        Comment commentEntity = this.commentRepository.saveAndFlush(comment);
        article.addComment(commentEntity);

        return "redirect:/article/" + articleId;
    }

}
