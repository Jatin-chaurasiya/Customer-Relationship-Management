package in.sp.main.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.sp.main.entities.Inquiry;

@Repository
public interface InquiryRespository extends JpaRepository<Inquiry, Long> {

	List<Inquiry> findByPhoneno(String Phoneno);

}
