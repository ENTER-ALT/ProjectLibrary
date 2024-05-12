package be.ucll.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import be.ucll.model.Publication;
import be.ucll.repository.PublicationRepository;

@Service
public class PublicationService {
    
    public static final String NEGATIVE_AVAILABLE_COPIES_EXCEPTION = "Available copies cannot be negative";
    public static final String PUBLICATION_NOT_FOUND_EXCEPTION = "Publication with id %d not found.";


    private PublicationRepository publicationRepository;

    public PublicationService(PublicationRepository publicationRepository) {
        this.publicationRepository = publicationRepository;
    }

    public List<Publication> findPublicationsByTitleAndType(String title, String type) {
        return publicationRepository.findByTitleAndType(title, type);
    }

    public List<Publication> findPublicationsWithMoreAvailableCopiesThan(Integer copies) {
        if (copies < 0) {
            throw new ServiceException(NEGATIVE_AVAILABLE_COPIES_EXCEPTION);
        }
        return publicationRepository.findByAvailableCopiesGreaterThanEqual(copies);
    }

    public List<Publication> getPublicationsById(List<Long> publicationsId) {
        List<Publication> result = new ArrayList<>();
        publicationsId.forEach(publicationId -> {
            Publication foundPublication = getPublicationById(publicationId);
            result.add(foundPublication);
        });
        return result;
    }

    public Publication getPublicationById(Long publicationId) {
        Publication actualPublication = publicationRepository.findById(publicationId).orElse(null);
        if (actualPublication == null) {
            String message = String.format(PUBLICATION_NOT_FOUND_EXCEPTION, publicationId);
            throw new ServiceException(message);
        }
        return actualPublication;
    }
}
