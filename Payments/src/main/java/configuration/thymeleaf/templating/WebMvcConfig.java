package configuration.thymeleaf.templating;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.resource.GzipResourceResolver;

@Configuration
public class WebMvcConfig extends WebMvcAutoConfigurationAdapter {
	
    @Override
	public void addInterceptors(InterceptorRegistry registry) {
    	super.addInterceptors(registry);
        registry.addInterceptor(new ThymeleafLayoutInterceptor());
    }
    
    @Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    	
    	registry
    		.addResourceHandler("/resources/**")
    		.addResourceLocations(
                "classpath:/static/resources/")
    		.setCachePeriod(60*60*3)
    		.resourceChain(true)
    		.addResolver(new GzipResourceResolver());    	
	}
}