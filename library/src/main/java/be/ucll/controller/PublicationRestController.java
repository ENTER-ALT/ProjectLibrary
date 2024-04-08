package be.ucll.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.model.Publication;
import be.ucll.service.PublicationService;



@RestController
@RequestMapping("/publications")
public class PublicationRestController {
    
    private PublicationService publicationService;

    public PublicationRestController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @GetMapping()
    public List<Publication> getPublications(
        @RequestParam(value = "title", required = false) String title,
        @RequestParam(value = "type", required = false) String type
        ) {
        return publicationService.findPublicationsByTitleAndType(title, type);
    }

    @GetMapping("/stock/{availableCopies}")
    public List<Publication> getPublications(
        @PathVariable(value = "availableCopies") Integer availableCopies
        ) {
        return publicationService.findPublicationsWithMoreAvailableCopiesThan(availableCopies);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({Exception.class})
    public Map<String, String> handleException(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getClass().getSimpleName(), ex.getMessage());
        return errors;
    }
}
