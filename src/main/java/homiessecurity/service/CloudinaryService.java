package homiessecurity.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

    public String uploadImage(MultipartFile image, String folder);
}
