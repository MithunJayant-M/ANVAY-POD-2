package com.cts.mfrp.anvay.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

/**
 * Probes the DataSource at startup so we know whether the JDBC connection
 * actually works before the first HTTP request arrives. Without this, a
 * connection problem only surfaces lazily as a 500 from the first request
 * that touches the DB — and the real cause is buried in the log.
 *
 * Render → Logs tab will show:
 *   ✅ DB connection OK: jdbc:mysql://... (MySQL 8.x)
 * …or a full stack trace pinpointing the failure (handshake, auth, SSL, etc.).
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSourceHealthCheck {

    @Bean
    public ApplicationRunner verifyDatabaseConnection(DataSource dataSource) {
        return args -> {
            try (Connection conn = dataSource.getConnection()) {
                DatabaseMetaData meta = conn.getMetaData();
                log.info("✅ DB connection OK: {} ({} {})",
                        meta.getURL(),
                        meta.getDatabaseProductName(),
                        meta.getDatabaseProductVersion());
                log.info("   driver: {} {}", meta.getDriverName(), meta.getDriverVersion());
                log.info("   user:   {}", meta.getUserName());
            } catch (Exception e) {
                // Don't swallow — re-throw so the app fails to start if DB is unreachable
                log.error("❌ DB connection FAILED at startup", e);
                throw e;
            }
        };
    }
}
