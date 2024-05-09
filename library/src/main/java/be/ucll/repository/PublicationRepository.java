package be.ucll.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import be.ucll.model.Publication;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {

    public List<Publication> findByAvailableCopiesGreaterThanEqual(Integer copies);

    @Query("SELECT p FROM Publication p WHERE (:title IS NULL OR p.title LIKE %:title%) " +
            "AND (:type IS NULL OR p.type = LOWER(:type))")
    List<Publication> findByTitleAndType(String title, String type);
}
