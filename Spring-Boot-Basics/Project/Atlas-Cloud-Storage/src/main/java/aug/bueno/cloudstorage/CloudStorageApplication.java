package aug.bueno.cloudstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

import static aug.bueno.cloudstorage.controller.util.MessageWrapperUtil.FILE_NOT_FOUND_MSG;

@SpringBootApplication
public class CloudStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudStorageApplication.class, args);
    }

	/*
        // TODO Aks Udacity Knowledge Center about this case

        @Bean
        public ErrorPageRegistrar errorPageRegistrar(){
            return new CustomErrorPageRegistrar();
        }

        private static class CustomErrorPageRegistrar implements ErrorPageRegistrar {

            // Register your error pages and url paths.
            @Override
            public void registerErrorPages(ErrorPageRegistry registry) {
                registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/result?errorMessage=" + FILE_NOT_FOUND_MSG));
            }
        }
	*/

}
