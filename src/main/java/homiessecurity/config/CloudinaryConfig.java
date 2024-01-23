package homiessecurity.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

@Bean
public Cloudinary getCloudinary(){
    //Details of the cloudinary folder and account to be used
    Map<String, String> config = Map.of(
            "cloud_name", "daatewbgi",
            "api_key", "193882615798242",
            "api_secret", "49GHURrL6k1Lb2cpTl_O-ha0-k4"
    );

    return new Cloudinary(config);
}

}
