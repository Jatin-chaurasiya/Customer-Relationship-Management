package in.sp.main.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.sp.main.entities.FollowUps;

@Repository
public interface FollowUpsRepository extends JpaRepository<FollowUps, Long> {

	Optional<FollowUps> findByPhoneno(String phoneno);

	List<FollowUps> findByEmpEmailAndFollowUpDate(String empEmail, String followUpDate);

}
