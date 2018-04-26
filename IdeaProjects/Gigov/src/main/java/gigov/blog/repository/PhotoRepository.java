package gigov.blog.repository;

import gigov.blog.entity.Article;
import gigov.blog.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    List<Photo> findAllByArticleOrderByIdAsc(Article article);
    Photo findFirstByArticle(Article article);

}
