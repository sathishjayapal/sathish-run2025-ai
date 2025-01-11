package me.sathish.sathishrun2025ai;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class SathishRun2025AiApplication {
    private static final Logger logger = LoggerFactory.getLogger(SathishRun2025AiApplication.class);
    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext=  SpringApplication.run(SathishRun2025AiApplication.class, args);
        String[] beanDev=configurableApplicationContext.getBeanDefinitionNames();
        Arrays.stream(beanDev).forEach(logger::info);
    }


}











