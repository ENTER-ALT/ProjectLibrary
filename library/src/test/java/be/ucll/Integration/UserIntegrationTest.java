package be.ucll.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import be.ucll.model.DomainException;
import be.ucll.model.Membership;
import be.ucll.model.MembershipTest;
import be.ucll.model.Publication;
import be.ucll.model.User;
import be.ucll.model.UserTest;
import be.ucll.repository.DbInitializer;
import be.ucll.repository.LoanRepository;
import be.ucll.repository.MembershipRepository;
import be.ucll.repository.PublicationRepository;
import be.ucll.repository.UserRepository;
import be.ucll.service.LoanService;
import be.ucll.service.LoanServiceTest;
import be.ucll.service.PublicationService;
import be.ucll.service.ServiceException;
import be.ucll.service.UserService;
import be.ucll.utilits.TimeTracker;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient()
@Sql("classpath:schema.sql")
public class UserIntegrationTest {
    
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DbInitializer dbInitializer;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private MembershipRepository membershipRepository;
    @Autowired
    private PublicationRepository publicationRepository;

    @BeforeEach
    public void setupDatabasesAndTime() {
        TimeTracker.resetToday();
        TimeTracker.resetYear();
        dbInitializer.initialize();
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
                        "    ]\n" + //
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
                        "    ]\n" + //
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
        .json("[\r\n" + //
                        "  {\r\n" + //
                        "    \"user\": {\r\n" + //
                        "      \"name\": \"Jane Toe\",\r\n" + //
                        "      \"age\": 30,\r\n" + //
                        "      \"email\": \"jane.toe@ucll.be\",\r\n" + //
                        "      \"password\": \"jane1234\",\r\n" + //
                        "      \"profile\": {\r\n" + //
                        "        \"profileId\": 2,\r\n" + //
                        "        \"bio\": \"Bio 2\",\r\n" + //
                        "        \"location\": \"Location 2\",\r\n" + //
                        "        \"interests\": \"Interests 2\"\r\n" + //
                        "      },\r\n" + //
                        "      \"memberships\": []\r\n" + //
                        "    },\r\n" + //
                        "    \"publications\": [\r\n" + //
                        "      {\r\n" + //
                        "        \"availableCopies\": 5,\r\n" + //
                        "        \"title\": \"The Catcher in the Rye\",\r\n" + //
                        "        \"year\": 1951,\r\n" + //
                        "        \"type\": \"book\",\r\n" + //
                        "        \"author\": \"J.D. Salinger\",\r\n" + //
                        "        \"isbn\": \"978-0316769488\"\r\n" + //
                        "      },\r\n" + //
                        "      {\r\n" + //
                        "        \"availableCopies\": 97,\r\n" + //
                        "        \"title\": \"National Geographic\",\r\n" + //
                        "        \"year\": 2022,\r\n" + //
                        "        \"type\": \"magazine\",\r\n" + //
                        "        \"editor\": \"Editor-in-Chief\",\r\n" + //
                        "        \"issn\": \"12345\"\r\n" + //
                        "      },\r\n" + //
                        "      {\r\n" + //
                        "        \"availableCopies\": 78,\r\n" + //
                        "        \"title\": \"Time\",\r\n" + //
                        "        \"year\": 2022,\r\n" + //
                        "        \"type\": \"magazine\",\r\n" + //
                        "        \"editor\": \"Managing Editor\",\r\n" + //
                        "        \"issn\": \"67890\"\r\n" + //
                        "      },\r\n" + //
                        "      {\r\n" + //
                        "        \"availableCopies\": 58,\r\n" + //
                        "        \"title\": \"Vogue\",\r\n" + //
                        "        \"year\": 2022,\r\n" + //
                        "        \"type\": \"magazine\",\r\n" + //
                        "        \"editor\": \"Fashion Editor\",\r\n" + //
                        "        \"issn\": \"54321\"\r\n" + //
                        "      }\r\n" + //
                        "    ]\r\n" + //
                        "  }\r\n" + //
                        "]");
    }

