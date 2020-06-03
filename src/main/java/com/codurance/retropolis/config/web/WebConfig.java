package com.codurance.retropolis.config.web;

import static java.util.Collections.singletonList;

import com.codurance.retropolis.config.Environment;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@Profile(Environment.PROD)
public class WebConfig {

  @Configuration
  public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(new GoogleTokenAuthenticator());
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> simpleCorsFilter() {
      return CorsFilterConfiguration.simpleCorsFilter(singletonList("http://retropolis-fe.s3-website.eu-west-2.amazonaws.com"));
    }

  }
}