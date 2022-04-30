package com.codewithroy.resttester.expense.config;

import com.codewithroy.resttester.expense.constants.ExpenseConstants;
import com.codewithroy.resttester.expense.util.ConfigUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ConfigUtility configUtil;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins(configUtil.getProperty(ExpenseConstants.FRONT_DOMAIN_URL));
    }
}