    @Test
    public void givenInvalidEmail_whenGetUserLoans_thenClientErrorReturned() {
        webTestClient
        .get()
        .uri("/users/s/loans")
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody()
        .json("{\r\n" + //
                        "  \""+ServiceException.class.getSimpleName()+"\": \""+String.format(UserService.USER_WITH_EMAIL_DOESNT_EXIST_EXCEPTION, "s") +"\"\r\n" + //
                        "}");
    }

    @Test
    public void givenEmptyBody_whenPostUsers_thenClientErrorReturned() {
        webTestClient
        .post()
        .uri("/users")
        .header("Content-Type", "application/json")
        .bodyValue("")
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody()
        .json("{\r\n" + //
        "  \""+HttpMessageNotReadableException.class.getSimpleName()+"\": \"Required request body is missing: public be.ucll.model.User be.ucll.controller.UserRestController.addUser(be.ucll.model.User)\"\r\n" + //
        "}");
    }

    @Test
    public void givenExistingUser_whenPostUsers_thenClientErrorIsThrown() {
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
        .is4xxClientError()
        .expectBody()
        .json("{\r\n" + //
        "  \""+ServiceException.class.getSimpleName()+"\": \""+UserService.USER_ALREADY_EXISTS_EXCEPTION +"\"\r\n" + //
        "}");
    }

    @Test
    public void givenManyUsers_whenPostUsers_thenClientErrorIsThrown() {
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
        .is4xxClientError()
        .expectBody()
        .json("{\r\n" + //
        "  \""+HttpMessageNotReadableException.class.getSimpleName()+"\": \"JSON parse error: Cannot deserialize value of type `be.ucll.model.User` from Array value (token `JsonToken.START_ARRAY`)\"\r\n" + //
        "}");
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
        
        assertTrue(userRepository.findByEmail("johnss.doe@ucll.be") != null);
        assertEquals("johnss.doe@ucll.be", userRepository.findByEmail("johnss.doe@ucll.be").getEmail());
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
        
        User actualUser = userRepository.findByEmail("john.doe@ucll.be");
        assertTrue( actualUser != null);
        assertEquals("john.doe@ucll.be", actualUser.getEmail());
        assertEquals("John Does", actualUser.getName());
        assertEquals(27, actualUser.getAge());
        assertEquals("john12342", actualUser.getPassword());
    }

    @Test
    public void givenNonExistingEmail_whenPutUsers_thenClientErrorIsThrown() {
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
        .is4xxClientError()
        .expectBody()
        .json("{\r\n" + //
        "  \""+ServiceException.class.getSimpleName()+"\": \""+UserService.USER_DOESNT_EXIST_EXCEPTION +"\"\r\n" + //
        "}");
    }

    @Test
    public void givenChangedEmail_whenPutUsers_thenClientErrorIsThrown() {
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
        .is4xxClientError()
        .expectBody()
        .json("{\r\n" + //
        "  \""+DomainException.class.getSimpleName()+"\": \""+User.EMAIL_CANNOT_BE_CHANGED_EXCEPTION +"\"\r\n" + //
        "}");
    }

    @Test
    public void givenWrongAgeAndPassword_whenPutUsers_thenClientErrorIsThrown() {
        webTestClient
        .put()
        .uri("/users/john.doe@ucll.be")
        .header("Content-Type", "application/json")
        .bodyValue("  {\n" + //
                        "    \"name\": \"John Does\",\n" + //
                        "    \"age\": 105,\n" + //
                        "    \"email\": \"john.doe@ucll.be\",\n" + //
                        "    \"password\": \"john1\"\n" + //
                        "  }")
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody()
        .json("{\n" + //
                        "  \"password\": \"Password must be at least 8 characters long\",\n" + //
                        "  \"age\": \"Age must be a positive Integer between 0 and 101\"\n" + //
                        "}");
    }

