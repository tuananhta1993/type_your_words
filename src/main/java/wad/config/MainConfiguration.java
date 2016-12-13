//package wad.config;
//
//import java.net.URISyntaxException;
//import org.springframework.context.annotation.Configuration;
//import org.apache.commons.dbcp.BasicDataSource;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//
//@Configuration
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
//public class MainConfiguration {
//    public BasicDataSource dataSource() throws URISyntaxException {
//        String dbUrl = System.getenv("JDBC_DATABASE_URL");
//        String username = System.getenv("JDBC_DATABASE_USERNAME");
//        String password = System.getenv("JDBC_DATABASE_PASSWORD");
//
//        BasicDataSource basicDataSource = new BasicDataSource();
//        basicDataSource.setUrl(dbUrl);
//        basicDataSource.setUsername(username);
//        basicDataSource.setPassword(password);
//
//        return basicDataSource;
//    }
//}
