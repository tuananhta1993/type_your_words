package wad.controller;

import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.fluentlenium.adapter.FluentTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WordControllerTest extends FluentTest {

    public WebDriver webDriver = new HtmlUnitDriver();

    @Override
    public WebDriver getDefaultDriver() {
        return webDriver;
    }

    @LocalServerPort
    private Integer port;

    @Test
    public void canListWordsAfterLogin() throws Throwable {
        goTo("http://localhost:" + port + "/login");

        enterDetailsAndSubmit("user", "user");

        goTo("http://localhost:" + port + "/words");

        assertThat(pageSource()).contains("Words List");
        // Logout after the test
        goTo("http://localhost:" + port + "/logout");
    }

    @Test
    public void canNotListWordsWithoutLoggingIn() throws Throwable {
        goTo("http://localhost:" + port + "/words");

        assertThat(pageSource()).doesNotContain("Words List");
    }
    
    @Test
    public void canLoadAddWordFormForAuthenticatedUser() throws Throwable {
        goTo("http://localhost:" + port + "/login");

        enterDetailsAndSubmit("user", "user");

        goTo("http://localhost:" + port + "/words/createsingle");

        assertThat(pageSource()).contains("Create Single Word");
        // Logout after the test
        goTo("http://localhost:" + port + "/logout");
    }
    
    @Test
    public void returnErrorIfTheWordDetailIsEmpty() throws Throwable {
        String content = "";
        String Pronounce = "";
         
        goTo("http://localhost:" + port + "/login");

        enterDetailsAndSubmit("user", "user");

        goTo("http://localhost:" + port + "/words/createsingle");
        fill(find(By.name("Content"))).with(content);
        fill(find(By.name("Pronounce"))).with(Pronounce);
        find(By.name("Pronounce")).submit();
        
        assertThat(pageSource()).contains("may not be empty");
        // Logout after the test
        goTo("http://localhost:" + port + "/logout");
    }
    
    @Test
    public void returnErrorIfTheWordDetailIsTooLong() throws Throwable {
        String content = "Affronting imprudence do he he everything. Sex lasted dinner wanted indeed wished out law. Far advanced settling say finished raillery. Offered chiefly farther of my no colonel shyness. Such on help ye some door if in. Laughter proposal laughing any son law consider. Needed except up piqued an.";
        String Pronounce = UUID.randomUUID().toString().substring(0, 10);
         
        goTo("http://localhost:" + port + "/login");

        enterDetailsAndSubmit("user", "user");

        goTo("http://localhost:" + port + "/words/createsingle");
        fill(find(By.name("Content"))).with(content);
        fill(find(By.name("Pronounce"))).with(Pronounce);
        find(By.name("Pronounce")).submit();
        
        assertThat(pageSource()).contains("length must be between 1 and 100");
        
        // Logout after the test
        goTo("http://localhost:" + port + "/logout");
    }
    
    private void enterDetailsAndSubmit(String username, String password) {
        fill(find(By.name("username"))).with(username);
        fill(find(By.name("password"))).with(password);
        find(By.name("password")).submit();
    }
}