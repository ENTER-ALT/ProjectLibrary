package be.ucll.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.ucll.model.Loan;
import jakarta.transaction.Transactional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Transactional
    @Modifying
    void deleteByUserEmail(String email);

    List<Loan> findByUserEmail(String email);

    List<Loan> findByUserEmailAndEndDateAfter(String email, LocalDate currentDate);
}
