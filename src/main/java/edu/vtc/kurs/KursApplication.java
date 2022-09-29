package edu.vtc.kurs;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.function.Function;

@SpringBootApplication
public class KursApplication {

    public static void main(String[] args) {
        SpringApplication.run(KursApplication.class, args);
    }

    @Bean
    public Function<String, String> currentUrlWithoutParam() {
        return param -> {
            if (ServletUriComponentsBuilder.fromCurrentRequest().replaceQueryParam(param).toUriString().contains("/sorted")) {
                return ServletUriComponentsBuilder.fromCurrentRequest().replaceQueryParam(param).toUriString();
            } else {
                return ServletUriComponentsBuilder.fromCurrentRequest().replaceQueryParam(param).toUriString() + "/sorted";
            }
        };
    }

    @Bean
    public Function<String, String> currentUrl() {
        return param -> ServletUriComponentsBuilder.fromCurrentRequest().replaceQueryParam(param).toUriString();
    }
}
