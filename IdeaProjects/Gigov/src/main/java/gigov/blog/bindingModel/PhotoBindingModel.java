package gigov.blog.bindingModel;

import org.springframework.web.multipart.MultipartFile;

public class PhotoBindingModel {
    private MultipartFile photo;

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
}
