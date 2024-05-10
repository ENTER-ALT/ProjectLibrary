package be.ucll.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.ucll.model.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    void deleteByUserEmail(String email);

    List<Loan> findByUserEmail(String email);

    List<Loan> findByUserEmailAndEndDateIsNull(String email);
}
