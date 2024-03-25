package be.ucll.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import be.ucll.model.User;
import be.ucll.repository.LoanRepository;
import be.ucll.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class UserIntegrationTest {
    
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoanRepository loanRepository;

    @AfterEach
    public void resetRepository() {
        userRepository.resetRepository();
    }

    @Test
    public void givenUsers_whenGetUsers_thenUsersAreReturned() {
        webTestClient
        .get()
        .uri("/users")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("[\n" + //
                        "  {\n" + //
                        "    \"name\": \"John Doe\",\n" + //
                        "    \"age\": 25,\n" + //
                        "    \"email\": \"john.doe@ucll.be\",\n" + //
                        "    \"password\": \"john1234\"\n" + //
                        "  },\n" + //
                        "  {\n" + //
                        "    \"name\": \"Jane Toe\",\n" + //
                        "    \"age\": 30,\n" + //
                        "    \"email\": \"jane.toe@ucll.be\",\n" + //
                        "    \"password\": \"jane1234\"\n" + //
                        "  },\n" + //
                        "  {\n" + //
                        "    \"name\": \"Jack Doe\",\n" + //
                        "    \"age\": 5,\n" + //
                        "    \"email\": \"jack.doe@ucll.be\",\n" + //
                        "    \"password\": \"jack1234\"\n" + //
                        "  },\n" + //
                        "  {\n" + //
                        "    \"name\": \"Sarah Doe\",\n" + //
                        "    \"age\": 4,\n" + //
                        "    \"email\": \"sarah.doe@ucll.be\",\n" + //
                        "    \"password\": \"sarah1234\"\n" + //
                        "  },\n" + //
                        "  {\n" + //
                        "    \"name\": \"Birgit Doe\",\n" + //
                        "    \"age\": 18,\n" + //
                        "    \"email\": \"birgit.doe@ucll.be\",\n" + //
                        "    \"password\": \"birgit1234\"\n" + //
                        "  }\n" + //
                        "]");
    }

    @Test
    public void givenUsersAdults_whenGetUsers_thenUsersAdultsAreReturned() {
        webTestClient
        .get()
        .uri("/users/adults")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("[\n" + //
                        "  {\n" + //
                        "    \"name\": \"John Doe\",\n" + //
                        "    \"age\": 25,\n" + //
                        "    \"email\": \"john.doe@ucll.be\",\n" + //
                        "    \"password\": \"john1234\"\n" + //
                        "  },\n" + //
                        "  {\n" + //
                        "    \"name\": \"Jane Toe\",\n" + //
                        "    \"age\": 30,\n" + //
                        "    \"email\": \"jane.toe@ucll.be\",\n" + //
                        "    \"password\": \"jane1234\"\n" + //
                        "  },\n" + //
                        "  {\n" + //
                        "    \"name\": \"Birgit Doe\",\n" + //
                        "    \"age\": 18,\n" + //
                        "    \"email\": \"birgit.doe@ucll.be\",\n" + //
                        "    \"password\": \"birgit1234\"\n" + //
                        "  }\n" + //
                        "]");
    }

    @Test
    public void givenUsersMinAge0MaxAge18_whenGetUsers_thenUsersInAgeRangeAreReturned() {
        webTestClient
        .get()
        .uri("/users/age/0/18")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("[\n" + //
                        "  {\n" + //
                        "    \"name\": \"Jack Doe\",\n" + //
                        "    \"age\": 5,\n" + //
                        "    \"email\": \"jack.doe@ucll.be\",\n" + //
                        "    \"password\": \"jack1234\"\n" + //
                        "  },\n" + //
                        "  {\n" + //
                        "    \"name\": \"Sarah Doe\",\n" + //
                        "    \"age\": 4,\n" + //
                        "    \"email\": \"sarah.doe@ucll.be\",\n" + //
                        "    \"password\": \"sarah1234\"\n" + //
                        "  },\n" + //
                        "  {\n" + //
                        "    \"name\": \"Birgit Doe\",\n" + //
                        "    \"age\": 18,\n" + //
                        "    \"email\": \"birgit.doe@ucll.be\",\n" + //
                        "    \"password\": \"birgit1234\"\n" + //
                        "  }\n" + //
                        "]");
    }

    @Test
    public void givenValidEmail_whenGetUserLoans_thenUsersLoansAreReturned() {
        webTestClient
        .get()
        .uri("/users/jane.toe@ucll.be/loans")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("[\n" + //
                        "  {\n" + //
                        "    \"user\": {\n" + //
                        "      \"name\": \"Jane Toe\",\n" + //
                        "      \"age\": 30,\n" + //
                        "      \"email\": \"jane.toe@ucll.be\",\n" + //
                        "      \"password\": \"jane1234\"\n" + //
                        "    },\n" + //
                        "    \"publications\": [\n" + //
                        "      {\n" + //
                        "        \"availableCopies\": 18,\n" + //
                        "        \"title\": \"1984\",\n" + //
                        "        \"year\": 1949,\n" + //
                        "        \"author\": \"George Orwell\",\n" + //
                        "        \"isbn\": \"978-0451524935\"\n" + //
                        "      },\n" + //
                        "      {\n" + //
                        "        \"availableCopies\": 10,\n" + //
                        "        \"title\": \"Pride and Prejudice\",\n" + //
                        "        \"year\": 1813,\n" + //
                        "        \"author\": \"Jane Austen\",\n" + //
                        "        \"isbn\": \"978-0141439518\"\n" + //
                        "      }\n" + //
                        "    ],\n" + //
                        "    \"startDate\": \"1111-01-02\",\n" + //
                        "    \"endDate\": \"1111-01-04\"\n" + //
                        "  },\n" + //
                        "  {\n" + //
                        "    \"user\": {\n" + //
                        "      \"name\": \"Jane Toe\",\n" + //
                        "      \"age\": 30,\n" + //
                        "      \"email\": \"jane.toe@ucll.be\",\n" + //
                        "      \"password\": \"jane1234\"\n" + //
                        "    },\n" + //
                        "    \"publications\": [\n" + //
                        "      {\n" + //
                        "        \"availableCopies\": 5,\n" + //
                        "        \"title\": \"The Catcher in the Rye\",\n" + //
                        "        \"year\": 1951,\n" + //
                        "        \"author\": \"J.D. Salinger\",\n" + //
                        "        \"isbn\": \"978-0316769488\"\n" + //
                        "      },\n" + //
                        "      {\n" + //
                        "        \"availableCopies\": 97,\n" + //
                        "        \"title\": \"National Geographic\",\n" + //
                        "        \"year\": 2022,\n" + //
                        "        \"editor\": \"Editor-in-Chief\",\n" + //
                        "        \"issn\": \"12345\"\n" + //
                        "      },\n" + //
                        "      {\n" + //
                        "        \"availableCopies\": 78,\n" + //
                        "        \"title\": \"Time\",\n" + //
                        "        \"year\": 2022,\n" + //
                        "        \"editor\": \"Managing Editor\",\n" + //
                        "        \"issn\": \"67890\"\n" + //
                        "      },\n" + //
                        "      {\n" + //
                        "        \"availableCopies\": 58,\n" + //
                        "        \"title\": \"Vogue\",\n" + //
                        "        \"year\": 2022,\n" + //
                        "        \"editor\": \"Fashion Editor\",\n" + //
                        "        \"issn\": \"54321\"\n" + //
                        "      }\n" + //
                        "    ],\n" + //
                        "    \"startDate\": \"1111-01-02\",\n" + //
                        "    \"endDate\": null\n" + //
                        "  }\n" + //
                        "]");
    }

    @Test
    public void givenValidEmailOnlyActiveTrue_whenGetUserLoans_thenActiveUsersLoansReturned() {
        webTestClient
        .get()
        .uri("/users/jane.toe@ucll.be/loans?onlyActive=true")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("[\n" + //
                        "  {\n" + //
                        "    \"user\": {\n" + //
                        "      \"name\": \"Jane Toe\",\n" + //
                        "      \"age\": 30,\n" + //
                        "      \"email\": \"jane.toe@ucll.be\",\n" + //
                        "      \"password\": \"jane1234\"\n" + //
                        "    },\n" + //
                        "    \"publications\": [\n" + //
                        "      {\n" + //
                        "        \"availableCopies\": 5,\n" + //
                        "        \"title\": \"The Catcher in the Rye\",\n" + //
                        "        \"year\": 1951,\n" + //
                        "        \"author\": \"J.D. Salinger\",\n" + //
                        "        \"isbn\": \"978-0316769488\"\n" + //
                        "      },\n" + //
                        "      {\n" + //
                        "        \"availableCopies\": 97,\n" + //
                        "        \"title\": \"National Geographic\",\n" + //
                        "        \"year\": 2022,\n" + //
                        "        \"editor\": \"Editor-in-Chief\",\n" + //
                        "        \"issn\": \"12345\"\n" + //
                        "      },\n" + //
                        "      {\n" + //
                        "        \"availableCopies\": 78,\n" + //
                        "        \"title\": \"Time\",\n" + //
                        "        \"year\": 2022,\n" + //
                        "        \"editor\": \"Managing Editor\",\n" + //
                        "        \"issn\": \"67890\"\n" + //
                        "      },\n" + //
                        "      {\n" + //
                        "        \"availableCopies\": 58,\n" + //
                        "        \"title\": \"Vogue\",\n" + //
                        "        \"year\": 2022,\n" + //
                        "        \"editor\": \"Fashion Editor\",\n" + //
                        "        \"issn\": \"54321\"\n" + //
                        "      }\n" + //
                        "    ],\n" + //
                        "    \"startDate\": \"1111-01-02\",\n" + //
                        "    \"endDate\": null\n" + //
                        "  }\n" + //
                        "]");
    }

    @Test
    public void givenInvalidEmail_whenGetUserLoans_thenServerErrorReturned() {
        webTestClient
        .get()
        .uri("/users/s/loans")
        .exchange()
        .expectStatus()
        .is5xxServerError();
    }

    @Test
    public void givenEmptyBody_whenPostUsers_thenServerErrorReturned() {
        webTestClient
        .post()
        .uri("/users")
        .header("Content-Type", "application/json")
        .bodyValue("")
        .exchange()
        .expectStatus()
        .is4xxClientError();
    }

    @Test
    public void givenExistingUser_whenPostUsers_thenServerErrorIsThrown() {
        webTestClient
        .post()
        .uri("/users")
        .header("Content-Type", "application/json")
        .bodyValue("  {\n" + //
                        "    \"name\": \"John Doe\",\n" + //
                        "    \"age\": 25,\n" + //
                        "    \"email\": \"john.doe@ucll.be\",\n" + //
                        "    \"password\": \"john1234\"\n" + //
                        "  }")
        .exchange()
        .expectStatus()
        .is5xxServerError();
    }

    @Test
    public void givenManyUsers_whenPostUsers_thenServerErrorIsThrown() {
        webTestClient
        .post()
        .uri("/users")
        .header("Content-Type", "application/json")
        .bodyValue(" [ {\n" + //
                        "    \"name\": \"Johns Doe\",\n" + //
                        "    \"age\": 26,\n" + //
                        "    \"email\": \"john.dsse@ucll.be\",\n" + //
                        "    \"password\": \"john1234asd\"\n" + //
                        "  },\n" + //
                        "  {\n" + //
                        "    \"name\": \"Johnss Doe\",\n" + //
                        "    \"age\": 26,\n" + //
                        "    \"email\": \"john.dsssse@ucll.be\",\n" + //
                        "    \"password\": \"john1234asdss\"\n" + //
                        "  }]")
        .exchange()
        .expectStatus()
        .is4xxClientError();
    }

    @Test
    public void givenValidUser_whenPostUsers_thenUserIsSaved() {
        webTestClient
        .post()
        .uri("/users")
        .header("Content-Type", "application/json")
        .bodyValue("{\n" + //
                        "  \"name\": \"Johns Doe\",\n" + //
                        "  \"age\": 25,\n" + //
                        "  \"email\": \"johnss.doe@ucll.be\",\n" + //
                        "  \"password\": \"john1234ss\"\n" + //
                        "}")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("{\n" + //
                        "  \"name\": \"Johns Doe\",\n" + //
                        "  \"age\": 25,\n" + //
                        "  \"email\": \"johnss.doe@ucll.be\",\n" + //
                        "  \"password\": \"john1234ss\"\n" + //
                        "}");
        
        assertTrue(userRepository.userByEmail("johnss.doe@ucll.be") != null);
        assertEquals("johnss.doe@ucll.be", userRepository.userByEmail("johnss.doe@ucll.be").getEmail());
    }

    @Test
    public void givenValidUser_whenPutUsers_thenUserIsSaved() {
        webTestClient
        .put()
        .uri("/users/john.doe@ucll.be")
        .header("Content-Type", "application/json")
        .bodyValue("{\n" + //
                        "    \"name\": \"John Does\",\n" + //
                        "    \"age\": 27,\n" + //
                        "    \"email\": \"john.doe@ucll.be\",\n" + //
                        "    \"password\": \"john12342\"\n" + //
                        "  }")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("{\n" + //
                        "  \"name\": \"John Does\",\n" + //
                        "  \"age\": 27,\n" + //
                        "  \"email\": \"john.doe@ucll.be\",\n" + //
                        "  \"password\": \"john12342\"\n" + //
                        "}");
        
        User actualUser = userRepository.userByEmail("john.doe@ucll.be");
        assertTrue( actualUser != null);
        assertEquals("john.doe@ucll.be", actualUser.getEmail());
        assertEquals("John Does", actualUser.getName());
        assertEquals(27, actualUser.getAge());
        assertEquals("john12342", actualUser.getPassword());
    }

    @Test
    public void givenNonExistingEmail_whenPutUsers_thenServerErrorIsThrown() {
        webTestClient
        .put()
        .uri("/users/joasdas@.sda")
        .header("Content-Type", "application/json")
        .bodyValue("{\n" + //
                        "    \"name\": \"John Does\",\n" + //
                        "    \"age\": 27,\n" + //
                        "    \"email\": \"john.doe@ucll.be\",\n" + //
                        "    \"password\": \"john12342\"\n" + //
                        "  }")
        .exchange()
        .expectStatus()
        .is5xxServerError();
    }

    @Test
    public void givenChangedEmail_whenPutUsers_thenServerErrorIsThrown() {
        webTestClient
        .put()
        .uri("/users/john.doe@ucll.be")
        .header("Content-Type", "application/json")
        .bodyValue("{\n" + //
                        "    \"name\": \"John Does\",\n" + //
                        "    \"age\": 27,\n" + //
                        "    \"email\": \"john.dosdeasdasdasd@ucll.be\",\n" + //
                        "    \"password\": \"john12342\"\n" + //
                        "  }")
        .exchange()
        .expectStatus()
        .is5xxServerError();
    }

    @Test
    public void givenNonExistingEmail_whenDeleteUsers_thenServerErrorIsThrown() {
        webTestClient
        .delete()
        .uri("/users/jasda@.asdasd")
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is5xxServerError();
    }

    @Test
    public void givenEmailWithActiveLoans_whenDeleteUsers_thenServerErrorIsThrown() {
        webTestClient
        .delete()
        .uri("/users/jane.toe@ucll.be")
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is5xxServerError();
    }

    @Test
    public void givenEmailWithoutActiveLoans_whenDeleteUsers_thenUserDeleted() {

        assertTrue(userRepository.userByEmail("john.doe@ucll.be") != null);
        assertTrue(loanRepository.findLoansByEmail("john.doe@ucll.be", false).size() > 0);

        webTestClient
        .delete()
        .uri("/users/john.doe@ucll.be")
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is2xxSuccessful();

        assertTrue(userRepository.userByEmail("john.doe@ucll.be") == null);
        assertTrue(loanRepository.findLoansByEmail("john.doe@ucll.be", false).size() == 0);
    }
}
