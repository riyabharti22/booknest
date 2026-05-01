package com.booknest.booknest.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Configuration
public class DataStaxAstraConfig {

    @Value("${datastax.astra.secure-connect-bundle}")
    private Resource secureConnectBundle;

    @Value("${spring.cassandra.username}")
    private String username;

    @Value("${spring.cassandra.password}")
    private String password;

    @Value("${spring.cassandra.keyspace-name}")
    private String keyspace;

    @Bean
    public CqlSession cqlSession() throws Exception {
        Path tempFile = Files.createTempFile("secure-connect", ".zip");
        try (InputStream is = secureConnectBundle.getInputStream()) {
            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }
        return CqlSession.builder()
                .withCloudSecureConnectBundle(tempFile)
                .withAuthCredentials(username, password)
                .withKeyspace(keyspace)
                .build();
    }
}