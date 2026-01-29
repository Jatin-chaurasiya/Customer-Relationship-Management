package in.sp.main.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.sp.main.entities.Employee;
import in.sp.main.entities.Inquiry;
import in.sp.main.services.InquiryService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class InquiryApi {

	@Autowired
	private InquiryService inquiryService;

	// ✅ Search Inquiries API - Employee Login Required
	@GetMapping("/searchInquiries")
	public ResponseEntity<?> searchInquiries(@RequestParam("phoneNumber") String phoneNumber, HttpSession session) {

		// Session check
		Employee employee = (Employee) session.getAttribute("sessionEmployee");
		if (employee == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please login first");
		}

		try {
			// Validation
			if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("Phone number is required");
			}

			List<Inquiry> inquiries = inquiryService.findInquiresByPhoneno(phoneNumber.trim());
			return ResponseEntity.ok(inquiries);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to search inquiries");
		}
	}
}