    @Test
    public void givenNonExistingEmail_whenDeleteUsers_thenClientErrorIsThrown() {
        webTestClient
        .delete()
        .uri("/users/jasda@.asdasd")
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody()
        .json("{\r\n" + //
        "  \""+ServiceException.class.getSimpleName()+"\": \""+UserService.USER_DOESNT_EXIST_EXCEPTION +"\"\r\n" + //
        "}");
    }

    @Test
    public void givenEmailWithActiveLoans_whenDeleteUsers_thenClientErrorIsThrown() {
        webTestClient
        .delete()
        .uri("/users/jane.toe@ucll.be")
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody()
        .json("{\r\n" + //
        "  \""+ServiceException.class.getSimpleName()+"\": \""+LoanService.USER_HAS_ACTIVE_LOANS_EXCEPTION +"\"\r\n" + //
        "}");
    }

    @Test
    public void givenEmailWithoutActiveLoans_whenDeleteUsers_thenUserDeleted() {

        assertTrue(userRepository.findByEmail("john.doe@ucll.be") != null);
        assertTrue(loanRepository.findByUserEmail("john.doe@ucll.be").size() > 0);

        webTestClient
        .delete()
        .uri("/users/john.doe@ucll.be")
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is2xxSuccessful();

        assertTrue(userRepository.findByEmail("john.doe@ucll.be") == null);
        assertTrue(loanRepository.findByUserEmail("john.doe@ucll.be").size() == 0);
    }

    @Test
    public void givenUsers_whenGettingTheOldestUser_thenTheOldestUserIsGiven() {
        webTestClient
        .get()
        .uri("/users/oldest")
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("{\n" + //
                        "  \"name\": \"Jane Toe\",\n" + //
                        "  \"age\": 30,\n" + //
                        "  \"email\": \"jane.toe@ucll.be\",\n" + //
                        "  \"password\": \"jane1234\",\n" + //
                        "  \"profile\": {\n" + //
                        "    \"profileId\": 2,\n" + //
                        "    \"bio\": \"Bio 2\",\n" + //
                        "    \"location\": \"Location 2\",\n" + //
                        "    \"interests\": \"Interests 2\"\n" + //
                        "  }\n" + //
                        "}");
    }

    @Test
    public void givenManyOldestUsers_whenGettingTheOldestUser_thenTheFirstOldestUserIsGiven() {
        User user = new User ("User", 30, "asdasd@gamil.com", "asdasdasdasdasdasd");
        userRepository.save(user);
        webTestClient
        .get()
        .uri("/users/oldest")
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("{\n" + //
                        "  \"name\": \"Jane Toe\",\n" + //
                        "  \"age\": 30,\n" + //
                        "  \"email\": \"jane.toe@ucll.be\",\n" + //
                        "  \"password\": \"jane1234\",\n" + //
                        "  \"profile\": {\n" + //
                        "    \"profileId\": 2,\n" + //
                        "    \"bio\": \"Bio 2\",\n" + //
                        "    \"location\": \"Location 2\",\n" + //
                        "    \"interests\": \"Interests 2\"\n" + //
                        "  }\n" + //
                        "}");
    }

    @Test
    public void givenNoUsers_whenGettingTheOldestUser_thenClientErrorIsThrown() {
        clearUserRepository();
        webTestClient
        .get()
        .uri("/users/oldest")
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody()
        .json("{\r\n" + //
        "  \""+ServiceException.class.getSimpleName()+"\": \""+UserService.NO_OLDEST_USER_FOUND_EXCEPTION +"\"\r\n" + //
        "}");
    }

    @Test
    public void givenNoUsers_whenGettingUsersWithInterests_thenClientErrorIsThrown() {
        clearUserRepository();
        webTestClient
        .get()
        .uri("/users/interest/Interest 2")
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody()
        .json("{\r\n" + //
        "  \""+ServiceException.class.getSimpleName()+"\": \""+String.format(UserService.NO_USERS_FOUND_WITH_INTEREST_IN_EXCEPTION,"Interest 2") +"\"\r\n" + //
        "}");
    }

