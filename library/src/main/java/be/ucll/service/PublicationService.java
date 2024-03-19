package be.ucll.service;

import java.util.List;

import org.springframework.stereotype.Service;

import be.ucll.model.Publication;
import be.ucll.repository.PublicationRepository;

@Service
public class PublicationService {
    
    private PublicationRepository publicationRepository;

    public PublicationService(PublicationRepository publicationRepository) {
        this.publicationRepository = publicationRepository;
    }

    public List<Publication> findPublicationsByTitleAndType(String title, String type) {
        return publicationRepository.publicationsByTitleAndType(title, type);
    }
}
