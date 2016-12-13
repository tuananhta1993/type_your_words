package wad.config;

import java.net.URISyntaxException;
import org.springframework.context.annotation.Configuration;
import org.apache.commons.dbcp.BasicDataSource;

@Configuration
public class MainConfiguration {
    public BasicDataSource dataSource() throws URISyntaxException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        String username = System.getenv("JDBC_DATABASE_USERNAME");
        String password = System.getenv("JDBC_DATABASE_PASSWORD");

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);

        return basicDataSource;
    }
}
