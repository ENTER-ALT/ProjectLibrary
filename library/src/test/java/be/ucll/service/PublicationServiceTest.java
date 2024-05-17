package be.ucll.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import be.ucll.model.Book;
import be.ucll.model.Magazine;
import be.ucll.model.Publication;
import be.ucll.repository.DbInitializer;
import be.ucll.repository.PublicationRepository;
import be.ucll.utilits.TimeTracker;

@ExtendWith(MockitoExtension.class)
public class PublicationServiceTest {
    
    @Mock
    private PublicationRepository publicationRepository;

    @InjectMocks
    private PublicationService publicationService;

    @BeforeEach
    public void resetTime() {
        TimeTracker.resetToday();
        TimeTracker.resetYear();
    }

@Test
    public void givenNullParams_whenRequestingPublications_thanAllPublicationsReturned() {
        List<Publication> expectedPublications = new ArrayList<>();
        expectedPublications.addAll(DbInitializer.createDefaultMagazineList());
        expectedPublications.addAll(DbInitializer.createDefaultBookList());

        when(publicationRepository.findByTitleAndType(null, null)).thenReturn(expectedPublications);

        List<Publication> actualPublications = publicationService.findPublicationsByTitleAndType(null, null);

        assertEquals(expectedPublications.size(), actualPublications.size());
        expectedPublications.forEach(publication -> assertTrue(actualPublications.contains(publication)));

        verify(publicationRepository).findByTitleAndType(null, null);
    }

    @Test
    public void givenEmptyParams_whenRequestingPublications_thanNoPublicationsReturned() {
        when(publicationRepository.findByTitleAndType("", "")).thenReturn(new ArrayList<>());

        List<Publication> actualPublications = publicationService.findPublicationsByTitleAndType("", "");

        assertEquals(0, actualPublications.size());
        
        verify(publicationRepository).findByTitleAndType("", "");
    }

    @Test
    public void givenTitleAndEmptyTypeParams_whenRequestingPublications_thanNoPublicationsReturned() {
        when(publicationRepository.findByTitleAndType("a", "")).thenReturn(new ArrayList<>());

        List<Publication> actualPublications = publicationService.findPublicationsByTitleAndType("a", "");

        assertEquals(0, actualPublications.size());
        
        verify(publicationRepository).findByTitleAndType("a", "");
    }

    @Test
    public void givenEmptyTitleAndBookTypeParams_whenRequestingPublications_thanOnlyBooksReturned() {
        List<Publication> books = new ArrayList<>(DbInitializer.createDefaultBookList());
        when(publicationRepository.findByTitleAndType("", "Book")).thenReturn(books);

        List<Publication> actualPublications = publicationService.findPublicationsByTitleAndType("", "Book");

        assertEquals(books.size(), actualPublications.size());
        actualPublications.forEach(publication -> assertTrue(books.contains(publication)));
        
        verify(publicationRepository).findByTitleAndType("", "Book");
    }

    @Test
    public void givenNullTitleAndBookTypeParams_whenRequestingPublications_thanOnlyBooksReturned() {
        List<Publication> books = new ArrayList<>(DbInitializer.createDefaultBookList());
        when(publicationRepository.findByTitleAndType(null, "Book")).thenReturn(books);

        List<Publication> actualPublications = publicationService.findPublicationsByTitleAndType(null, "Book");

        assertEquals(books.size(), actualPublications.size());
        actualPublications.forEach(publication -> assertTrue(books.contains(publication)));
        
        verify(publicationRepository).findByTitleAndType(null, "Book");
    }

    @Test
    public void givenEmptyTitleAndMagazineTypeParams_whenRequestingPublications_thanOnlyMagazinesReturned() {
        List<Publication> magazines = new ArrayList<>(DbInitializer.createDefaultMagazineList());
        when(publicationRepository.findByTitleAndType("", "Magazine")).thenReturn(magazines);

        List<Publication> actualPublications = publicationService.findPublicationsByTitleAndType("", "Magazine");

        assertEquals(magazines.size(), actualPublications.size());
        actualPublications.forEach(publication -> assertTrue(magazines.contains(publication)));
        
        verify(publicationRepository).findByTitleAndType("", "Magazine");
    }

