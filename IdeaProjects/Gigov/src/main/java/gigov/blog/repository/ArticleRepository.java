package gigov.blog.repository;

import gigov.blog.entity.Article;
import gigov.blog.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer>{
    List<Article> findByCategoryOrderByIdAsc(Category category);
    List<Article> findAllByContentContains(String text);
    List<Article> findAll();
    Page<Article> findAllById(Integer[] resultId, Pageable pageable);
    Page<Article> findAllByCategory_IdOrderByIdAsc(Integer categoryId, Pageable pageable);
    List<Article> findAllByOrderByIdAsc();
}