    @Test
    public void givenEmptyInterests_whenGettingUsersWithInterests_thenClientErrorIsThrown() {
        webTestClient
        .get()
        .uri("/users/interest/ ")
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody()
        .json("{\r\n" + //
        "  \""+ServiceException.class.getSimpleName()+"\": \""+UserService.INTEREST_CANNOT_BE_EMPTY_EXCEPTION +"\"\r\n" + //
        "}");
    }

    @Test
    public void givenInterestsThatUsersDoNotHave_whenGettingUsersWithInterests_thenClientErrorIsThrown() {
        webTestClient
        .get()
        .uri("/users/interest/Interest")
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody()
        .json("{\r\n" + //
        "  \""+ServiceException.class.getSimpleName()+"\": \""+String.format(UserService.NO_USERS_FOUND_WITH_INTEREST_IN_EXCEPTION,"Interest") +"\"\r\n" + //
        "}");
    }

    @Test
    public void givenInterests_whenGettingUsersWithInterests_thenUsersWithTheInterestReturned() {
        webTestClient
        .get()
        .uri("/users/interest/interests 2")
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("[\n" + //
                        "  {\n" + //
                        "    \"name\": \"Jane Toe\",\n" + //
                        "    \"age\": 30,\n" + //
                        "    \"email\": \"jane.toe@ucll.be\",\n" + //
                        "    \"password\": \"jane1234\",\n" + //
                        "    \"profile\": {\n" + //
                        "      \"profileId\": 2,\n" + //
                        "      \"bio\": \"Bio 2\",\n" + //
                        "      \"location\": \"Location 2\",\n" + //
                        "      \"interests\": \"Interests 2\"\n" + //
                        "    }\n" + //
                        "  },\n" + //
                        "  {\n" + //
                        "    \"name\": \"Birgit Doe\",\n" + //
                        "    \"age\": 18,\n" + //
                        "    \"email\": \"birgit.doe@ucll.be\",\n" + //
                        "    \"password\": \"birgit1234\",\n" + //
                        "    \"profile\": {\n" + //
                        "      \"profileId\": 5,\n" + //
                        "      \"bio\": \"Bio 5\",\n" + //
                        "      \"location\": \"Location 5\",\n" + //
                        "      \"interests\": \"Interests 2\"\n" + //
                        "    }\n" + //
                        "  }\n" + //
                        "]");
        webTestClient
        .get()
        .uri("/users/interest/INTERESTS 2")
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("[\n" + //
                        "  {\n" + //
                        "    \"name\": \"Jane Toe\",\n" + //
                        "    \"age\": 30,\n" + //
                        "    \"email\": \"jane.toe@ucll.be\",\n" + //
                        "    \"password\": \"jane1234\",\n" + //
                        "    \"profile\": {\n" + //
                        "      \"profileId\": 2,\n" + //
                        "      \"bio\": \"Bio 2\",\n" + //
                        "      \"location\": \"Location 2\",\n" + //
                        "      \"interests\": \"Interests 2\"\n" + //
                        "    }\n" + //
                        "  },\n" + //
                        "  {\n" + //
                        "    \"name\": \"Birgit Doe\",\n" + //
                        "    \"age\": 18,\n" + //
                        "    \"email\": \"birgit.doe@ucll.be\",\n" + //
                        "    \"password\": \"birgit1234\",\n" + //
                        "    \"profile\": {\n" + //
                        "      \"profileId\": 5,\n" + //
                        "      \"bio\": \"Bio 5\",\n" + //
                        "      \"location\": \"Location 5\",\n" + //
                        "      \"interests\": \"Interests 2\"\n" + //
                        "    }\n" + //
                        "  }\n" + //
                        "]");
    }

    @Test
    public void givenNoUsers_whenGettingUsersWithInterestsAndGreaterAge_thenClientErrorIsThrown() {
        clearUserRepository();
        String interest = "Interests 2";
        Integer age = 1;
        webTestClient
        .get()
        .uri("/users/interest/"+interest+"/"+age)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody()
        .json("{\r\n" + //
        "  \""+ServiceException.class.getSimpleName()+"\": \""+String.format(UserService.NO_USERS_FOUND_WITH_INTEREST_OLDER_THAN_EXCEPTION,interest, age) +"\"\r\n" + //
        "}");
    }

