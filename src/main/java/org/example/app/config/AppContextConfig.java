package org.example.app.config;


import org.example.app.services.FileStorage;
import org.example.app.services.FileStorageImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@ComponentScan(basePackages = "org.example.app")
public class AppContextConfig {

}
