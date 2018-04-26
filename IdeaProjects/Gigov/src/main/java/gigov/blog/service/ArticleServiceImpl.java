package gigov.blog.service;

import gigov.blog.bindingModel.ArticleBindingModel;
import gigov.blog.controller.ArticleController;
import gigov.blog.entity.Article;
import gigov.blog.entity.Category;
import gigov.blog.entity.Tag;
import gigov.blog.repository.ArticleRepository;
import gigov.blog.repository.CategoryRepository;
import gigov.blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;


    @Override
    public void editProcess(Integer id, ArticleBindingModel articleBindingModel) {

        Article article = this.articleRepository.findOne(id);


        Category category = this.categoryRepository.findOne(articleBindingModel.getCategoryId());

        HashSet<Tag> tags = this.findTagsFromString(articleBindingModel.getTagString());

        article.setTags(tags);
        article.setCategory(category);
        article.setContent(articleBindingModel.getContent());
        article.setTitle(articleBindingModel.getTitle());

        this.articleRepository.saveAndFlush(article);
    }

    @Override
    public Page<Article> getPendingArticles(Pageable pageable, Integer catId) {
        Page<Article> articleList = articleRepository.findAllByCategory_IdOrderByIdAsc(catId, pageable);
        return articleList;
    }

    @Override
    public Page<Article> getArticles(Pageable pageable) {

        Page<Article> searchArticleList = articleRepository.findAll(pageable);

        /*Page<Article> searchArticleList = new Page<Article>();

        for (Integer id : artId) {
            searchArticleList = articleRepository.findAllById(id, pageable);
        }*/

        return searchArticleList;
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

}
