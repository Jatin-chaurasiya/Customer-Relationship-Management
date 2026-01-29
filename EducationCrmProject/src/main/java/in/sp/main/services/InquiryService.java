package in.sp.main.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sp.main.entities.Inquiry;
import in.sp.main.repositories.InquiryRespository;

@Service
public class InquiryService {

	@Autowired
	private InquiryRespository inquiryRespository;

	public void addNewInquiry(Inquiry inquiry) {
		// Validation
		if (inquiry == null) {
			throw new IllegalArgumentException("Inquiry cannot be null");
		}

		if (inquiry.getPhoneno() == null || inquiry.getPhoneno().trim().isEmpty()) {
			throw new IllegalArgumentException("Phone number is required");
		}

		if (inquiry.getName() == null || inquiry.getName().trim().isEmpty()) {
			throw new IllegalArgumentException("Customer name is required");
		}

		inquiryRespository.save(inquiry);
	}

	public List<Inquiry> findInquiresByPhoneno(String phoneno) {
		if (phoneno == null || phoneno.trim().isEmpty()) {
			return List.of(); // Empty list
		}
		return inquiryRespository.findByPhoneno(phoneno.trim());
	}
}