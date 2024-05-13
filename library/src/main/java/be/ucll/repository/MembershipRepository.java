package be.ucll.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import be.ucll.model.Membership;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    @Query("SELECT m FROM Membership m WHERE m.user.email = :email " +
           "AND m.startDate <= :givenDate " +
           "AND m.endDate >= :givenDate")
    Optional<Membership> findMembershipByUserEmailAndDate(@Param("email") String email, 
                                                          @Param("givenDate") LocalDate givenDate);
}