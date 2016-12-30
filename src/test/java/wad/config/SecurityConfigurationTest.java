package wad.config;

import org.fluentlenium.adapter.FluentTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityConfigurationTest extends FluentTest {

    public WebDriver webDriver = new HtmlUnitDriver();

    @Override
    public WebDriver getDefaultDriver() {
        return webDriver;
    }

    @Before
    public void setup() {
        webDriver.manage().deleteAllCookies();
    }

    @LocalServerPort
    private Integer port;

    @Test
    public void anyoneCanSeeRegistrationpath() throws Throwable {
        goTo("http://localhost:" + port + "/register");
        assertThat(pageSource()).contains("Sign Up");
    }

    @Test
    public void anyoneCanSeeLoginpath() throws Throwable {
        goTo("http://localhost:" + port + "/login");
        assertThat(pageSource()).contains("Login");
    }
    
    @Test
    public void userCanSeeWordsPathAfterLogin() throws Throwable {
        goTo("http://localhost:" + port + "/words");
        assertThat(pageSource()).doesNotContain("Words List");
        enterDetailsAndSubmit("user", "user");
        assertThat(pageSource()).contains("Words List");
    }
    
    private void enterDetailsAndSubmit(String username, String password) {
        fill(find(By.name("username"))).with(username);
        fill(find(By.name("password"))).with(password);
        find(By.name("password")).submit();
    }
}
