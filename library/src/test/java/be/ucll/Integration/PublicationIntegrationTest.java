package be.ucll.Integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import be.ucll.repository.DbInitializer;
import be.ucll.service.PublicationService;
import be.ucll.service.ServiceException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Sql("classpath:schema.sql")
public class PublicationIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private DbInitializer dbInitializer;

    @BeforeEach
    public void setupDatabases() {
        dbInitializer.initialize();
    }

    @Test
    public void givenPublications_whenGetPublications_thenPublicationsAreReturned() {
        webTestClient
        .get()
        .uri("/publications")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("[\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 9,\r\n" + //
                        "    \"title\": \"The Great Gatsby\",\r\n" + //
                        "    \"year\": 1925,\r\n" + //
                        "    \"author\": \"F. Scott Fitzgerald\",\r\n" + //
                        "    \"isbn\": \"978-0743273565\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 14,\r\n" + //
                        "    \"title\": \"To Kill a Mockingbird\",\r\n" + //
                        "    \"year\": 1960,\r\n" + //
                        "    \"author\": \"Harper Lee\",\r\n" + //
                        "    \"isbn\": \"978-0061120084\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 18,\r\n" + //
                        "    \"title\": \"1984\",\r\n" + //
                        "    \"year\": 1949,\r\n" + //
                        "    \"author\": \"George Orwell\",\r\n" + //
                        "    \"isbn\": \"978-0451524935\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 10,\r\n" + //
                        "    \"title\": \"Pride and Prejudice\",\r\n" + //
                        "    \"year\": 1813,\r\n" + //
                        "    \"author\": \"Jane Austen\",\r\n" + //
                        "    \"isbn\": \"978-0141439518\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 5,\r\n" + //
                        "    \"title\": \"The Catcher in the Rye\",\r\n" + //
                        "    \"year\": 1951,\r\n" + //
                        "    \"author\": \"J.D. Salinger\",\r\n" + //
                        "    \"isbn\": \"978-0316769488\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 97,\r\n" + //
                        "    \"title\": \"National Geographic\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Editor-in-Chief\",\r\n" + //
                        "    \"issn\": \"12345\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 78,\r\n" + //
                        "    \"title\": \"Time\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Managing Editor\",\r\n" + //
                        "    \"issn\": \"67890\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 58,\r\n" + //
                        "    \"title\": \"Vogue\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Fashion Editor\",\r\n" + //
                        "    \"issn\": \"54321\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 39,\r\n" + //
                        "    \"title\": \"Scientific American\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Science Editor\",\r\n" + //
                        "    \"issn\": \"98765\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 19,\r\n" + //
                        "    \"title\": \"Sports Illustrated\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Sports Editor\",\r\n" + //
                        "    \"issn\": \"13579\"\r\n" + //
                        "  }\r\n" + //
                        "]");
    }

    @Test
    public void givenPublicationsTitleA_whenGetPublications_thenPublicationsWithAInTitleAreReturned() {
        webTestClient
        .get()
        .uri("/publications?title=a")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("[\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 9,\r\n" + //
                        "    \"title\": \"The Great Gatsby\",\r\n" + //
                        "    \"year\": 1925,\r\n" + //
                        "    \"author\": \"F. Scott Fitzgerald\",\r\n" + //
                        "    \"isbn\": \"978-0743273565\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 14,\r\n" + //
                        "    \"title\": \"To Kill a Mockingbird\",\r\n" + //
                        "    \"year\": 1960,\r\n" + //
                        "    \"author\": \"Harper Lee\",\r\n" + //
                        "    \"isbn\": \"978-0061120084\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 10,\r\n" + //
                        "    \"title\": \"Pride and Prejudice\",\r\n" + //
                        "    \"year\": 1813,\r\n" + //
                        "    \"author\": \"Jane Austen\",\r\n" + //
                        "    \"isbn\": \"978-0141439518\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 5,\r\n" + //
                        "    \"title\": \"The Catcher in the Rye\",\r\n" + //
                        "    \"year\": 1951,\r\n" + //
                        "    \"author\": \"J.D. Salinger\",\r\n" + //
                        "    \"isbn\": \"978-0316769488\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 97,\r\n" + //
                        "    \"title\": \"National Geographic\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Editor-in-Chief\",\r\n" + //
                        "    \"issn\": \"12345\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 39,\r\n" + //
                        "    \"title\": \"Scientific American\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Science Editor\",\r\n" + //
                        "    \"issn\": \"98765\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 19,\r\n" + //
                        "    \"title\": \"Sports Illustrated\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Sports Editor\",\r\n" + //
                        "    \"issn\": \"13579\"\r\n" + //
                        "  }\r\n" + //
                        "]");
    }

    @Test
    public void givenPublicationsTitleANonExistingType_whenGetPublications_thenZeroPublicationsAreReturned() {
        webTestClient
        .get()
        .uri("/publications?title=a&type=s")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("[]");
    }

    @Test
    public void givenPublicationsBookType_whenGetPublications_thenBookPublicationsAreReturned() {
        webTestClient
        .get()
        .uri("/publications?type=Book")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("[\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 9,\r\n" + //
                        "    \"title\": \"The Great Gatsby\",\r\n" + //
                        "    \"year\": 1925,\r\n" + //
                        "    \"author\": \"F. Scott Fitzgerald\",\r\n" + //
                        "    \"isbn\": \"978-0743273565\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 14,\r\n" + //
                        "    \"title\": \"To Kill a Mockingbird\",\r\n" + //
                        "    \"year\": 1960,\r\n" + //
                        "    \"author\": \"Harper Lee\",\r\n" + //
                        "    \"isbn\": \"978-0061120084\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 18,\r\n" + //
                        "    \"title\": \"1984\",\r\n" + //
                        "    \"year\": 1949,\r\n" + //
                        "    \"author\": \"George Orwell\",\r\n" + //
                        "    \"isbn\": \"978-0451524935\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 10,\r\n" + //
                        "    \"title\": \"Pride and Prejudice\",\r\n" + //
                        "    \"year\": 1813,\r\n" + //
                        "    \"author\": \"Jane Austen\",\r\n" + //
                        "    \"isbn\": \"978-0141439518\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 5,\r\n" + //
                        "    \"title\": \"The Catcher in the Rye\",\r\n" + //
                        "    \"year\": 1951,\r\n" + //
                        "    \"author\": \"J.D. Salinger\",\r\n" + //
                        "    \"isbn\": \"978-0316769488\"\r\n" + //
                        "  }\r\n" + //
                        "]");
    }

    @Test
    public void givenPublicationsMagazineType_whenGetPublications_thenMagazinePublicationsAreReturned() {
        webTestClient
        .get()
        .uri("/publications?type=Magazine")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("[\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 97,\r\n" + //
                        "    \"title\": \"National Geographic\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Editor-in-Chief\",\r\n" + //
                        "    \"issn\": \"12345\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 78,\r\n" + //
                        "    \"title\": \"Time\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Managing Editor\",\r\n" + //
                        "    \"issn\": \"67890\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 58,\r\n" + //
                        "    \"title\": \"Vogue\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Fashion Editor\",\r\n" + //
                        "    \"issn\": \"54321\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 39,\r\n" + //
                        "    \"title\": \"Scientific American\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Science Editor\",\r\n" + //
                        "    \"issn\": \"98765\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 19,\r\n" + //
                        "    \"title\": \"Sports Illustrated\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Sports Editor\",\r\n" + //
                        "    \"issn\": \"13579\"\r\n" + //
                        "  }\r\n" + //
                        "]");
    }

    @Test
    public void givenPublicationsTitleATypeMagazine_whenGetPublications_thenMagazinePublicationsWithAInTitleAreReturned() {
        webTestClient
        .get()
        .uri("/publications?title=a&type=Magazine")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("[\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 97,\r\n" + //
                        "    \"title\": \"National Geographic\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Editor-in-Chief\",\r\n" + //
                        "    \"issn\": \"12345\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 39,\r\n" + //
                        "    \"title\": \"Scientific American\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Science Editor\",\r\n" + //
                        "    \"issn\": \"98765\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 19,\r\n" + //
                        "    \"title\": \"Sports Illustrated\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Sports Editor\",\r\n" + //
                        "    \"issn\": \"13579\"\r\n" + //
                        "  }\r\n" + //
                        "]");
    }

    @Test
    public void givenPublicationsStockZero_whenGetPublications_thenAllPublicationsAreReturned() {
        webTestClient
        .get()
        .uri("/publications/stock/0")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("[\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 9,\r\n" + //
                        "    \"title\": \"The Great Gatsby\",\r\n" + //
                        "    \"year\": 1925,\r\n" + //
                        "    \"author\": \"F. Scott Fitzgerald\",\r\n" + //
                        "    \"isbn\": \"978-0743273565\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 14,\r\n" + //
                        "    \"title\": \"To Kill a Mockingbird\",\r\n" + //
                        "    \"year\": 1960,\r\n" + //
                        "    \"author\": \"Harper Lee\",\r\n" + //
                        "    \"isbn\": \"978-0061120084\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 18,\r\n" + //
                        "    \"title\": \"1984\",\r\n" + //
                        "    \"year\": 1949,\r\n" + //
                        "    \"author\": \"George Orwell\",\r\n" + //
                        "    \"isbn\": \"978-0451524935\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 10,\r\n" + //
                        "    \"title\": \"Pride and Prejudice\",\r\n" + //
                        "    \"year\": 1813,\r\n" + //
                        "    \"author\": \"Jane Austen\",\r\n" + //
                        "    \"isbn\": \"978-0141439518\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 5,\r\n" + //
                        "    \"title\": \"The Catcher in the Rye\",\r\n" + //
                        "    \"year\": 1951,\r\n" + //
                        "    \"author\": \"J.D. Salinger\",\r\n" + //
                        "    \"isbn\": \"978-0316769488\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 97,\r\n" + //
                        "    \"title\": \"National Geographic\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Editor-in-Chief\",\r\n" + //
                        "    \"issn\": \"12345\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 78,\r\n" + //
                        "    \"title\": \"Time\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Managing Editor\",\r\n" + //
                        "    \"issn\": \"67890\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 58,\r\n" + //
                        "    \"title\": \"Vogue\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Fashion Editor\",\r\n" + //
                        "    \"issn\": \"54321\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 39,\r\n" + //
                        "    \"title\": \"Scientific American\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Science Editor\",\r\n" + //
                        "    \"issn\": \"98765\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 19,\r\n" + //
                        "    \"title\": \"Sports Illustrated\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Sports Editor\",\r\n" + //
                        "    \"issn\": \"13579\"\r\n" + //
                        "  }\r\n" + //
                        "]");
    }

    @Test
    public void givenPublicationsStockNegative_whenGetPublications_thenServerErrorIsThrown() {
        webTestClient
        .get()
        .uri("/publications/stock/-1")
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody()
        .json("{\r\n" + //
                        "  \""+ServiceException.class.getSimpleName()+"\": \""+PublicationService.NEGATIVE_AVAILABLE_COPIES_EXCEPTION+"\"\r\n" + //
                        "}");
    }

    @Test
    public void givenPublicationsStock40_whenGetPublications_thenPublicationsWithMoreCopiesThan40AreReturned() {
        webTestClient
        .get()
        .uri("/publications/stock/40")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .json("[\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 97,\r\n" + //
                        "    \"title\": \"National Geographic\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Editor-in-Chief\",\r\n" + //
                        "    \"issn\": \"12345\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 78,\r\n" + //
                        "    \"title\": \"Time\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Managing Editor\",\r\n" + //
                        "    \"issn\": \"67890\"\r\n" + //
                        "  },\r\n" + //
                        "  {\r\n" + //
                        "    \"availableCopies\": 58,\r\n" + //
                        "    \"title\": \"Vogue\",\r\n" + //
                        "    \"year\": 2022,\r\n" + //
                        "    \"editor\": \"Fashion Editor\",\r\n" + //
                        "    \"issn\": \"54321\"\r\n" + //
                        "  }\r\n" + //
                        "]");
    }
}
