package com.example.demo;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;
import reactor.blockhound.BlockHound;

@SpringBootApplication
@Slf4j
public class DemoApplication {

    public static void main(String[] args) {
        BlockHound.install();
        ConfigurableApplicationContext applicationContext = SpringApplication.run(DemoApplication.class, args);
        logApplicationStartup(applicationContext.getEnvironment());
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        String swaggerPath = env.getProperty("springdoc.swagger-ui.path");
        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\t\t{}://localhost:{}{}\n\t" +
                "External: \t\t{}://{}:{}{}\n\t" +
                "Profile(s): \t{}\n----------------------------------------------------------\n\t" +
                "Swagger doc: \t{}://{}:{}{}{}\n" +
                "----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles(),
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            swaggerPath == null ? "swagger-ui.html" : swaggerPath
        );
    }

}
