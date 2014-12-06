package configuration.thymeleaf.templating;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfig extends WebMvcAutoConfigurationAdapter {
	
    @Override
	public void addInterceptors(InterceptorRegistry registry) {
    	super.addInterceptors(registry);
        registry.addInterceptor(new ThymeleafLayoutInterceptor());
    }
}