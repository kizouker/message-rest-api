package com.storytel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

@SpringBootApplication
public class App {


    public static void main(String[] args) {

        SpringApplication.run(App.class, args);
    }


    @Bean (name = "applicationScopedBean")
    @Scope(value = WebApplicationContext.SCOPE_APPLICATION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Messages getMessagesScopeApplication() {

        return new Messages();
    }


}
