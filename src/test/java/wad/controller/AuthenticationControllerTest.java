package wad.controller;

import java.util.UUID;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.web.context.WebApplicationContext;
import wad.domain.Account;
import wad.repository.AccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private AccountRepository accountRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    // Configuration test
    @Test
    public void formReturnedOnGetToRegistrations() throws Exception {
        MvcResult res = mockMvc.perform(get("/register"))
                .andExpect(status().isOk()).andReturn();

        assertEquals("Verify that a GET-request to /register returns a view created from the register.html-page.",
                "register", res.getModelAndView().getViewName());
    }

    @Test
    public void redirectToSuccessOnSuccessfulPostToRegister() throws Exception {
        String name = UUID.randomUUID().toString().substring(0, 10);
        String address = UUID.randomUUID().toString().substring(0, 16);
        String email = name.substring(0, 4) + "@" + address.substring(0, 4) + ".fi";

        MvcResult res = mockMvc.perform(post("/register")
                .param("username", name)
                .param("hometown", address)
                .param("password", "password")
                .param("email", email))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?new_user")).andReturn();

        boolean found = false;
        for (Account account : accountRepository.findAll()) {
            if (account.getUsername() == null) {
                continue;
            }

            if (account.getUsername().equals(name)) {
                found = true;

                // Remove the record after testing
                accountRepository.delete(account);
                break;
            }
        }

        assertTrue("The registration must be added to database on success.", found);
    }

    @Test
    public void returnToFormOnTooShortName() throws Exception {
        String name = UUID.randomUUID().toString().substring(0, 2);
        String address = UUID.randomUUID().toString().substring(0, 16);
        String email = name.substring(0, 1) + "@" + address.substring(0, 4) + ".fi";

        verifyValidationsReturnToForm(name, address, email, "When the name is too short, the user should be shown the form again with existing inputs and error messages.", "The registration must not be added to database on failure.");
    }

    @Test
    public void returnToFormOnTooLongName() throws Exception {
        String name = UUID.randomUUID().toString() + UUID.randomUUID().toString() + UUID.randomUUID().toString();
        name = name.substring(0, 100);
        String address = UUID.randomUUID().toString().substring(0, 16);
        String email = name.substring(0, 1) + "@" + address.substring(0, 4) + ".fi";

        verifyValidationsReturnToForm(name, address, email, "When the name is too long, the user should be shown the form again with existing inputs and error messages.", "The registration must not be added to database on failure.");
    }

    @Test
    public void returnToFormOnTooLongAddress() throws Exception {
        String name = UUID.randomUUID().toString().substring(0, 6);
        String address = UUID.randomUUID().toString() + UUID.randomUUID().toString() + UUID.randomUUID().toString();
        address = address.substring(0, 100);
        String email = name.substring(0, 4) + "@" + address.substring(0, 1) + ".fi";

        verifyValidationsReturnToForm(name, address, email, "When the address is too long, the user should be shown the form again with existing inputs and error messages.", "The registration must not be added to database on failure.");
    }

    @Test
    public void returnToFormOnEmail() throws Exception {
        String name = UUID.randomUUID().toString().substring(0, 6);
        String address = UUID.randomUUID().toString().substring(0, 16);
        String email = name.substring(0, 3) + address.substring(0, 5) + ".fi";

        verifyValidationsReturnToForm(name, address, email, "When the email is invalid, the user should be shown the form again with existing inputs and error messages.", "The registration must not be added to database on failure.");
    }

    @Test
    public void returnToFormOnAllFailure() throws Exception {
        String name = UUID.randomUUID().toString().substring(0, 2);
        String address = UUID.randomUUID().toString().substring(0, 2);
        String email = name.substring(0, 1) + address.substring(0, 1) + ".fi";

        verifyValidationsReturnToForm(name, address, email, "When any of the inputs is faulty, the user should be shown the form again with existing inputs and error messages.", "The registration must not be added to database on failure.");
    }

    private void verifyValidationsReturnToForm(String name, String address, String email, String viewFailsError, String inDatabaseError) {
        try {
            MvcResult res = mockMvc.perform(post("/register")
                    .param("username", name)
                    .param("hometown", address)
                    .param("password", "password")
                    .param("email", email))
                    .andExpect(status().isOk()).andReturn();

            assertEquals("register", res.getModelAndView().getViewName());
        } catch (Throwable t) {
            fail(viewFailsError + " Error: " + t.getMessage());
        }

        boolean found = false;
        for (Account account : accountRepository.findAll()) {
            if (account.getUsername() == null) {
                continue;
            }

            if (account.getUsername().equals(name)) {
                found = true;

                // Remove the record after testing
                accountRepository.delete(account);
                break;
            }
        }

        assertFalse(inDatabaseError, found);
    }
}