    @Test
    public void givenNullTitleAndMagazineTypeParams_whenRequestingPublications_thanOnlyMagazinesReturned() {
        List<Publication> magazines = new ArrayList<>(DbInitializer.createDefaultMagazineList());
        when(publicationRepository.findByTitleAndType(null, "Magazine")).thenReturn(magazines);

        List<Publication> actualPublications = publicationService.findPublicationsByTitleAndType(null, "Magazine");

        assertEquals(magazines.size(), actualPublications.size());
        actualPublications.forEach(publication -> assertTrue(magazines.contains(publication)));
        
        verify(publicationRepository).findByTitleAndType(null, "Magazine");
    }

    @Test
    public void givenTitleAndBookTypeParams_whenRequestingPublications_thanBooksWithTitleReturned() {
        List<Publication> booksWithTitle = new ArrayList<>(DbInitializer.createDefaultBookList());
        booksWithTitle = booksWithTitle.stream().filter(book -> book.getTitle().contains("a")).toList();
        final List<Publication> excpectedPublications = booksWithTitle;
        when(publicationRepository.findByTitleAndType("a", "Book")).thenReturn(booksWithTitle);

        List<Publication> actualPublications = publicationService.findPublicationsByTitleAndType("a", "Book");

        assertNotEquals(DbInitializer.createDefaultBookList().size(), actualPublications.size());
        actualPublications.forEach(publication -> {
            assertTrue(excpectedPublications.contains(publication));
            assertTrue(publication.getTitle().contains("a"));
        });

        verify(publicationRepository).findByTitleAndType("a", "Book");
    }

    @Test
    public void givenZeroCopiesParams_whenRequestingPublicationsByCopies_thanAllPublicationsReturned() {
        List<Publication> expectedPublications = new ArrayList<>();
        expectedPublications.addAll(DbInitializer.createDefaultMagazineList());
        expectedPublications.addAll(DbInitializer.createDefaultBookList());

        when(publicationRepository.findByAvailableCopiesGreaterThanEqual(0)).thenReturn(expectedPublications);

        List<Publication> actualPublications = publicationService.findPublicationsWithMoreAvailableCopiesThan(0);

        assertEquals(expectedPublications.size(), actualPublications.size());
        expectedPublications.forEach(publication -> assertTrue(actualPublications.contains(publication)));

        verify(publicationRepository).findByAvailableCopiesGreaterThanEqual(0);
    }

    @Test
    public void given40CopiesParams_whenRequestingPublicationsByCopies_thanFilteredPublicationsReturned() {
        List<Book> books = DbInitializer.createDefaultBookList();
        List<Magazine> magazines = DbInitializer.createDefaultMagazineList();
        List<Publication> publications = new ArrayList<>();
        publications.addAll(magazines);
        publications.addAll(books);
        final List<Publication> filteredPublications = publications.stream().filter(publication -> publication.getAvailableCopies() >= 40).toList();
        when(publicationRepository.findByAvailableCopiesGreaterThanEqual(40)).thenReturn(filteredPublications);

        List<Publication> actualPublications = publicationService.findPublicationsWithMoreAvailableCopiesThan(40);

        assertEquals(filteredPublications.size(), actualPublications.size());
        actualPublications.forEach(publication -> {
            assertTrue(filteredPublications.contains(publication));
            assertTrue(publication.getAvailableCopies() >= 40);
        });

        verify(publicationRepository).findByAvailableCopiesGreaterThanEqual(40);
    }

    @Test
    public void givenNegativeCopiesParams_whenRequestingPublicationsByCopies_thanExceptionThrown() {
        Integer availableCopies = -1;

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            publicationService.findPublicationsWithMoreAvailableCopiesThan(availableCopies);
        });

        String expectedMessage = PublicationService.NEGATIVE_AVAILABLE_COPIES_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
