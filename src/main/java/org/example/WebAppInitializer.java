package org.example;

import org.example.app.config.AppContextConfig;
import org.example.web.config.WebContextConfig;
import org.h2.server.web.WebServlet;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import org.springframework.web.servlet.DispatcherServlet;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.boot.CommandLineRunner;

import org.example.app.services.FileStorage;


public class WebAppInitializer implements WebApplicationInitializer, CommandLineRunner {

    @Resource
    FileStorage fileStorage;


    @Override
    public void onStartup(javax.servlet.ServletContext servletContext) throws ServletException {


        AnnotationConfigWebApplicationContext appContext =
                new AnnotationConfigWebApplicationContext();
        appContext.register(AppContextConfig.class);

        servletContext.addListener(new ContextLoaderListener(appContext));





        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
        webContext.register(WebContextConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(webContext);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");


       ServletRegistration.Dynamic servlet = servletContext.addServlet("h2-console", new WebServlet());
        servlet.setLoadOnStartup(2);
        servlet.addMapping("/console/*");



    }

    @Override
    public void run(String... args) throws Exception {
        fileStorage.deleteAll();
        fileStorage.init();
    }

}