    @Test
    public void givenEmptyInterests_whenGettingUsersWithInterestsAndGreaterAge_thenClientErrorIsThrown() {
        String interest = " ";
        Integer age = 1;
        webTestClient
        .get()
        .uri("/users/interest/"+interest+"/"+age)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody()
        .json("{\r\n" + //
        "  \""+ServiceException.class.getSimpleName()+"\": \""+UserService.INTEREST_CANNOT_BE_EMPTY_EXCEPTION +"\"\r\n" + //
        "}");
    }

    @Test
    public void givenInvalidAge_whenGettingUsersWithInterestsAndGreaterAge_thenClientErrorIsThrown() {
        String interest = "Interest 2";
        Integer age = 151;
        webTestClient
        .get()
        .uri("/users/interest/"+interest+"/"+age)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody()
        .json("{\r\n" + //
        "  \""+ServiceException.class.getSimpleName()+"\": \""+UserService.INVALID_AGE_RANGE_EXCEPTION +"\"\r\n" + //
        "}");
    }

    @Test
    public void givenValidInfo_whenGettingUsersWithInterestsAndGreaterAge_thenClientErrorIsThrown() {
        String interest = "Interests 2";
        Integer age = 3;
        webTestClient
        .get()
        .uri("/users/interest/"+interest+"/"+age)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("[\r\n" + //
                        "  {\r\n" + //
                        "    \"name\": \"Jane Toe\",\r\n" + //
                        "    \"age\": 30,\r\n" + //
                        "    \"email\": \"jane.toe@ucll.be\",\r\n" + //
                        "    \"password\": \"jane1234\",\r\n" + //
                        "    \"profile\": {\r\n" + //
                        "      \"profileId\": 2,\r\n" + //
                        "      \"bio\": \"Bio 2\",\r\n" + //
                        "      \"location\": \"Location 2\",\r\n" + //
                        "      \"interests\": \"Interests 2\"\r\n" + //
                        "    }\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"name\": \"Birgit Doe\",\r\n" + //
                        "    \"age\": 18,\r\n" + //
                        "    \"email\": \"birgit.doe@ucll.be\",\r\n" + //
                        "    \"password\": \"birgit1234\",\r\n" + //
                        "    \"profile\": {\r\n" + //
                        "      \"profileId\": 5,\r\n" + //
                        "      \"bio\": \"Bio 5\",\r\n" + //
                        "      \"location\": \"Location 5\",\r\n" + //
                        "      \"interests\": \"Interests 2\"\r\n" + //
                        "    }\r\n" + //
                        "  }\r\n" + //
                        "]");
    }

    @Test
    public void givenValidMembership_whenAddAMembership_thenMembershipAddedToUser() {
        
        TimeTracker.setCustomToday(LocalDate.of(1111, 1, 1));
        String email = "sarah.doe@ucll.be";

        webTestClient
        .post()
        .uri("/users/" + email + "/membership")
        .header("Content-Type", "application/json")
        .bodyValue("{\n" + //
                        "  \"startDate\": \"2024-05-10\",\n" + //
                        "  \"endDate\": \"2025-05-10\",\n" + //
                        "  \"type\": \"BRONZE\",\n" + //
                        "  \"freeLoansQuantity\": \"4\"\n" + //
                        "}")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("  {\n" + //
                        "    \"name\": \"Sarah Doe\",\n" + //
                        "    \"age\": 4,\n" + //
                        "    \"email\": \"sarah.doe@ucll.be\",\n" + //
                        "    \"password\": \"sarah1234\",\n" + //
                        "    \"profile\": null,\n" + //
                        "    \"memberships\": [\n" + //
                        "      {\n" + //
                        "        \"startDate\": \"2024-05-10\",\n" + //
                        "        \"endDate\": \"2025-05-10\",\n" + //
                        "        \"type\": \"BRONZE\"\n" + //
                        "      }\n" + //
                        "    ]\n" + //
                        "  }");
    }    

