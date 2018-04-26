package gigov.blog.viewModels;

import gigov.blog.entity.Tag;

import java.util.Set;

public class ListArticleViewModel {
    private Integer id;
    private String title;
    private String summary;
    private String author;
    private String photo;
    private Set<Tag> tags;

    public ListArticleViewModel(Integer id, String title, String summary, String author, String photo, Set<Tag> tags) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.photo = photo;
        this.tags = tags;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
}
