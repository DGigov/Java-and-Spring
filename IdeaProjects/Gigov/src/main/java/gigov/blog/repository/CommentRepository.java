package gigov.blog.repository;

import gigov.blog.entity.Article;
import gigov.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByArticleOrderByIdDesc(Article article);
}