    @Test
    public void givenValidLoan_whenRegisterLoan_thenLoanRegistered() {
        
        LocalDate today = TimeTracker.getToday();
        String email = "sarah.doe@ucll.be";
        List<Publication> publications = publicationRepository.findAll();
        List<Long> ids = publications.stream().map(publication -> publication.getId()).toList().subList(0, 3);
        String bodyValue = ids.toString();

        webTestClient
        .post()
        .uri("/users/" + email + "/loans/" + today)
        .header("Content-Type", "application/json")
        .bodyValue(bodyValue)
        .exchange()
        .expectStatus()
        .is2xxSuccessful();

        assertTrue(loanRepository.findByUserEmailAndEndDateAfter(email, today).size() > 0);
    }   

    @Test 
    public void givenWrongIDs_whenRegisterLoan_thanServiceExceptionThrown() {
        String email = "sarah.doe@ucll.be";
        LocalDate today = TimeTracker.getToday();
        List<Publication> publications = publicationRepository.findAll();
        List<Long> ids = publications.stream().map(publication -> publication.getId()).toList().subList(0, 3);

        List<Long> unexistingId = new ArrayList<>(List.of(LoanServiceTest.generateUniqueNumber(ids)));
        String bodyValue = unexistingId.toString();

        webTestClient
        .post()
        .uri("/users/" + email + "/loans/" + today)
        .header("Content-Type", "application/json")
        .bodyValue(bodyValue)
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody()
        .json("{\r\n" + //
        "  \""+ServiceException.class.getSimpleName()+"\": \""+String.format(PublicationService.PUBLICATION_NOT_FOUND_EXCEPTION, unexistingId.get(0)) +"\"\r\n" + //
        "}");
    }

    @Test
    public void givenValidInfo_whenReturnLoan_thenLoanReturned() {
        
        LocalDate today = TimeTracker.getToday();
        String email = "sarah.doe@ucll.be";
        List<Publication> publications = publicationRepository.findAll();
        List<Long> ids = publications.stream().map(publication -> publication.getId()).toList().subList(0, 3);
        String bodyValue = ids.toString();

        webTestClient
        .post()
        .uri("/users/" + email + "/loans/" + today)
        .header("Content-Type", "application/json")
        .bodyValue(bodyValue)
        .exchange()
        .expectStatus()
        .is2xxSuccessful();

        TimeTracker.setCustomToday(today.plusDays(1));
        webTestClient
        .put()
        .uri("/users/" + email + "/loans/return/" + today.plusDays(1))
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is2xxSuccessful();
    }  

    @Test 
    public void givenValidDateAndMembership_whenGettingMembershipByDate_thanMembershipReturned() {
        TimeTracker.setCustomToday(MembershipTest.DEFAULT_TODAY);
        LocalDate date = TimeTracker.getToday().plusDays(10);
        User user = UserTest.createDefaultUser();
        String email = user.getEmail();
        Membership membership = MembershipTest.createDefaultBronzeMembership();
        user.setMembership(membership);
        membership.setUser(user);
        userRepository.save(user);
        membershipRepository.save(membership);

        String expectedJson =String.format("{\n" + //
                        "  \"startDate\": \"%s\",\n" + //
                        "  \"endDate\": \"%s\",\n" + //
                        "  \"type\": \"%s\",\n" + //
                        "  \"freeLoansQuantity\": %d\n" + //
                        "}", membership.getStartDate().minusDays(1), membership.getEndDate().minusDays(1), membership.getType(), membership.getFreeLoansQuantity());

        webTestClient
        .get()
        .uri("/users/"+email+"/membership?date="+date)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json(expectedJson);
    }

    public void clearUserRepository() {
        loanRepository.deleteAll();
        membershipRepository.deleteAll();
        userRepository.deleteAll();
    }
}
