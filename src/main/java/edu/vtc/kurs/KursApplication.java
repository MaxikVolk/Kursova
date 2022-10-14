package edu.vtc.kurs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.function.Function;

/**
 * The type Kurs application.
 */
@SpringBootApplication
public class KursApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(KursApplication.class, args);
    }

    /**
     * Current url without param function.
     *
     * @return the function
     */
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

    /**
     * Current url function.
     *
     * @return the function
     */
    @Bean
    public Function<String, String> currentUrl() {
        return param -> ServletUriComponentsBuilder.fromCurrentRequest().replaceQueryParam(param).toUriString();
    }
}
