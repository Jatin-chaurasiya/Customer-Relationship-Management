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
		inquiryRespository.save(inquiry);
	}

	public List<Inquiry> findInquiresByPhoneno(String phoneno) {
		return inquiryRespository.findByPhoneno(phoneno);
	}

}
