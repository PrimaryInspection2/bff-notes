package com.saveit.bff.notes.feign.configuration;

import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotesServiceFeignClientConfiguration {

    //todo make it configurable
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new NotesServiceErrorDecoder();
    }
}
