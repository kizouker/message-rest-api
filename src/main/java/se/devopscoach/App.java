package se.devopscoach;

import se.devopscoach.model.Messages;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;


@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

        System.out.println("Running tests!");
        JUnitCore engine = new JUnitCore();
        engine.addListener(new TextListener(System.out));
        engine.run(MyTests.class);
    }
    // The context is through the whole application
    @Bean (name = "applicationScopedBean")
    @Scope(value = WebApplicationContext.SCOPE_APPLICATION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Messages getMessagesScopeApplication() {

     return new Messages();
    }
}
