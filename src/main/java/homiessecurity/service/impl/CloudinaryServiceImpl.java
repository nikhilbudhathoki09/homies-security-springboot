package homiessecurity.service.impl;

import com.cloudinary.Cloudinary;
import homiessecurity.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;



    @Override
    public String uploadImage(MultipartFile image, String folder) {
        Map data = null;
        try {
            data = cloudinary.uploader().upload(image.getBytes(), Map.of("folder", folder));
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed");
        }

        String url = (String) data.get("url");
        return url;
    }
}
