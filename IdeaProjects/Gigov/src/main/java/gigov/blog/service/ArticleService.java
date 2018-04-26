package gigov.blog.service;

import gigov.blog.bindingModel.ArticleBindingModel;
import gigov.blog.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ArticleService {
    //method declaration//
    void editProcess(Integer id, ArticleBindingModel articleBindingModel);
    Page<Article> getPendingArticles(Pageable pageable,Integer catId);
    Page<Article> getArticles(Pageable pageable);
}
