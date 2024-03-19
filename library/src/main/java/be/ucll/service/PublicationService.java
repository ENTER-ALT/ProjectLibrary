package be.ucll.service;

import java.util.List;

import org.springframework.stereotype.Service;

import be.ucll.model.Publication;
import be.ucll.repository.PublicationRepository;

@Service
public class PublicationService {
    
    public static final String NEGATIVE_AVAILABLE_COPIES_EXCEPTION = "Available copies cannot be negative";

    private PublicationRepository publicationRepository;

    public PublicationService(PublicationRepository publicationRepository) {
        this.publicationRepository = publicationRepository;
    }

    public List<Publication> findPublicationsByTitleAndType(String title, String type) {
        return publicationRepository.publicationsByTitleAndType(title, type);
    }

    public List<Publication> findPublicationsWithMoreAvailableCopiesThan(Integer copies) {
        if (copies < 0) {
            throw new ServiceException(NEGATIVE_AVAILABLE_COPIES_EXCEPTION);
        }
        return publicationRepository.publicationsWithMoreAvailableCopiesThan(copies);
    }
}
