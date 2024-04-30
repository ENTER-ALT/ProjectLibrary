package be.ucll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.ucll.model.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
}